Lifecycle example

This example shows the lifecycle methods create/start/stop/destroy. 

adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/lifecycle> ant
Buildfile: build.xml

compile:
    [javac] Compiling 1 source file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/lifecycle/build

dist:
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/lifecycle/dist/example-lifecycle.beans

run:
     [java] LifecycleBean() Lifecycle1
     [java] LifecycleBean() Lifecycle2
     [java] create: Lifecycle2
     [java] create: Lifecycle1
     [java] start: Lifecycle2
     [java] start: Lifecycle1
     [java] stop: Lifecycle1
     [java] destroy: Lifecycle1
     [java] stop: Lifecycle2
     [java] destroy: Lifecycle2

BUILD SUCCESSFUL
Total time: 6 seconds
