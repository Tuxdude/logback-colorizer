<h1>logback-colorizer</h1>

<b>v1.0.0</b>

Overview
========

[`logback-colorizer`][1] is a customizable log colorizer for [`logback`][21],
based on log levels. This extension is far more customizable than the
built-in coloring provided using [`highlight`][22] keyword of [`logback`][21].

Works with any of the logback implementations such as [`logback-classic`][21],
[`logback-android`][23].

Quick Start
===========
1. Add [`logback-colorizer`][1] to your project's class path.
2. Configure the colors for different log levels using the `COLORIZER_COLORS`
property in `logback.xml`.
3. Choose any `conversionWord` as the keyword for applying colors, and
add a new `conversionRule`. Set the `converterClass` as
`org.tuxdude.logback.extensions.LogColorizer` in `logback.xml`.
4. Use the previously chosen `conversionWord` in the `pattern` of your
`appender`'s `encoder` in `logback.xml`.

Example logback.xml
===================
```xml
<configuration>
    <property scope="context" name="COLORIZER_COLORS" value="boldred@white,yellow@black,green@,blue@,cyan@" />
    <conversionRule conversionWord="colorize" converterClass="org.tuxdude.logback.extensions.LogColorizer" />
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %colorize(%msg%n)</pattern>
        </encoder>
    </appender>

    <root level="TRACE">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```

Download
========
* [logback-colorizer-1.0.0.jar][2]
* `Maven` and `Gradle` based dependencies coming soon

Available Colors
================
Foreground and background colors can be set independently for each log level.
The following colors are available:

* `black`
* `red`
* `green`
* `yellow`
* `blue`
* `magenta`
* `cyan`
* `white`

In addition a `bold` prefix is also supported with each color, for example
`boldred`.

Choosing the colors for each log level
======================================
Specify the colors for each log level using the `COLORIZER_COLORS` property
in `logback.xml`. It takes a comma-separated list of 5 color configurations,
one for each log level.

Example:

`boldred@white,yellow@black,green@,blue@,cyan@`
 
 The format is:

`COLOR_CONFIG_ERROR,COLOR_CONFIG_WARN,COLOR_CONFIG_INFO,COLOR_CONFIG_DEBUG,COLOR_CONFIG_TRACE`

Each color configuration specifies a foreground and a background color:

`foreground@background`

If a color is left blank (no whitespaces), then the log color is left
unchanged.

Example color configuration, which sets the foreground color to `white` and
background color to `boldblue`.

`white@boldblue`

Example color configuration, which sets the foreground color to `magenta` and
leaves the background color unchanged:

`magenta@`

Example color configuration, which sets the background color to `boldgreen`
and leaves the foreground color unchanged:

`@boldgreen`

Example color configuration, which leaves both the foreground and background
colors untouched.

`@`


[1]: https://github.com/Tuxdude/logback-colorizer
[2]: https://github.com/Tuxdude/maven-artifacts/blob/master/org/tuxdude/logback/extensions/logback-colorizer/1.0.0/logback-colorizer-1.0.0.jar
[21]: http://logback.qos.ch/
[22]: http://logback.qos.ch/manual/layouts.html#coloring
[23]: http://tony19.github.io/logback-android/index.html
