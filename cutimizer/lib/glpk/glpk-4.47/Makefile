SHELL = /bin/sh
NAME = `pwd | awk -f getlastfragment.awk`
all:
	cd doc; $(MAKE) all
	cd examples/java; $(MAKE) all
	cd swig; $(MAKE) all
clean:
	cd doc; $(MAKE) clean
	cd examples/java; $(MAKE) clean
	cd swig; $(MAKE) clean
dist: documentation distclean
	tar -C .. -cf ../$(NAME).tar $(NAME)
	gzip -f ../$(NAME).tar;md5sum ../$(NAME).tar.gz > ../$(NAME).tar.gz.md5
distclean:
	cd doc; $(MAKE) distclean
	cd examples/java; $(MAKE) distclean
	cd swig; $(MAKE) distclean
	rm -f *~
	find . -name .svn -exec rm {} -r -f \;;true
documentation:
	cd doc; $(MAKE) documentation
install:
	cd doc; $(MAKE) install
	cd examples/java; $(MAKE) install
	cd swig; $(MAKE) install
check:
	cd doc; $(MAKE) check
	cd examples/java; $(MAKE) check
	cd swig; $(MAKE) check
