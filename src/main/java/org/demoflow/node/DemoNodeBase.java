package org.demoflow.node;

import com.badlogic.gdx.utils.Array;
import nu.xom.Attribute;
import nu.xom.Element;
import org.demoflow.utils.uiutils.UiUtils;

import javax.swing.*;
import java.io.IOException;

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

    protected void addAttribute(Element element, final String name, final Number value) {
        addAttribute(element, name, value.toString());
    }

    protected void addAttribute(Element element, final String name, final String value) {
        element.addAttribute(new Attribute(name, value));
    }

    protected final boolean getBooleanAttribute(Element element, final String name, boolean defaultValue) throws IOException {
        try {
            if (hasAttribute(element, name)) return Boolean.parseBoolean(element.getAttributeValue(name));
            else return defaultValue;
        }
        catch (NumberFormatException e) {
            throw new IOException("Could not parse the boolean attribute " + name + " of xml element " + element + ": " + e.getMessage(), e);
        }
    }

    protected final int getIntAttribute(Element element, final String name, int defaultValue) throws IOException {
        try {
            if (hasAttribute(element, name)) return Integer.parseInt(element.getAttributeValue(name));
            else return defaultValue;
        }
        catch (NumberFormatException e) {
            throw new IOException("Could not parse the integer attribute " + name + " of xml element " + element + ": " + e.getMessage(), e);
        }
    }

    protected final long getLongAttribute(Element element, final String name, long defaultValue) throws IOException {
        try {
            if (hasAttribute(element, name)) return Long.parseLong(element.getAttributeValue(name));
            else return defaultValue;
        }
        catch (NumberFormatException e) {
            throw new IOException("Could not parse the long attribute " + name + " of xml element " + element + ": " + e.getMessage(), e);
        }
    }

    protected final double getDoubleAttribute(Element element, final String name, double defaultValue) throws IOException {
        try {
            if (hasAttribute(element, name)) return Double.parseDouble(element.getAttributeValue(name));
            else return defaultValue;
        }
        catch (NumberFormatException e) {
            throw new IOException("Could not parse the double attribute " + name + " of xml element " + element + ": " + e.getMessage(), e);
        }
    }

    protected final boolean hasAttribute(Element element, String name) {
        return element.getAttribute(name) != null;
    }
}
