package org.demoflow.node;

import com.badlogic.gdx.utils.Array;

import java.util.Enumeration;

/**
 * A node in the demo tree structure.
 * Used to make navigating the demo in the UI easier.
 */
public interface DemoNode {

    /**
     * @return the parent of this node, or null if this node has no parent (e.g. a Demo).
     */
    DemoNode getParent();

    /**
     * Update the parent of this DemoNode.
     */
    void setParent(DemoNode parent);

    /**
     * @return some kind of user readable name for the node.
     */
    String getName();

    /**
     * @return current number of child nodes.
     */
    int getChildCount();

    /**
     * @return child nodes, or an empty array if this node has no children.
     */
    Array<? extends DemoNode> getChildren();

    /**
     * @param nodeListener listener that is notified about added and removed children, and general UI updates.
     */
    void addNodeListener(DemoNodeListener nodeListener);

    /**
     * @param nodeListener DemoNodeListener to remove.
     */
    void removeNodeListener(DemoNodeListener nodeListener);

    /**
     * @return depth level of this node.  0 for the root (a demo), increases with 1 for each generation down.
     */
    int getDepth();

    /**
     * @return number of depth levels of nodes nested under this node.
     */
    int getMaxDepth();

    /**
     * Prompts this node to update its stored max depth.
     * Called automatically as needed.
     */
    void updateMaxDepth();

    /**
     * The parent node that has no parent.
     */
    DemoNode getRootNode();

    int getTotalNumberOfDescendants();
}
