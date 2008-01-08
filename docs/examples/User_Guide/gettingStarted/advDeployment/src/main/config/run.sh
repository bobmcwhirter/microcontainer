#!/bin/sh

java -Djava.ext.dirs=`pwd`/lib -cp .:advDeployment-1.0.0.jar org.jboss.example.client.Client $1
