package org.gnu.glpk;   

/**
 * Exception thrown, when the GLPK native library call fails
 */
public class GlpkException extends RuntimeException {
    private static final long serialVersionUID = -7806261799440623428L;

    /** 
     * Constructs a new GLPK exception.
     */
    public GlpkException() {
        super();
    }
    
    /** 
     * Constructs a new GLPK exception.
     * @param message detail message
     */
    public GlpkException(String message) {
        super(message);
    }
}

