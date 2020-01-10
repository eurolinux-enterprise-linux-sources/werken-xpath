#!/bin/sh

TESTS_XML=./test/data/abbr_tests.xml
TEST_DRIVER=com.werken.xpath.test.Driver

ANTLR_CP=./lib/antlr-runtime.jar
JDOM_CP=./lib/jdom.jar
XERCES_CP=./lib/xerces.jar
XPATH_CP=./build/werken.xpath.jar
TEST_CP=./build/test/classes/

CP=$ANTLR_CP:$JDOM_CP:$XPATH_CP:$TEST_CP:$XERCES_CP

java -classpath $CP $TEST_DRIVER $TESTS_XML
