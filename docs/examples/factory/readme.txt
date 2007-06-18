Factory example

This example shows different mechanisms of using factories to create beans.

adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/factory> ant
Buildfile: build.xml

compile:
    [javac] Compiling 3 source files to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/factory/build

dist:
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/factory/dist/example-factory.jar

run:
     [java] FactoryCreatedBean() from StaticFactory.createBean()
     [java] SingletonFactory()
     [java] FactoryCreatedBean() from SingletonFactory.createBean()

BUILD SUCCESSFUL
Total time: 6 seconds
