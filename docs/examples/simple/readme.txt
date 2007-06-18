Simple example

This example is just an "Hello World"
to validate it is actually working.

It simply bootstraps, then creates one bean with the constructor doing a System.out.println()

adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple> ant
Buildfile: build.xml

compile:
    [mkdir] Created dir: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple/build
    [javac] Compiling 1 source file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple/build

dist:
     [copy] Copying 1 file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple/build
    [mkdir] Created dir: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple/dist
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/simple/dist/example-simple.beans

run:
     [java] SimpleBean() constructor

BUILD SUCCESSFUL
Total time: 6 seconds