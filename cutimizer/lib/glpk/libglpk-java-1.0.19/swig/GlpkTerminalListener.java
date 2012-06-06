package org.gnu.glpk;

/**
 * Terminal Listener
 */
public interface GlpkTerminalListener {
    /**
     * receive terminal output
     * @param str output string
     * @return true if terminal output is requested
     */
    public boolean output(String str);
}
