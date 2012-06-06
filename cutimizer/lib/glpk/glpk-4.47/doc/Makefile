SHELL = /bin/sh
# copy revision from glpk package: configure.ac
REVISION := 4.47
# package name
package = libglpk-java
# installation path for package
prefix = /usr/local

all:
	gzip -c libglpk-java.3 > libglpk-java.3.gz

clean:
	rm -f *.aux
	rm -f *.bbl
	rm -f *.blg
	rm -f *.gz
	rm -f *.idx
	rm -f *.ilg
	rm -f *.ind
	rm -f *.log
	rm -f *.out
	rm -f *.toc
distclean: clean
	rm -f *~

documentation:
	epstopdf swimlanes.eps
	pdflatex glpk-java.tex
	bibtex glpk-java
	pdflatex glpk-java.tex
	makeindex glpk-java.idx
	pdflatex glpk-java.tex

install:
	mkdir -p -m 755 $(DESTDIR)${prefix}/share/doc/${package};true
	install -m 644 glpk-java.pdf $(DESTDIR)${prefix}/share/doc/${package}/glpk-java.pdf
	mkdir -p -m 755 $(DESTDIR)${prefix}/share/man/man3/;true
	install -m 644 libglpk-java.3.gz $(DESTDIR)${prefix}/share/man/man3/libglpk-java.3.gz

check:
	if test ! -f glpk-java.pdf;then echo glpk-java.pdf not found;exit 1; fi
