<h1>logback-colorizer</h1>

<b>v1.0.1</b>

Build Status
============

|   **Branch/Tag**   |                                                               **Status**                                                              |
|:------------------:|:-------------------------------------------------------------------------------------------------------------------------------------:|
|     **v1.0.1**     | [![Build Status](https://travis-ci.org/Tuxdude/logback-colorizer.svg?branch=v1.0.1)](https://travis-ci.org/Tuxdude/logback-colorizer) |
|     **Master**     | [![Build Status](https://travis-ci.org/Tuxdude/logback-colorizer.svg?branch=master)](https://travis-ci.org/Tuxdude/logback-colorizer) |
|      **Dev**       | [![Build Status](https://travis-ci.org/Tuxdude/logback-colorizer.svg?branch=dev)](https://travis-ci.org/Tuxdude/logback-colorizer)    |

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
* *Gradle*

    - Add the following dependency in the project's `build.gradle`

    ```groovy
    dependencies {
        ...
        ...
        compile 'org.tuxdude.logback.extensions:logback-colorizer:1.0.1'
        ...
        ...
    }
    ```

* *Maven*

    - Add the following dependency in the project's `pom.xml`

    ```xml
    <dependencies>
        ...
        ...
        <dependency>
            <groupId>org.tuxdude.logback.extensions</groupId>
            <artifactId>logback-colorizer</artifactId>
            <version>1.0.1</version>
            <type>jar</type>
        </dependency>
        ...
        ...
    </dependencies>
    ```

* Manually download the JAR from here: [`logback-colorizer-1.0.1.jar`][2]

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

```xml
<property scope="context" name="COLORIZER_COLORS" value="boldred@white,yellow@black,green@,blue@,cyan@" />
```
 
The order of specifying the log colors for different log levels is:

`Error,Warn,Info,Debug,Trace`

Each color configuration specifies a foreground and a background color:

`foreground@background`

If a color is left blank (no whitespaces), then the log color is left
unchanged.

- *Example color configuration, which sets the foreground color to `white` and
background color to `boldblue`.*

    `white@boldblue`

- *Example color configuration, which sets the foreground color to `magenta` and
leaves the background color unchanged:*

    `magenta@`

- *Example color configuration, which sets the background color to `boldgreen`
and leaves the foreground color unchanged:*

    `@boldgreen`

- *Example color configuration, which leaves both the foreground and background
colors untouched:*

    `@`

Some valid example properties:

```xml
<property scope="context" name="COLORIZER_COLORS" value="red@,yellow@,green@,blue@,cyan@" />
```

```xml
<property scope="context" name="COLORIZER_COLORS" value="@red,@yellow,@green,@blue,@cyan" />
```

```xml
<property scope="context" name="COLORIZER_COLORS" value="red@white,yellow@black,@,@,@" />
```

```xml
<property scope="context" name="COLORIZER_COLORS" value="white@magenta,boldyellow@black,green@white,boldblue@,@" />
```


Conversion rule and pattern
===========================
The final part of configuring the [`logback-colorizer`][1] is to specify a
custom conversion rule and use this in the encoder `pattern`.

Define a `conversionRule` using a custom `conversionWord` for LogColorizer.
Use this custom word in the pattern to enable colorization. Only the part
of the pattern which is within the parantheses `(...)` gets colorized, and
can be used numerous times within the same pattern.

- An example using `colorize` as the keyword to enable colorization:

```xml
<conversionRule conversionWord="colorize" converterClass="org.tuxdude.logback.extensions.LogColorizer" />
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %colorize(%msg%n)</pattern>
    </encoder>
</appender>
```

- An example using `github` as the keyword to enable colorization:

```xml
<conversionRule conversionWord="github" converterClass="org.tuxdude.logback.extensions.LogColorizer" />
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} %github([%thread]) %-5level %logger{36} - %github(%msg%n)</pattern>
    </encoder>
</appender>
```

- An example using `rainbow` as the keyword to enable colorization:

```xml
<conversionRule conversionWord="rainbow" converterClass="org.tuxdude.logback.extensions.LogColorizer" />
<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>%d{HH:mm:ss.SSS} %rainbow([%thread]) %-5level %rainbow(%logger{36}) - %rainbow(%msg%n)</pattern>
    </encoder>
</appender>
```


[1]: https://github.com/Tuxdude/logback-colorizer
[2]: https://oss.sonatype.org/content/groups/public/org/tuxdude/logback/extensions/logback-colorizer/1.0.1/logback-colorizer-1.0.1.jar
[21]: http://logback.qos.ch/
[22]: http://logback.qos.ch/manual/layouts.html#coloring
[23]: http://tony19.github.io/logback-android/index.html
