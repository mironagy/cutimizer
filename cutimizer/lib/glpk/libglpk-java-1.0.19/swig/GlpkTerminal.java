package org.gnu.glpk;

import java.util.LinkedList;

/**
 * This class manages terminal output.
 */
public class GlpkTerminal {
    private static LinkedList<GlpkTerminalListener> listeners
            = new LinkedList<GlpkTerminalListener>();

    static {
        GLPK.glp_term_hook(null, null);
    }

    /**
     * Callback function called by native library.
     */
    public static int callback(String str) {
        boolean output = false;

        if (listeners.size() > 0) {
            for (GlpkTerminalListener listener : listeners) {
                output |= listener.output(str);
            }
            return output ? 0 : 1;
        }
        return 0;
    }

    /**
     * Add listener.
     * @param listener listener for terminal output
     */
    public static void addListener(GlpkTerminalListener listener) {
        listeners.add(listener);
    }

    /**
     * Remove listener.
     * @param listener listener for terminal output
     */
    public static void removeListener(GlpkTerminalListener listener) {
        listeners.remove(listener);
    }

    /**
     * Remove all listeneres.
     */
    public static void removeAllListeners() {
        while( listeners.size() > 0) {
            listeners.removeLast();
        }
    }
}
