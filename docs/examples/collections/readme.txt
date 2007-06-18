Collections example

This example shows different mechanisms of configuring collections.
adrian@adrian-srv01:~/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/collections> ant
Buildfile: build.xml

compile:
    [javac] Compiling 1 source file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/collections/build

dist:
     [copy] Copying 1 file to /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/collections/build
      [jar] Building jar: /home/adrian/jboss-head/workspace/kernel/output/microcontainer-1.0.0/examples/collections/dist/example-collections.beans

run:
     [java] Collection type=java.util.ArrayList
     [java] value='Value of type elementClass' type=java.lang.String
     [java] value='4' type=java.lang.Integer

     [java] List type=java.util.ArrayList
     [java] value='Value of type elementClass' type=java.lang.String
     [java] value='4' type=java.lang.Integer

     [java] LinkedList type=java.util.LinkedList
     [java] value='Value of type elementClass' type=java.lang.String
     [java] value='4' type=java.lang.Integer

     [java] Set type=java.util.HashSet
     [java] value='4' type=java.lang.Integer
     [java] value='Value of type elementClass' type=java.lang.String

     [java] Map type=java.util.HashMap
     [java] key='4' type=java.lang.Long
     [java] value='Value of type valueClass' type=java.lang.String
     [java] key='Key2 of type keyClass' type=java.lang.String
     [java] value='4' type=java.lang.Integer
     [java] key='Key1 of type keyClass' type=java.lang.String
     [java] value='Value1 of type valueClass' type=java.lang.String

     [java] Hashtable type=java.util.Hashtable
     [java] key='4' type=java.lang.Long
     [java] value='Value of type valueClass' type=java.lang.String
     [java] key='Key1 of type keyClass' type=java.lang.String
     [java] value='Value1 of type valueClass' type=java.lang.String
     [java] key='Key2 of type keyClass' type=java.lang.String
     [java] value='4' type=java.lang.Integer


BUILD SUCCESSFUL
Total time: 6 seconds
