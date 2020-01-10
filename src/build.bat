@echo off

echo Werken.XPath Build System
echo -------------------------


if "%JAVA_HOME%" == "" goto error
	
set JAVA_CP=%JAVA_HOME%/lib/tools.jar;%JAVA_HOME%/lib/dev.jar
set JDOM_CP=./lib/jdom.jar
set ANTLR_CP=./lib/antlr-all.jar
set XERCES_CP=./lib/xerces.jar
set ANT_CP=./lib/ant.jar

set ANT_HOME=./lib

set CP=%JAVA_CP%;%JDOM_CP%;%ANTLR_CP%;%XERCES_CP%;%ANT_CP%

echo Building with classpath %CP%
echo

echo Starting Ant...
echo

%JAVA_HOME%\bin\java.exe -Dant.home="%ANT_HOME%" -classpath %CP% org.apache.tools.ant.Main %1 %2 %3 %4 %5

goto end

:error
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."

:end