# jHiccup
----------------------------------------------------------------------------

Written by Gil Tene of Azul Systems, and released to the public domain
as explained at http://creativecommons.org/publicdomain/zero/1.0

Extended by ImproveDigital to add statsd support.

---

Version: 2.0.8
--------------

jHiccup is a non-intrusive instrumentation tool that logs and records
platform "hiccups" - including the JVM stalls that often happen when
Java applications are executed and/or any OS or hardware platform noise
that may cause the running application to not be continuously runnable.

jHiccup can be executed in one of three main ways:

1. It can be run as a Java agent (using: `java -javaagent:jHiccup.jar`)

2. It can be injected into a running application (using: `jHiccup -p <pid>`)

3. It can also be run using a convenient wrapper command for your
   existing Java application (using: `jHiccup java myProg ...`)

----------------------------------------------------------------------------

### Example jHiccup plot 
![example plot]
 
----------------------------------------------------------------------------
# Using jHiccup as a Java agent:

jHiccup is most often used as a Java agent. This is useful for platforms and
environments where a Java agent is simpler to integrate into launch scripts,
or in environments where using the bash jHiccup wrapper script is not practical
(e.g. Windows, and environments where java is not directly launched from
the command line).

jHiccup.jar can be used as a Java agent using the following launch syntax:

    % java -javaagent:jHiccup.jar MyProgram

or

    % java -javaagent:jHiccup.jar="<options>" MyProgram.jar -a -b -c

You can find the available options for the Java agent mode by running:

    % java -javaagent:jHiccup.jar="-h"

Here is a Java agent usage example with explicit parameters:

    % java -javaagent:jHiccup.jar="-d 0 -i 1000 -l hiccuplog -c" MyProgram.jar -a -b -c

This example will record hiccups experienced during the running of `MyProgram.jar`
in log file `hiccuplog`, while at the same time recording the hiccups experienced by
a control process running in a separate JVM in the log file `c.hiccuplog`.
Measurement will start immediately upon startups (rather than the 30 second
default delay), and interval data will be records every 1 second (rather than the
default 5 seconds).

Useful Java agent related notes:

Note 1: When used as a java agent, jHiccup will treat spaces, commas, and
semicolons as delimiting characters (`[ ,;]+`). For example, the option string
`-d 0 -i 1000` is equivalent to the option string `-d,0,-i,1000`. This is
useful for environments where placing space delimiters into quoted strings
is difficult or confusing.

Note 2: I find that a common way people add jHiccup as a Java agent is by using
the `_JAVA_OPTIONS` environment variable. This often allows one to add the jHiccup
Java agent without significant launch script surgery. For example:

    export _JAVA_OPTIONS='-javaagent:/path/to/jHiccup/target/jHiccup.jar="-d 20000 -i 1000"'

----------------------------------------------------------------------------

# Reading and processing the jHiccup log with jHiccupLogProcessor:

jHiccup logs hiccup information in a histogram log (see [HdrHistogram.org]
(http://hdrhistogram.org/)). This histogram log contains a full, high fidelity
histogram of all collected results in each interval, in a highly compressed
form (typically using only ~200-400 bytes per interval). However, other than
the timestamp and maximum hiccup magnitude found in the given interval, the
rest of the log line for each interval is not human readable (it is a base64
encoding of a compressed HdrHistogram).

To translate the jHiccup log file to a more human-readable form (identical to
the form that jHiccup versions before jHiccup 2.0 had used), the jHiccupLogProcessor
utility is provided. In it's simplest form, this utility can be used as such

    % jHiccupLogProcessor -i mylog.hlog -o mylog

Which will produce log files `mylog` and `mylog.hgrm` containing a human readable
interval log (with selcted percentiles in each interval), as well as a human
readable histogram percentile distribution log.

jHiccupLogProcessor can also be used to produce log files for an arbitrary
section of the jHiccup log, by using the optional `-start` and `-end` parameters.

See `jHiccupLogProcessor -h` for more details.

----------------------------------------------------------------------------

# Launching jHiccup by attaching it to existing, running application:

The jHiccup agent can be injected into a live, running Java application
if the environment supports the java attach API (which is typically available
in java environments running Java SE 6 or later).

$ jHiccup -p <pid>

NOTE: In order to attach to a running java application, the running
application needs to have `${JAVA_HOME}/lib/tools.jar` in it's classpath.
While this is commonly the case already for many IDE and desktop environments,
and for environments that involve or enable other attachable agents (such as
profilers), you may find that it is not included in your application's
classpath, and that it needs to be added if attaching jHiccup at runtime
is needed (launching jHiccup as a Java agent per the below may be a good
alternative).

----------------------------------------------------------------------------

# Running jHiccup using the Wrapper Script form:

In the wrapper script form, all it takes is adding the word "jHiccup" in
front of whatever the java invocation command line is.

For example, if your program were normally executed as:

    java <Java args> MyProgram -a -b -c

The launch line would become:

    jHiccup java <Java args> MyProgram -a -b -c

or, for a program launched with:

    /usr/bin/java <Java args> -jar MyProgram.jar -a -b -c

The launch line would become:

    jHiccup /usr/bin/java <Java args> -jar MyProgram.jar -a -b -c

or, to override the defaults by making the recording start delay 60 seconds
and log to hlog, it would become:

    jHiccup -d 60000 -l hlog /usr/bin/java <Java args> -jar MyProgram.jar -a -b -c

The jar file also includes a simple "Idle" class to facilitate sanity checks
without an external program. Here is a simple sanity test example: jHiccup
with a 4 sec delay on recording start, wrapping an Idle run that does nothing
for 30 seconds and exits:

    % jHiccup -d 4000 /usr/bin/java org.jhiccup.Idle -t 30000

[Run `jHiccup -h`, or see comment in jHiccup script for more details.]

----------------------------------------------------------------------------

# Hiccup Charts: Plotting jHiccup results

A `jHiccupPlotter.xls` Excel spreadsheet is included to conveniently
plot jHiccup log files produced by jHiccupLogProcessor in "Hiccup Chart"form.
To use the spreadsheet, load it into Excel, (make sure to enable macros),
and follow the 2-step instructions in the main menu worksheet to automatically
import the log files and produce the Hiccup Chart.

Note that `jHiccupPlotter.xls` reads the log files produced by jHiccupLogProcessor,
(the interval log and the .hgrm histogram percentile distribution log), and
not the .hlog log format that jHiccup outputs directly.

----------------------------------------------------------------------------

# Supported/Tested platforms:

The jHiccup command is expected to work and has been tested on the following
platforms:
- Various Linux flavors (Tested on RHEL/CentOS 5.x and 6.x)
- Mac OS X (tested on Lion, 10.7)
- Windows with a Cygwin environment installed (tested on Windows 7)
- Solaris (tested on both SPARC and x86)

jHiccup.jar is expected to work as a java agent and has been tested on the
following platforms:
- Various Linux flavors (Tested on RHEL/CentOS 5.x and 6.x)
- Mac OS X (tested on Lion, 10.7)
- Windows standard command shell (tested on Windows 7)
- Solaris (tested on both SPARC and x86)

If you use jHiccup on other operating systems and setups, please report back
on your experience so that we can expand the list.

----------------------------------------------------------------------------

# Using a control process to concurrently record baseline idle load hiccups:

It is often useful to compare the hiccup behavior experienced by a running
application with a "control" hiccup level of an idle workload, running on
the same system and at the same time as the observed application. To make
such control measurement convenient, jHiccup supports a `-c` option that will
launch a concurrently executing "control process" and will separately log
hiccup information of an idle workload running on a separate jvm for the
duration of the instrumented application run. When selected, the control
process log file name will match those used for the launching application,
followed with a `.c`.

For example:

    % jHiccup -l mylog -c /usr/bin/java <Java args> -jar MyProgram.jar -a -b -c

Will produce log file `mylog` detailing the hiccup behavior during the
execution of `MyProgram.jar`, as well as a log file `c.mylog` detailing
the hiccup behavior of an idle workload running on a separate jvm at
the same time.

----------------------------------------------------------------------------

# Log file name recognizes and fills in %pid and %date terms

When a log file name is specified with the `-l` option, the terms `%pid`
and `%date` will be filled in with the appropriate information. The default
log file name setting is simply `hiccup.%date.%pid`.

----------------------------------------------------------------------------

# Using jHiccup to process latency log files:

jHiccup's HiccupMeter class supports a mode `-f` that will take latency
input from a file instead of recording it. This is useful for producing
jHiccup-style text and graphical output for recorded latency data collected
by some other means.

When provided to the `-f` option, an input file is expected to contain two
white-space delimited values per line (in either integer or real number format),
representing a time stamp and a measured latency, both in millisecond units.

It's important to note that the default "expected interval between samples"
resolution in jHiccup and HiccupMeter is 1 millisecond. When processing
input files, it is imperative that an appropriate value be supplied to
the `-r` option, and that this value correctly represent the expected interval
between samples in the provided input file. HiccupMeter will use this
parameter to determine whether additional, artificial values should be added
to the histogram recording, between input samples that are farther apart in
time than the expected interval specified to the `-r` option. This behavior
corrects for "coordinated omission" situations (where long response times
lead to "skipped" requests that would have typically correlated with "bad"
response times). A "large" value (e.g. `-r 100000`) can easily be specified
to avoid any correction of this situation.

----------------------------------------------------------------------------

# Enabling (DataDog) statsd metric reporting

It also possible to enable statsd reporting via the `-statsd host[:port]`
option. When specified in each reporting interval values of the current histogram
at 99.9%, 99.99%, 99.999% and 100% are sent to the statsd daemon listening
on `host[:port]`. The port is optional. If omitted the default 8125
is used. The metrics are sent as statsd gauges with nanosecond resolution.
The keys used are the following:

- jhiccup.percentiles.99_9
- jhiccup.percentiles.99_99
- jhiccup.percentiles.99_999
- jhiccup.percentiles.100

You can also disable logging histograms into file once values are sent
to statsd by the `-nohistolog` option. Note that all other logs are still
logged into file.

For example you can enable jHiccup metrics of the host for an indefinite
 time with the following command.

    % java -jar jHiccup.jar -i 60000 -statsd localhost -nohistolog

This will send metric every minute. If used on multiple hosts this
 will generate a plot like this:

![statsd example]

Under the hood the code uses the `java-dogstatd-client` that can be
found on GitHub at https://github.com/DataDog/java-dogstatsd-client

----------------------------------------------------------------------------

# Example: adding jHiccup to Tomcat runs:

In Tomcat's `catalina.sh` script, replace the following line:

    exec "$_RUNJAVA" "$LOGGING_CONFIG" $JAVA_OPTS $CATALINA_OPTS

with:

    exec "$JHICCUP_HOME/jHiccup" "$_RUNJAVA" "$LOGGING_CONFIG" $JAVA_OPTS $CATALINA_OPTS

----------------------------------------------------------------------------

# Note: Use of HdrHistogram.

jHiccup depends on and makes systemic use of HdrHistogram to collected and
report on the statistical distribution of hiccups. This package includes an
`HdrHistogram.jar` jar file to support this functionality. HdrHistogram sources,
documentation, and a ready to use jar file can all be found on GitHub, at
http://giltene.github.com/HdrHistogram

----------------------------------------------------------------------------

# Building jHiccup:

jHiccup can be (re)built from source files using Maven:

    % mvn package

[example plot]:./examplePlot.png "Example jHiccup plot"
[statsd example]:./statsdDatadogExample.png "Example DataDog plot"
