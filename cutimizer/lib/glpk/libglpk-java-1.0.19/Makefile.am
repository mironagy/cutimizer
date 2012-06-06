SUBDIRS = doc swig

EXTRA_DIST = examples w32 w64

clean:

documentation:
	-for d in $(SUBDIRS); do (cd $$d && $(MAKE) documentation ); done

