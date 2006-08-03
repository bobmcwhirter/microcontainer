Constructor example

This example shows different mechanisms of configuring constructors.

adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor> ant
Buildfile: build.xml

compile:
    [mkdir] Created dir: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor/build
    [javac] Compiling 1 source file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor/build

dist:
     [copy] Copying 1 file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor/build
    [mkdir] Created dir: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor/dist
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/constructor/dist/example-constructor.beans

run:
     [java] ConstructorBean(int) with 4
     [java] ConstructorBean(String, long) with a string and 10

BUILD SUCCESSFUL
Total time: 6 seconds
