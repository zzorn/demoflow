package org.demoflow.demo;

/**
 * Listens to children being added or removed to a demo node.
 * Can also be notified about general updates to the node.
 */
public interface DemoNodeListener {

    /**
     * Child added
     * @param parent node we are listening to
     * @param child added child
     */
    void onChildAdded(DemoNode parent, DemoNode child);

    /**
     * Child removed
     * @param parent node we are listening to
     * @param child removed child
     */
    void onChildRemoved(DemoNode parent, DemoNode child);

    /**
     * Suggests that the node could be redrawn in the UI.
     * @param node node we are listening to.
     */
    void onNodeUpdated(DemoNode node);

}
