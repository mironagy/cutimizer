%module GLPK

/* Load the native library 
 */
%pragma(java) modulecode=%{
  static {
    try {
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        // try to load Windows library
        System.loadLibrary("glpk_4_47_java");
      } else {
        // try to load Linux library
        System.loadLibrary("glpk_java");
      }
    } catch (UnsatisfiedLinkError e) {
      System.err.println(
        "The dynamic link library for GLPK for Java could not be"
        + "loaded.\nConsider using\njava -Djava.library.path=");
      throw e;  
    }
  }
%}

/* As there is no good transformation for va_list
 * we will just do nothing.
 * cf. http://swig.org/Doc1.3/SWIGDocumentation.html#Varargs_nn8
 * This typemap is necessary to compile on amd64
 * Linux. 
 */
%typemap(in) (va_list arg) {
}

/* The function glp_term_hook is modified to preset
 * the callback function.
 */
%exception glp_term_hook  { 
  arg1 = glp_java_term_hook;
  arg2 = (void *) jenv;
  $action
}

/* The function glp_init_iocp is modified to preset
 * the callback function.
 */
%typemap(out) void glp_init_iocp {
  arg1->cb_func = glp_java_cb;
  arg1->cb_info = (void *) jenv;
}

%{
#include "glpk.h"
#include <setjmp.h>
#include <jni.h>

/*
 * Function declarations
 */
int glp_java_term_hook(void *info, const char *s);
void glp_java_error_hook(void *in);
void glp_java_error(char *message);

/*
 * Static variables to handle errors inside callbacks
 */
#define GLP_JAVA_MAX_CALLBACK_LEVEL 4
int glp_java_callback_level = 0;
int glp_java_error_occured = 0;
jmp_buf *glp_java_callback_env[GLP_JAVA_MAX_CALLBACK_LEVEL];

void glp_java_error(char *message) {
    glp_error("%s\n", message);
}

int glp_java_term_hook(void *info, const char *s) {
    jclass cls;
    jmethodID mid = NULL;
    JNIEnv *env = (JNIEnv *) info;
    jstring str = NULL;
    jint ret = 0;

    glp_java_callback_level++;
    if (glp_java_callback_level >= GLP_JAVA_MAX_CALLBACK_LEVEL) {
        glp_java_error_occured = 1;
    } else {
        glp_java_error_occured = 0;
        cls = (*env)->FindClass(env, "org/gnu/glpk/GlpkTerminal");
        if (cls != NULL) {
            mid = (*env)->GetStaticMethodID(
                env, cls, "callback", "(Ljava/lang/String;)I");
            if (mid != NULL) {
                str = (*env)->NewStringUTF(env, s);
                ret = (*env)->CallStaticIntMethod(env, cls, mid, str);
                if (str != NULL) {
                    (*env)->DeleteLocalRef( env, str );
                }
            }
            (*env)->DeleteLocalRef( env, cls );
        }
    }
    glp_java_callback_level--;
    if (glp_java_error_occured) {
       longjmp(*glp_java_callback_env[glp_java_callback_level], 1);
    }
    return ret;
}

/**
 * Call back function for MIP solver
 */
void glp_java_cb(glp_tree *tree, void *info) {
    jclass cls;
    jmethodID mid = NULL;
    JNIEnv *env = (JNIEnv *) info;
    jlong ltree;

    glp_java_callback_level++;
    if (glp_java_callback_level >= GLP_JAVA_MAX_CALLBACK_LEVEL) {
        glp_java_error_occured = 1;
    } else {
        glp_java_error_occured = 0;
        cls = (*env)->FindClass(env, "org/gnu/glpk/GlpkCallback");
        if (cls != NULL) {
            mid = (*env)->GetStaticMethodID(
                env, cls, "callback", "(J)V");
        }
        if (mid != NULL) {
            *(glp_tree **)&ltree = tree;
            (*env)->CallStaticVoidMethod(env, cls, mid, ltree);
        }
        if (cls != NULL) {
            (*env)->DeleteLocalRef( env, cls );
        }
    }
    glp_java_callback_level--;
    if (glp_java_error_occured) {
       longjmp(*glp_java_callback_env[glp_java_callback_level], 1);
    }
}

/**
 * This hook function will be processed if an error occured
 * calling the glpk library
 * @param in pointer to long jump environment
 */
void glp_java_error_hook(void *in) {
    glp_java_error_occured = 1;
    /* free GLPK memory */
    glp_free_env();
    /* safely return */
    longjmp(*((jmp_buf*)in), 1);
}

/**
 * This function is used to throw a Java exception
 * @param env Java environment
 * @param message detail message
 */
void glp_java_throw(JNIEnv *env, char *message) {
    jclass newExcCls;
    newExcCls = (*env)->FindClass(env, "org/gnu/glpk/GlpkException");
    if (newExcCls == NULL) {
        newExcCls = (*env)->FindClass(env, "java/lang/IllegalArgumentException");
    }
    if (newExcCls != NULL) {
        (*env)->ThrowNew(env, newExcCls, message);
    }
}
%}
// Exception handling
%exception {
    jmp_buf glp_java_env;
    
    glp_java_callback_env[glp_java_callback_level] = &glp_java_env;
    if (setjmp(glp_java_env)) {
        glp_java_throw(jenv, "function $name failed");
    } else {
        glp_error_hook(glp_java_error_hook, &glp_java_env);
        $action;
    }
    glp_java_callback_env[glp_java_callback_level] = NULL;
    glp_error_hook(NULL, NULL);
}

// Add handling for arrays
%include "carrays.i"
%array_functions(int, intArray)
%array_functions(double, doubleArray)

// Add handling for String arrays in glp_main
%include "various.i"
%apply char **STRING_ARRAY { const char *argv[] };

// Add the library to be wrapped
%include "glpk.h"
void glp_java_error(char *message);
