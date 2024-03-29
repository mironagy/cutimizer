EXTRA_DIST = *.i *.java pom.xml src/site

# copy version-info from glpk package: src/Makefile.am
VERSION_INFO = 32:0:32

all:
	mkdir -p target/classes
	mkdir -p target/apidocs
	mkdir -p src/c
	mkdir -p src/main/java/org/gnu/glpk
	cp *.java src/main/java/org/gnu/glpk
	$(SWIG) $(SWIGFLAGS) -java -package org.gnu.glpk \
	  -o src/c/glpk_wrap.c -outdir src/main/java/org/gnu/glpk glpk.i
	$(LIBTOOL) --mode=compile gcc $(SWIGFLAGS) -c -fPIC src/c/glpk_wrap.c
	$(LIBTOOL) --mode=link \
	  $(CC) -version-info $(VERSION_INFO) -revision $(PACKAGE_VERSION) \
	  -g -O -o libglpk_java.la -rpath ${prefix}/lib/jni glpk_wrap.lo -lglpk 
	$(JAVADOC) -sourcepath ./src/main/java org.gnu.glpk -d ./target/apidocs
	$(JAR) cf glpk-java-javadoc.jar -C ./target/apidocs .
	$(JAR) cf glpk-java-sources.jar -C ./src/main/java .
	$(JAVAC) -source 1.6 -target 1.6 -classpath ./src/main/java \
	  -d ./target/classes *.java 
	$(JAR) cf glpk-java.jar -C ./target/classes .

clean-local:
	rm -f -r src/main src/c target .libs
	rm -f *.jar *.o *.la *.lo ../examples/*.class target 
	rm -f *~ ../examples/java/*~ ../w32/*~ ../w64/*~

documentation:

install:
	mkdir -p -m 755 $(DESTDIR)${libdir}/jni;true
	$(LIBTOOL) --mode=install install -c libglpk_java.la \
	  $(DESTDIR)${libdir}/jni/libglpk_java.la
	$(LIBTOOL) --mode=finish $(DESTDIR)${libdir}/jni
	mkdir -p -m 755 $(DESTDIR)${datarootdir}/java;true
	install -m 644 glpk-java.jar \
	  $(DESTDIR)${datarootdir}/java/glpk-java-$(PACKAGE_VERSION).jar
	cd $(DESTDIR)${prefix}/share/java/; \
	  $(LN_S) -f glpk-java-$(PACKAGE_VERSION).jar glpk-java.jar
	mkdir -p -m 755 $(DESTDIR)${docdir};true
	install -m 644 glpk-java-javadoc.jar \
	  $(DESTDIR)${docdir}/glpk-java-javadoc-$(PACKAGE_VERSION).jar
	cd $(DESTDIR)${docdir}; \
	  $(LN_S) -f glpk-java-javadoc-$(PACKAGE_VERSION).jar \
	  glpk-java-javadoc.jar
	install -m 644 glpk-java-sources.jar \
	  $(DESTDIR)${docdir}/glpk-java-sources-$(PACKAGE_VERSION).jar
	cd $(DESTDIR)${docdir}; \
	  $(LN_S) -f glpk-java-sources-$(PACKAGE_VERSION).jar \
	  glpk-java-sources.jar

check:
	cd ../examples/java;$(JAVAC) -source 1.6 -target 1.6 \
	  -classpath ../../swig/glpk-java.jar Gmpl.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs \
	  -classpath ../../swig/glpk-java.jar:. Gmpl marbles.mod
	cd ../examples/java;$(JAVAC) -source 1.6 -target 1.6 -classpath \
	  ../../swig/glpk-java.jar Lp.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs \
	  -classpath ../../swig/glpk-java.jar:. Lp
	cd ../examples/java;$(JAVAC) -source 1.6 -target 1.6 \
	   -classpath ../../swig/glpk-java.jar ErrorDemo.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs \
	   -classpath ../../swig/glpk-java.jar:. ErrorDemo
	cd ../examples/java;$(JAVAC) -source 1.6 -target 1.6 \
	  -classpath ../../swig/glpk-java.jar LinOrd.java
	cd ../examples/java;java -Djava.library.path=../../swig/.libs \
	  -classpath ../../swig/glpk-java.jar:. LinOrd tiw56r72.mat \
	  tiw56r72.sol;rm tiw56r72.sol

dist-hook:
	rm -rf `find $(distdir) -name '*~'`
	rm -rf `find $(distdir) -name .svn`
	rm -rf `find $(distdir) -name '*.bak'`

