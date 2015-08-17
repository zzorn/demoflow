package org.demoflow.node;

import com.badlogic.gdx.utils.Array;
import org.demoflow.utils.ArrayEnumeration;
import org.demoflow.utils.EmptyEnumeration;
import org.demoflow.utils.UiUtils;

import javax.swing.*;
import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for DemoNodes.
 */
public abstract class DemoNodeBase implements DemoNode {

    private transient Array<DemoNodeListener> nodeListeners = null;

    private DemoNode parent = null;
    private int maxDepth = 0;

    private ImageIcon cachedIcon;

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
        for (DemoNode childNode : getChildren()) {
            maxDepth = Math.max(maxDepth, childNode.getMaxDepth() + 1);
        }

        if (oldMaxDepth != maxDepth) {
            // Notify parent as well of the change so they can update
            if (parent != null) parent.updateMaxDepth();

            // Notify listeners
            notifyMaxDepthChanged(maxDepth);
        }
    }

    @Override public DemoNode getRootNode() {
        if (parent == null) return this;
        else return (parent.getRootNode());
    }

    @Override public int getTotalNumberOfDescendants() {
        if (getChildCount() <= 0) return 0;
        else {
            int sum = 0;
            final Array<? extends DemoNode> children = getChildren();
            for (int i = 0; i < children.size; i++) {
                sum += children.get(i).getTotalNumberOfDescendants() + 1;
            }
            return sum;
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

    /**
     * Notify NodeListeners that the max depth of this node changed.
     */
    protected final void notifyMaxDepthChanged(int newMaxDepth) {
        if (nodeListeners != null) {
            for (int i = 0; i < nodeListeners.size; i++) {
                nodeListeners.get(i).onNodeMaxDepthChanged(this, newMaxDepth);
            }
        }
    }

    @Override public String toString() {
        return getName();
    }

    /**
     * @return filename of icon to use for this node, or null if no icon (or if getIcon is implemented separately).
     */
    protected String getIconPath() {
        return null;
    }

    @Override public Icon getIcon() {
        final String iconPath = getIconPath();
        if (iconPath == null) return null;

        if (cachedIcon == null) {
            cachedIcon = UiUtils.loadIcon(iconPath);
        }

        return cachedIcon;
    }
}
