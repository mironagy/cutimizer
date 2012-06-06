dnl GLPK for Java

dnl Initialization
AC_INIT([libglpk-java], [1.0.19], [xypron.glpk@gmx.de])
AM_INIT_AUTOMAKE
LT_INIT

dnl Compiler check
AC_PROG_CC
AC_CHECK_PROG([have_cc],[$CC],[yes],[no])
if test [$have_cc] != [yes]; then
  AC_MSG_ERROR([$CC is missing])
fi

dnl Provide $(LN_S)
AC_PROG_LN_S

dnl Check for programs needed
AC_PATH_PROG([SWIG],[swig])
if test "x$SWIG" == "x"; then
  AC_MSG_ERROR([swig is missing])
fi
AC_PATH_PROG([JAVAC],[javac])
if test "x$JAVAC" == "x"; then
  AC_MSG_ERROR([javac is missing])
fi
AC_PATH_PROG([JAVADOC],[javadoc])
if test "x$JAVADOC" == "x"; then
  AC_MSG_ERROR([javadoc is missing])
fi
AC_PATH_PROG([JAR],[jar])
if test "x$JAR" == "x"; then
  AC_MSG_ERROR([jar is missing])
fi

dnl Check JAVA_HOME is set
if test "x$JAVA_HOME" == "x"; then
  AC_MSG_ERROR([JAVA_HOME is not set])
fi

dnl Include path
CPPFLAGS="-I$JAVA_HOME/include -I$JAVA_HOME/include/linux $CPPFLAGS"

dnl SWIG
AC_ARG_VAR([SWIGFLAGS],[The list of flags that should be passed to SWIG.])
SWIGFLAGS="-I/usr/include -I/usr/local/include $SWIGFLAGS"
SWIGFLAGS="-I$JAVA_HOME/include -I$JAVA_HOME/include/linux $SWIGFLAGS"


dnl Check includes
AC_CHECK_HEADER([glpk.h],
  [],
  [AC_MSG_ERROR([glpk.h not found])]
  )
AC_CHECK_HEADER([jni.h],
  [],
  [AC_MSG_ERROR([jni.h not found])]
  )

dnl Makefiles
AC_CONFIG_FILES([
  Makefile
  doc/Makefile
  swig/Makefile
  ])

dnl Generate files
AC_OUTPUT

