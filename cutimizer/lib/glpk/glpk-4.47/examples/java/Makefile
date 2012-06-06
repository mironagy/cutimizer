SHELL = /bin/sh
# package name
package = libglpk-java
# installation path for package
prefix = /usr/local
all:

clean:
	rm -f *.class

distclean: clean
	rm -f *~

install:
	mkdir -p -m 755 $(DESTDIR)${prefix}/share/doc/${package}/examples;true
	install -m 644 ErrorDemo.java  $(DESTDIR)${prefix}/share/doc/${package}/examples/ErrorDemo.java
	install -m 644 Gmpl.java       $(DESTDIR)${prefix}/share/doc/${package}/examples/Gmpl.java
	install -m 644 GmplSwing.java  $(DESTDIR)${prefix}/share/doc/${package}/examples/GmplSwing.java
	install -m 644 LinOrd.java     $(DESTDIR)${prefix}/share/doc/${package}/examples/LinOrd.java
	install -m 644 Lp.java         $(DESTDIR)${prefix}/share/doc/${package}/examples/Lp.java
	install -m 644 Mip.java        $(DESTDIR)${prefix}/share/doc/${package}/examples/Mip.java
	install -m 644 application.png $(DESTDIR)${prefix}/share/doc/${package}/examples/application.png
	install -m 644 marbles.mod     $(DESTDIR)${prefix}/share/doc/${package}/examples/marbles.mod
	install -m 644 tiw56r72.mat    $(DESTDIR)${prefix}/share/doc/${package}/examples/tiw56r72.mat

check:
