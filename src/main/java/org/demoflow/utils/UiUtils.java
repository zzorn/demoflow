package org.demoflow.utils;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;

/**
 * Swing UI related utility functions.
 */
public final class UiUtils {


    public static void expandAll(final JTree tree) {
        expandCollapseTree(tree, true);
    }

    public static void collapseAll(final JTree tree) {
        expandCollapseTree(tree, false);
    }

    public static void expand(final JTree tree, int maxDepth) {
        expandCollapseTree(tree, true, maxDepth);
    }

    public static void collapse(final JTree tree, int maxDepth) {
        expandCollapseTree(tree, false, maxDepth);
    }

    public static void expandCollapseTree(JTree tree, boolean expand) {
        expandCollapseTree(tree, expand, Integer.MAX_VALUE);
    }

    public static void expandCollapseTree(JTree tree, boolean expand, int maxDepth) {
        final TreeModel model = tree.getModel();
        if (model != null && model.getRoot() != null) {
            final TreePath rootPath = new TreePath(model.getRoot());
            expandCollapseTreePath(tree, rootPath, expand, 0, maxDepth);
        }
    }

    public static void expandCollapseTreePath(JTree tree, TreePath path, boolean expand, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth) return;

        TreeNode node = (TreeNode) path.getLastPathComponent();

        // Recurse to children
        if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
            while (enumeration.hasMoreElements()) {
                TreePath childPath = path.pathByAddingChild(enumeration.nextElement());
                expandCollapseTreePath(tree, childPath, expand, currentDepth + 1, maxDepth);
            }
        }

        if (expand) {
            tree.expandPath(path);
        } else {
            tree.collapsePath(path);
        }
    }


    private UiUtils() {
    }

}
