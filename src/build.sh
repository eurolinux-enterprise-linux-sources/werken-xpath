#!/bin/sh

echo
echo "Building..."
echo

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

  JAVA_CP=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dev.jar
  JDOM_CP=./lib/jdom.jar
 ANTLR_CP=./lib/antlr-all.jar
XERCES_CP=./lib/xerces.jar
   ANT_CP=./lib/ant.jar

 ANT_HOME=./lib

CP=$JAVA_CP:$JDOM_CP:$ANTLR_CP:$XERCES_CP:$ANT_CP

echo Building with classpath $CP
echo

echo Starting Ant...
echo

#strace $JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $CP org.apache.tools.ant.Main $*
$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $CP org.apache.tools.ant.Main $*
