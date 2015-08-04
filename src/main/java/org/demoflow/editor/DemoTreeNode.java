package org.demoflow.editor;

import com.badlogic.gdx.utils.Array;
import org.demoflow.demo.DemoNode;
import org.demoflow.demo.DemoNodeListener;
import org.demoflow.utils.ArrayEnumeration;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * TreeNode used for JTree UI component to show the structure of the Demo.
 */
public final class DemoTreeNode implements TreeNode {

    private DemoNode demoNode;
    private DemoTreeNode parentTreeNode;
    private Array<DemoTreeNode> treeNodes = new Array<>();

    private final DemoNodeListener listener = new DemoNodeListener() {
        @Override public void onChildAdded(DemoNode parent, DemoNode child) {
            addChildNode(child);
        }

        @Override public void onChildRemoved(DemoNode parent, DemoNode child) {
            removeChildNode(child);
        }

        @Override public void onNodeUpdated(DemoNode node) {
            // TODO: Request a repaint?
        }
    };

    public DemoTreeNode(DemoNode demoNode) {
        this(null, demoNode);
    }

    public DemoTreeNode(DemoTreeNode parentTreeNode, DemoNode demoNode) {
        notNull(demoNode, "demoNode");

        this.parentTreeNode = parentTreeNode;
        this.demoNode = demoNode;

        // Add tree nodes for current children
        final Enumeration<? extends DemoNode> childIterator = demoNode.getChildren();
        while (childIterator.hasMoreElements()) {
            addChildNode(childIterator.nextElement());
        }

        // Listen to demoNode changes.
        demoNode.addNodeListener(listener);
    }

    public DemoNode getDemoNode() {
        return demoNode;
    }

    @Override public DemoTreeNode getChildAt(int childIndex) {
        return treeNodes.get(childIndex);
    }

    @Override public int getChildCount() {
        return treeNodes.size;
    }

    @Override public TreeNode getParent() {
        return parentTreeNode;
    }

    @Override public int getIndex(TreeNode node) {
        return treeNodes.indexOf((DemoTreeNode) node, true);
    }

    @Override public boolean getAllowsChildren() {
        return demoNode.allowsChildNodes();
    }

    @Override public boolean isLeaf() {
        return getChildCount() <= 0;
    }

    @Override public Enumeration<DemoTreeNode> children() {
        return new ArrayEnumeration<>(treeNodes);
    }

    @Override public String toString() {
        return demoNode.toString();
    }

    private void addChildNode(DemoNode child) {
        treeNodes.add(new DemoTreeNode(this, child));
    }

    private void removeChildNode(DemoNode child) {
        for (int i = 0; i < treeNodes.size; i++) {
            final DemoTreeNode treeNode = treeNodes.get(i);
            if (treeNode.demoNode == child) {
                treeNode.onDeleted();
                treeNodes.removeIndex(i);
                return;
            }
        }
    }

    private void onDeleted() {
        if (demoNode != null) {
            demoNode.removeNodeListener(listener);
            demoNode = null;
            parentTreeNode = null;
            for (DemoTreeNode treeNode : treeNodes) {
                treeNode.onDeleted();
            }
        }
    }
}
