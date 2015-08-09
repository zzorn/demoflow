package org.demoflow.node;

/**
 *
 */
public abstract class DemoNodeListenerAdapter implements DemoNodeListener {
    @Override public void onChildAdded(DemoNode parent, DemoNode child) {
    }

    @Override public void onChildRemoved(DemoNode parent, DemoNode child) {
    }

    @Override public void onNodeUpdated(DemoNode node) {
    }

    @Override public void onNodeMaxDepthChanged(DemoNode node, int newMaxDepth) {
    }
}
