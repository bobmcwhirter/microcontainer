Locator example

This example shows two types of locator

The first is a Hashtable locator which is a singleton containing a hashtable configured via the kernel.
The second is a wrapper around the kernel controller, using the Microcontainer as a locator.

[ejort@htimes2 locator]$ ant
Buildfile: build.xml

compile:
    [mkdir] Created dir: /home/ejort/jboss-head/workspace/kernel/output/microcontainer-1.0.2/examples/locator/build
    [javac] Compiling 5 source files to /home/ejort/jboss-head/workspace/kernel/output/microcontainer-1.0.2/examples/locator/build

dist:
     [copy] Copying 1 file to /home/ejort/jboss-head/workspace/kernel/output/microcontainer-1.0.2/examples/locator/build
    [mkdir] Created dir: /home/ejort/jboss-head/workspace/kernel/output/microcontainer-1.0.2/examples/locator/dist
      [jar] Building jar: /home/ejort/jboss-head/workspace/kernel/output/microcontainer-1.0.2/examples/locator/dist/example-locator.jar

run:
     [java] =============================================================
     [java] Using locator: org.jboss.example.microcontainer.locator.HashtableLocator@8acf6e

     [java] SimpleBean1 text=Simple1
     [java] SimpleBean2 text=Simple2
     [java] =============================================================
     [java] =============================================================
     [java] Using locator: org.jboss.example.microcontainer.locator.ControllerLocator@1bc82e7

     [java] SimpleBean1 text=Simple1
     [java] SimpleBean2 text=Simple2
     [java] =============================================================

BUILD SUCCESSFUL
Total time: 10 seconds
