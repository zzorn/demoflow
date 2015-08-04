package org.demoflow.demo;

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
     * @return current number of child nodes.
     */
    int getChildCount();

    /**
     * @return enumeration with all the current children.
     * NOTE: Implementations create new enumeration objects on each call, so this should not be used for tight inner loops in rendering, rather it is designed for UI use.
     */
    Enumeration<? extends DemoNode> getChildren();

    /**
     * @param nodeListener listener that is notified about added and removed children, and general UI updates.
     */
    void addNodeListener(DemoNodeListener nodeListener);

    /**
     * @param nodeListener DemoNodeListener to remove.
     */
    void removeNodeListener(DemoNodeListener nodeListener);

    boolean allowsChildNodes();
}
