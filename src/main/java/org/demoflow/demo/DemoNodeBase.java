package org.demoflow.demo;

import com.badlogic.gdx.utils.Array;
import org.demoflow.utils.ArrayEnumeration;

import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for DemoNodes.
 */
public abstract class DemoNodeBase implements DemoNode {

    private transient Array<DemoNodeListener> nodeListeners = null;

    @Override public int getChildCount() {
        final Array<? extends DemoNode> childArray = getChildArray();
        return childArray == null ? 0 : childArray.size;
    }

    @Override public Enumeration<? extends DemoNode> getChildren() {
        return new ArrayEnumeration<>(getChildArray());
    }

    /**
     * Can be implemented instead of the getChildCount and getChildren methods.
     * @return array with children, or null if no children.
     */
    protected Array<? extends DemoNode> getChildArray() {
        return null;
    }

    @Override public final void addNodeListener(DemoNodeListener nodeListener) {
        notNull(nodeListener, "nodeListener");

        if (nodeListeners == null) nodeListeners = new Array<>(4);

        nodeListeners.add(nodeListener);
    }

    @Override public final void removeNodeListener(DemoNodeListener nodeListener) {
        if (nodeListener != null && nodeListeners != null) {
            nodeListeners.removeValue(nodeListener, true);
        }
    }

    @Override public boolean allowsChildNodes() {
        return true;
    }

    /**
     * Notify node listeners that a new child node was added to this node.
     */
    protected final void notifyChildNodeAdded(DemoNode childNode) {
        if (nodeListeners != null) {
            for (int i = 0; i < nodeListeners.size; i++) {
                nodeListeners.get(i).onChildAdded(this, childNode);
            }
        }
    }

    /**
     * Notify node listeners that a child node was removed from this node.
     */
    protected final void notifyChildNodeRemoved(DemoNode childNode) {
        if (nodeListeners != null) {
            for (int i = 0; i < nodeListeners.size; i++) {
                nodeListeners.get(i).onChildRemoved(this, childNode);
            }
        }
    }

    /**
     * Notify NodeListeners that this node has changed, and should be redrawn in UIs and such.
     */
    protected final void notifyNodeUpdated() {
        if (nodeListeners != null) {
            for (int i = 0; i < nodeListeners.size; i++) {
                nodeListeners.get(i).onNodeUpdated(this);
            }
        }
    }

    @Override public String toString() {
        return getClass().getSimpleName();
    }

}
