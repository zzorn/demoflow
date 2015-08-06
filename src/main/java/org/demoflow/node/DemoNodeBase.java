package org.demoflow.node;

import com.badlogic.gdx.utils.Array;
import org.demoflow.utils.ArrayEnumeration;
import org.demoflow.utils.EmptyEnumeration;

import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for DemoNodes.
 */
public abstract class DemoNodeBase implements DemoNode {

    private transient Array<DemoNodeListener> nodeListeners = null;

    private DemoNode parent = null;
    private int maxDepth = 0;

    public DemoNodeBase() {
        this(null);
    }

    public DemoNodeBase(DemoNode parent) {
        setParent(parent);
    }

    @Override public final DemoNode getParent() {
        return parent;
    }

    @Override public final void setParent(DemoNode parent) {
        this.parent = parent;
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

    @Override public int getDepth() {
        final DemoNode parent = getParent();
        return parent == null ? 0 : parent.getDepth() + 1;
    }

    @Override public final int getMaxDepth() {
        return maxDepth;
    }

    @Override public final void updateMaxDepth() {
        int oldMaxDepth = maxDepth;
        maxDepth = 0;

        // Set maxDepth to maximum child maxDepth + 1
        final Enumeration<? extends DemoNode> childEnumerator = getChildren();
        while (childEnumerator.hasMoreElements()) {
            maxDepth = Math.max(maxDepth, childEnumerator.nextElement().getMaxDepth() + 1);
        }

        if (oldMaxDepth != maxDepth) {
            // Notify parent as well of the change so they can update
            if (parent != null) parent.updateMaxDepth();
        }
    }

    /**
     * Notify node listeners that a new child node was added to this node.
     */
    protected final void notifyChildNodeAdded(DemoNode childNode) {
        // Max depth may have changed.
        updateMaxDepth();

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
        // Max depth may have changed.
        updateMaxDepth();

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
