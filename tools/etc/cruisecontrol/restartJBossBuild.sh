#!/bin/bash

#echo kill old cruisecontrol jobs

#killall -9 java
#killall -9 cvs


#echo sleep a bit before we start again
#sleep 30
rm nohup.out
nohup ../cruisecontrol/main/bin/cruisecontrol.sh -configfile scripts/cc-config.xml -projectname jboss-head -port 8090 &