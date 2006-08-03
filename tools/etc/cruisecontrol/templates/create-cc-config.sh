#!/bin/sh

java -classpath .:velocity-1.4.jar:jdom.jar:commons-collections.jar:commons-logging.jar:logkit.jar ConfigGenerator projects.xml cc-config.xml

java -classpath .:velocity-1.4.jar:jdom.jar:commons-collections.jar:commons-logging.jar:logkit.jar ConfigGenerator projects-db.xml cc-db-config.xml

java -classpath .:velocity-1.4.jar:jdom.jar:commons-collections.jar:commons-logging.jar:logkit.jar ConfigGenerator projects-web.xml cc-web-config.xml

java -classpath .:velocity-1.4.jar:jdom.jar:commons-collections.jar:commons-logging.jar:logkit.jar ConfigGenerator projects-matrix.xml cc-matrix-config.xml
