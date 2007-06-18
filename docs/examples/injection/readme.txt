Injection example

This example shows different mechanisms of injecting values. 
Including a mechanism to resolve circular dependencies.
Only properties are shown, but injections work anywhere a string value is accepted.

adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/injection> ant
Buildfile: build.xml

compile:
    [javac] Compiling 1 source file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/injection/build

dist:
     [copy] Copying 1 file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/injection/build
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/injection/dist/example-injection.beans

run:
     [java] InjectionBean() InjectionBean1
     [java] setHost: www.jboss.org on InjectionBean1
     [java] InjectionBean() InjectionBean2
     [java] setOther: InjectionBean1 on InjectionBean2
     [java] InjectionBean() Circular1
     [java] InjectionBean() Circular2
     [java] setOther: Circular1 on Circular2
     [java] setOther: Circular2 on Circular1
     [java] setOther: null on InjectionBean2
     [java] setHost: null on InjectionBean1
     [java] setOther: null on Circular1
     [java] setOther: null on Circular2

BUILD SUCCESSFUL
Total time: 6 seconds
