rem Build GLPK JNI DLL with Microsoft Visual Studio Express 2010
rem NOTE: Make sure that the following variables specify correct paths:
rem HOME, SWIG, JAVA_HOME

set HOME="C:\Program Files (x86)\Microsoft Visual Studio 10.0\VC"
set SWIG="C:\Program Files (x86)\swig\swigwin-2.0.4"
set SDK="C:\Program Files\Microsoft SDKs\Windows\v7.1"

set path_build_jni=%path%
cd ..\swig
mkdir target\classes
mkdir target\apidocs
mkdir src\main\java\org\gnu\glpk
mkdir src\c
copy *.java src\main\java\org\gnu\glpk
%SWIG%\swig.exe -I..\src -java -package org.gnu.glpk -o src/c/glpk_wrap.c -outdir src/main/java/org/gnu/glpk glpk.i
"%JAVA_HOME%\bin\javadoc.exe" -sourcepath ./src/main/java org.gnu.glpk -d ./target/apidocs
"%JAVA_HOME%\bin\jar.exe" cf glpk-java-javadoc.jar -C ./target/apidocs .
"%JAVA_HOME%\bin\jar.exe" cf glpk-java-sources.jar -C ./src/main/java .
cd src\main\java
dir /b /s *.java > ..\..\..\sources.txt
"%JAVA_HOME%\bin\javac.exe" -d ../../../target/classes @..\..\..\sources.txt
cd ..\..\..
"%JAVA_HOME%\bin\jar.exe" cf glpk-java.jar -C ./target/classes .
cd "%~dp0"
set INCLUDE=
set LIB=
call %HOME%\vcvarsall.bat x64
call %SDK%\bin\rc.exe glpk_java_dll.rc
set INCLUDE=%INCLUDE%;%JAVA_HOME%\include;%JAVA_HOME%\include\win32
%HOME%\bin\nmake.exe /f Makefile_JNI_VC_DLL
copy ..\swig\*.jar .
%HOME%\bin\nmake.exe /f Makefile_JNI_VC_DLL check
path %path_build_jni%
set INCLUDE=
set LIB=
pause
