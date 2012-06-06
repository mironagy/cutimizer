SHELL = /bin/sh
# installation path of the include files
SWIG_FLAGS := -I$(JAVA_HOME)/include -I/usr/local/include -I/usr/include
# copy version-info from glpk package: src/Makefile.am
VERSION_INFO := 32:0:32
# copy revision from glpk package: configure.ac
REVISION := 4.47
# package name
package = libglpk-java
# installation path for package
prefix = /usr/local

all:
	echo $(JAVA_HOME)
	mkdir -p target/classes
	mkdir -p target/apidocs
	mkdir -p src/c
	mkdir -p src/main/java/org/gnu/glpk
	cp *.java src/main/java/org/gnu/glpk
	swig $(SWIG_FLAGS) -java -package org.gnu.glpk -o src/c/glpk_wrap.c \
        -outdir src/main/java/org/gnu/glpk glpk.i 
	libtool --mode=compile gcc $(SWIG_FLAGS) -c -fPIC src/c/glpk_wrap.c
	libtool --mode=link \
		gcc -version-info $(VERSION_INFO) -revision $(REVISION) \
		-g -O -o libglpk_java.la -rpath ${prefix}/lib/jni glpk_wrap.lo -lglpk 
	javadoc -sourcepath ./src/main/java org.gnu.glpk -d ./target/apidocs
	jar cf glpk-java-javadoc.jar -C ./target/apidocs .
	jar cf glpk-java-sources.jar -C ./src/main/java .
	javac -source 1.6 -target 1.6 -classpath ./src/main/java \
        -d ./target/classes *.java 
	jar cf glpk-java.jar -C ./target/classes .

clean:
	rm -f -r src/main src/c target .libs
	rm -f *.jar *.o *.la *.lo ../examples/*.class target 

distclean: clean
	rm -f *~ ../examples/java/*~ ../w32/*~ ../w64/*~

documentation:

install:
	mkdir -p -m 755 $(DESTDIR)${prefix}/lib/jni;true
	libtool --mode=install install -c libglpk_java.la $(DESTDIR)${prefix}/lib/jni/libglpk_java.la
	libtool --mode=finish $(DESTDIR)${prefix}/lib/jni
	mkdir -p -m 755 $(DESTDIR)${prefix}/share/java;true
	install -m 644 glpk-java.jar $(DESTDIR)${prefix}/share/java/glpk-java-$(REVISION).jar
	cd $(DESTDIR)${prefix}/share/java/;ln -f -s glpk-java-$(REVISION).jar glpk-java.jar
	mkdir -p -m 755 $(DESTDIR)${prefix}/share/doc/${package};true
	install -m 644 glpk-java-javadoc.jar $(DESTDIR)${prefix}/share/doc/${package}/glpk-java-javadoc-$(REVISION).jar
	cd $(DESTDIR)${prefix}/share/doc/${package};ln -f -s glpk-java-javadoc-$(REVISION).jar glpk-java-javadoc.jar
	install -m 644 glpk-java-sources.jar $(DESTDIR)${prefix}/share/doc/${package}/glpk-java-sources-$(REVISION).jar
	cd $(DESTDIR)${prefix}/share/doc/${package};ln -f -s glpk-java-sources-$(REVISION).jar glpk-java-sources.jar

check:
	cd ../examples/java;javac -source 1.6 -target 1.6 -classpath ../../swig/glpk-java.jar Gmpl.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs -classpath ../../swig/glpk-java.jar:. Gmpl marbles.mod
	cd ../examples/java;javac -source 1.6 -target 1.6 -classpath ../../swig/glpk-java.jar Lp.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs -classpath ../../swig/glpk-java.jar:. Lp
	cd ../examples/java;javac -source 1.6 -target 1.6 -classpath ../../swig/glpk-java.jar ErrorDemo.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs -classpath ../../swig/glpk-java.jar:. ErrorDemo
	cd ../examples/java;javac -source 1.6 -target 1.6 -classpath ../../swig/glpk-java.jar LinOrd.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs -classpath ../../swig/glpk-java.jar:. LinOrd tiw56r72.mat tiw56r72.sol;rm tiw56r72.sol
