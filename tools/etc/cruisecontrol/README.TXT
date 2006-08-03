Hello,

These notes describe this JBoss build and test configuration - which uses cruisecontrol. As usual, they are a work in progress...

The basic idea is that cruisecontrol is used to monitor CVS, it then kicks off a build when a change is detected.
We use one cruisecontrol process to monitor (currently) 3 versions of JBoss (HEAD, 3.2 and 3.0).
Since there is a lot in common between each project - I use a couple of scripts to populate a template and
generate the cruisecontrol config file.

Cruisecontrol monitors the directory called checkout/<jboss version> for changes in CVS.  When a change is detected,
the ant script  build-jboss-head.xml is run.  This script then calls a bash shell script to do a full checkout
(to ensure we get any new cvs module bits) and compile the relevant jboss version.

IDEAS

Runnings Tests
Which tests
- minimal smoke tests - after each build
- unit test suite - once a day
- full test suite  - once a week
- must not do more than one thing at once - could invalidate tests - eg performance, ports used etc.

Options to trigger tests
- get cc to control when to do it...
- cc/modification set with requiresmod=false and time based build...
- cc/at end of every build
- cc/use multiple to run tests every X builds
- cc/use a pausebuilder in cc and run the tests externally
- cc/use executepublisher to trigger testing asynchronously
- use crontab to kill/start cc and run tests
- ?

TODO
- complete these notes  ;-)
- make the build more robust
-- currently, due to cc problems it is periodically restarted - but this could happen mid-build leaving it in a dodgy state
-- so perhaps best to begin each start of cruisecontrol with a fresh checkout - with a little refactoring the existing build script can do this
- add some smoke tests - probably based on a subset of the current unit tests
- compress the logs produced - to make them more accessible over the web


REQUIREMENTS
- cruisecontrol - something after version 2.1.5 (as we need the fix re: monitoring branches)
- java
- bash (eg Linux or Cygwin on Windows)
- mailhost - used to send out results


INSTALL

build loop
==================
Generate the cruisecontrol config file:
- to the <root directory>/scripts directory
- review the generate.conf and build.conf files to ensure appropriate settings
- run the gen_configs scripts   "bash gen_configs.sh" - which merges the config template with the version configs
to get the cruisecontrol config file.  The file build.conf and generate.conf files are used to configure bits of the
build, such as which cvs user to use, mail server, web server etc.

Assuming you have cruisecontrol installed (see http://sf.net/projects/cruisecontrol if not)

In the root directory type:
   nohup cruisecontrol.sh -configfile scripts/cc-config-all.xml &

reporting website
==================


LINKS

cruisecontrol - http://sf.net/projects/cruisecontrol


cruisecontrol quick start
==========================

- download it - http://cruisecontrol.sourceforge.net/download.html
- unzip the download into some directory.
- run build.bat/sh in main directory to build the cruisecontrol jar
- then you need to run it using the bin/cruisecontrol.bat/sh script - with a project specific config file

You also need to build the related webapp - its in the reporting/jsp directory, you will need to do some mods:
- create an override.properties file in the reporting/jsp sub-directory
- move aside the reporting/xsl directory and instead use the xsl directory in this cvs - this version includes version
  info in the output [probably should try and get cc cvs updated for this...] 
- run 'sh build.sh war' in the same directory
- unwar - so that you can then point tomcat at it
- may get issues due to xalan jar (eg errors or incomplete pages) - if so, move the one in dist out of way
(and copy latest from xalan website into tomcat common/endorsed directory - but the jdk one maybe sufficient)