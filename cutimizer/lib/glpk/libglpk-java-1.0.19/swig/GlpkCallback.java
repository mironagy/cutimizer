package org.gnu.glpk;

import java.util.LinkedList;

/**
 * This class manages callbacks from the MIP solver.
 */
public class GlpkCallback {
    private static LinkedList<GlpkCallbackListener> listeners
            = new LinkedList<GlpkCallbackListener>();

    /**
     * callback function called by native library
     */
    public static void callback(long cPtr) {
        glp_tree tree;
        tree = new glp_tree(cPtr, false);
        for (GlpkCallbackListener listener : listeners) {
            listener.callback(tree);
        }
    }

    public static void addListener(GlpkCallbackListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(GlpkCallbackListener listener) {
        listeners.remove(listener);
    }

}
