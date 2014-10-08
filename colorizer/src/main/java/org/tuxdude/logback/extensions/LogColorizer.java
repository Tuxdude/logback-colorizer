/**
 *
 *  Copyright (C) 2014 Ash (Tuxdude) <tuxdude.github@gmail.com>
 *
 *  This file is part of logback-colorizer.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.tuxdude.logback.extensions;

import java.util.Locale;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;


/**
 * A custom highlighting Composite Converter for logback.
 */
public class LogColorizer extends CompositeConverter<ILoggingEvent> {

    /**
     * Escape sequence Start.
     */
    public static final String ESC_START = "\033[";
    /**
     * Escape sequence End.
     */
    public static final String ESC_END = "m";

    public static final String COLOR_BOLD = "1;";

    public static final String COLOR_BLACK = "0";
    public static final String COLOR_RED = "1";
    public static final String COLOR_GREEN = "2";
    public static final String COLOR_YELLOW = "3";
    public static final String COLOR_BLUE = "4";
    public static final String COLOR_MAGENTA = "5";
    public static final String COLOR_CYAN = "6";
    public static final String COLOR_WHITE = "7";

    public static final String COLOR_RESET = "0";

    public static final String FG_PREFIX = "3";
    public static final String BG_PREFIX = "4";

    public static final String COLOR_RESET_ALL = ESC_START + COLOR_RESET + ESC_END;

    public static final String COLOR_NAME_BLACK = "black";
    public static final String COLOR_NAME_RED = "red";
    public static final String COLOR_NAME_GREEN = "green";
    public static final String COLOR_NAME_YELLOW = "yellow";
    public static final String COLOR_NAME_BLUE = "blue";
    public static final String COLOR_NAME_MAGENTA = "magenta";
    public static final String COLOR_NAME_CYAN = "cyan";
    public static final String COLOR_NAME_WHITE = "white";

    public static final String COLORIZER_PROPERTY = "COLORIZER_COLORS";

    private static final int NUM_COLORS_IN_PROPERTY = 5;

    protected static String mColorError = null;
    protected static String mColorWarn = null;
    protected static String mColorInfo = null;
    protected static String mColorDebug = null;
    protected static String mColorTrace = null;
    protected static Boolean mIsInitialized = false;
    protected static Boolean mIsColorizerEnabled = false;

    @Override
    protected String transform(final ILoggingEvent event, final String input) {
        if (!mIsInitialized) {
            setUpColors(event);
        }

        String transformedValue = input;
        Boolean isTransformed = false;

        if (mIsColorizerEnabled) {
            final StringBuilder stringBuilder = new StringBuilder();

            switch (event.getLevel().toInt()) {
                case Level.ERROR_INT:
                    if (mColorError != null) {
                        stringBuilder.append(mColorError);
                        isTransformed = true;
                    }
                    break;
                case Level.WARN_INT:
                    if (mColorWarn != null) {
                        stringBuilder.append(mColorWarn);
                        isTransformed = true;
                    }
                    break;
                case Level.INFO_INT:
                    if (mColorInfo != null) {
                        stringBuilder.append(mColorInfo);
                        isTransformed = true;
                    }
                    break;
                case Level.DEBUG_INT:
                    if (mColorDebug != null) {
                        stringBuilder.append(mColorDebug);
                        isTransformed = true;
                    }
                    break;
                case Level.TRACE_INT:
                    if (mColorTrace != null) {
                        stringBuilder.append(mColorTrace);
                        isTransformed = true;
                    }
                    break;
                default:
                    break;
            }

            if (isTransformed) {
                stringBuilder.append(input);
                stringBuilder.append(COLOR_RESET_ALL);
                transformedValue = stringBuilder.toString();
            }
        }

        return transformedValue;
    }

    protected void setUpColors(final ILoggingEvent event) {
        final Map<String, String> properties = event.getLoggerContextVO().getPropertyMap();
        if (properties.containsKey(COLORIZER_PROPERTY)) {
            String colorProperty = properties.get(COLORIZER_PROPERTY);
            if (colorProperty != null) {
                colorProperty = colorProperty.toLowerCase(Locale.ENGLISH).replaceAll("\\s+", "");
                final String[] colors = colorProperty.split(",", -1);
                if (colors.length == NUM_COLORS_IN_PROPERTY) {
                    mColorError = parseColorProperty(colors[0]);
                    mColorWarn = parseColorProperty(colors[1]);
                    mColorInfo = parseColorProperty(colors[2]);
                    mColorDebug = parseColorProperty(colors[3]);
                    mColorTrace = parseColorProperty(colors[4]);
                    mIsColorizerEnabled =
                            mColorError != null || mColorWarn != null || mColorInfo != null
                            || mColorDebug != null || mColorTrace != null;
                }
            }
        }
        mIsInitialized = true;
    }

    protected String parseColorProperty(final String colorProperty) {
        String result = null;
        if (colorProperty != null && colorProperty.contains("@")) {
            // colorProperty can be one of the following
            // fgcolor@bgcolor
            // fgcolor@
            // @bgcolor
            // @
            final String[] colors = colorProperty.split("@", -1);
            if (colors.length == 2) {
                result = parseColors(colors[0], colors[1]);
            }
        }
        return result;
    }

    protected String parseColors(
            final String foregroundColorProperty,
            final String backgroundColorProperty) {
        String result = null;
        if (foregroundColorProperty != null && backgroundColorProperty != null) {
            final StringBuilder stringBuilder = new StringBuilder();
            if (getColor(stringBuilder, foregroundColorProperty, true) &&
                    getColor(stringBuilder, backgroundColorProperty, false) &&
                    stringBuilder.length() > 0) {
                result = stringBuilder.toString();
            }
        }
        return result;
    }

    protected Boolean getColor(
            final StringBuilder stringBuilder,
            final String colorNameParameter,
            final Boolean isForeground) {
        // Non-Bold FG - ESC_START + FG_PREFIX + COLOR + ESC_END
        // Bold FG - ESC_START + BOLD + FG_PREFIX + COLOR + ESC_END
        // BG - ESC_START + BG_PREFIX + COLOR + ESC_END
        // Bold BG - ESC_START + BOLD + BG_PREFIX + COLOR + ESC_END
        Boolean isValid = true;

        if (colorNameParameter.length() > 0) {
            stringBuilder.append(ESC_START);

            String colorName = colorNameParameter;
            if (colorName.startsWith("bold")) {
                stringBuilder.append(COLOR_BOLD);
                colorName = colorNameParameter.substring(4);
            }

            if (isForeground) {
                stringBuilder.append(FG_PREFIX);
            } else {
                stringBuilder.append(BG_PREFIX);
            }

            if (colorName.equals(COLOR_NAME_BLACK)) {
                stringBuilder.append(COLOR_BLACK);
            } else if (colorName.equals(COLOR_NAME_RED)) {
                stringBuilder.append(COLOR_RED);
            } else if (colorName.equals(COLOR_NAME_GREEN)) {
                stringBuilder.append(COLOR_GREEN);
            } else if (colorName.equals(COLOR_NAME_YELLOW)) {
                stringBuilder.append(COLOR_YELLOW);
            } else if (colorName.equals(COLOR_NAME_BLUE)) {
                stringBuilder.append(COLOR_BLUE);
            } else if (colorName.equals(COLOR_NAME_MAGENTA)) {
                stringBuilder.append(COLOR_MAGENTA);
            } else if (colorName.equals(COLOR_NAME_CYAN)) {
                stringBuilder.append(COLOR_CYAN);
            } else if (colorName.equals(COLOR_NAME_WHITE)) {
                stringBuilder.append(COLOR_WHITE);
            } else {
                // A non-empty invalid color name
                isValid = false;
            }

            if (isValid) {
                stringBuilder.append(ESC_END);
            }
        }

        return isValid;
    }
}
