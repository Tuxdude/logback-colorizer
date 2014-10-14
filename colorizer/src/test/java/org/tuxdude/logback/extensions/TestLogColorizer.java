/**
 *
 *  Copyright (C) 2014 Ash (Tuxdude) [tuxdude.github@gmail.com]
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

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggerContextVO;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

/**
 * Test the LogColorizer.
 */
public class TestLogColorizer {

    private ILoggingEvent getMockEvent() {
        return mock(ILoggingEvent.class);
    }

    private void setLogbackProperty(
            final ILoggingEvent mockEvent,
            final String propertyKey,
            final String propertyValue) {
        LoggerContextVO mockContext = mock(LoggerContextVO.class);
        Map<String, String> mockPropertiesMap = new HashMap<String, String>();
        mockPropertiesMap.put(propertyKey, propertyValue);
        when(mockEvent.getLoggerContextVO()).thenReturn(mockContext);
        when(mockContext.getPropertyMap()).thenReturn(mockPropertiesMap);
    }

    private void setLogLevel(final ILoggingEvent mockEvent, final Level level) {
        when(mockEvent.getLevel()).thenReturn(level);
    }

    private String invokeTransform(
            final LogColorizer logColorizer,
            final ILoggingEvent mockEvent,
            final String input) {
        String result = null;
        logColorizer.transform(mockEvent, input);
        return result;
    }

    private String expectedTransform(final String input,
                                     final String foregroundColor,
                                     final Boolean isForegroundBold,
                                     final String backgroundColor,
                                     final Boolean isBackgroundBold) {
        String result = "";
        if (foregroundColor != null) {
            result += LogColorizer.ESC_START;
            if (isForegroundBold) {
                result += LogColorizer.COLOR_BOLD;
            }
            result += LogColorizer.FG_PREFIX + foregroundColor + LogColorizer.ESC_END;
        }
        if (backgroundColor != null) {
            result += LogColorizer.ESC_START;
            if (isBackgroundBold) {
                result += LogColorizer.COLOR_BOLD;
            }
            result += LogColorizer.BG_PREFIX + backgroundColor + LogColorizer.ESC_END;
        }
        return result + input + LogColorizer.COLOR_RESET_ALL;
    }

    /**
     * Tests all the constant values exposed from LogColorizer to
     * ensure they're valid.
     */
    @Test
    public void testConstants() {
        assertEquals(LogColorizer.COLORIZER_PROPERTY, "COLORIZER_COLORS");

        assertEquals(LogColorizer.ESC_START, "\033[");
        assertEquals(LogColorizer.ESC_END, "m");

        assertEquals(LogColorizer.COLOR_BOLD, "1;");

        assertEquals(LogColorizer.COLOR_BLACK, "0");
        assertEquals(LogColorizer.COLOR_RED, "1");
        assertEquals(LogColorizer.COLOR_GREEN, "2");
        assertEquals(LogColorizer.COLOR_YELLOW, "3");
        assertEquals(LogColorizer.COLOR_BLUE, "4");
        assertEquals(LogColorizer.COLOR_MAGENTA, "5");
        assertEquals(LogColorizer.COLOR_CYAN, "6");
        assertEquals(LogColorizer.COLOR_WHITE, "7");

        assertEquals(LogColorizer.COLOR_RESET, "0");

        assertEquals(LogColorizer.FG_PREFIX, "3");
        assertEquals(LogColorizer.BG_PREFIX, "4");

        assertEquals(
                LogColorizer.COLOR_RESET_ALL,
                LogColorizer.ESC_START + LogColorizer.COLOR_RESET + LogColorizer.ESC_END);

        assertEquals(LogColorizer.COLOR_NAME_BLACK, "black");
        assertEquals(LogColorizer.COLOR_NAME_RED, "red");
        assertEquals(LogColorizer.COLOR_NAME_GREEN, "green");
        assertEquals(LogColorizer.COLOR_NAME_YELLOW, "yellow");
        assertEquals(LogColorizer.COLOR_NAME_BLUE, "blue");
        assertEquals(LogColorizer.COLOR_NAME_MAGENTA, "magenta");
        assertEquals(LogColorizer.COLOR_NAME_CYAN, "cyan");
        assertEquals(LogColorizer.COLOR_NAME_WHITE, "white");

        assertEquals(LogColorizer.COLORIZER_PROPERTY, "COLORIZER_COLORS");
    }

    /**
     * Test various color combinations for properties, and the
     * result.
     */
    @Test
    public void testColorCombinations() {
        LogColorizer logColorizer = new LogColorizer();
        ILoggingEvent mockEvent = getMockEvent();
        setLogbackProperty(
                mockEvent,
                LogColorizer.COLORIZER_PROPERTY,
                "red@,boldgreen@magenta,@blue,yell@,@");

        String input = "Test Input";

        setLogLevel(mockEvent, Level.ERROR);
        assertEquals(
                expectedTransform(
                        input,
                        LogColorizer.COLOR_RED,
                        false,
                        null,
                        false),
                logColorizer.transform(mockEvent, input)
        );

        setLogLevel(mockEvent, Level.WARN);
        assertEquals(
                expectedTransform(
                        input,
                        LogColorizer.COLOR_GREEN,
                        true,
                        LogColorizer.COLOR_MAGENTA,
                        false),
                logColorizer.transform(mockEvent, input)
        );

        setLogLevel(mockEvent, Level.INFO);
        assertEquals(
                expectedTransform(
                        input,
                        null,
                        false,
                        LogColorizer.COLOR_BLUE,
                        false),
                logColorizer.transform(mockEvent, input)
        );

        setLogLevel(mockEvent, Level.DEBUG);
        assertEquals(
                input,
                logColorizer.transform(mockEvent, input)
        );

        setLogLevel(mockEvent, Level.TRACE);
        assertEquals(
                input,
                logColorizer.transform(mockEvent, input)
        );
    }
}
