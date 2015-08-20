package org.demoflow.utils.uiutils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import static org.flowutils.Check.notNull;

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


    public static void setJTreeIndent(JTree tree, final int indentAmountPixels) {
        BasicTreeUI basicTreeUI = (BasicTreeUI) tree.getUI();
        basicTreeUI.setRightChildIndent(indentAmountPixels);
    }

    public static int getJTreeIndent(JTree tree) {
        BasicTreeUI basicTreeUI = (BasicTreeUI) tree.getUI();
        return basicTreeUI.getRightChildIndent() + basicTreeUI.getLeftChildIndent();
    }

    public static ImageIcon loadIcon(String iconPath) {
        notNull(iconPath, "iconPath");

        final Image image = Toolkit.getDefaultToolkit().getImage(iconPath);
        return new ImageIcon(image);
    }


    public static Action createAction(String name, ActionListener actionListener) {
        return createAction(name, null, null, null, actionListener);
    }

    public static Action createAction(String name, String tooltip, ActionListener actionListener) {
        return createAction(name, tooltip, null, null, actionListener);
    }

    public static Action createAction(String name, String tooltip, String iconPath, ActionListener actionListener) {
        return createAction(name, tooltip, iconPath, null, actionListener);
    }

    public static Action createAction(final String name,
                                      final String tooltip,
                                      final String iconPath,
                                      final KeyStroke acceleratorKey,
                                      final ActionListener actionListener) {
        notNull(name, "name");
        notNull(actionListener, "actionListener");

        final AbstractAction action = new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        };

        // Load icon
        ImageIcon icon = null;
        if (iconPath != null) {
            icon = loadIcon(iconPath);
        }

        // Initialize the action fields
        action.putValue(Action.NAME, name);
        action.putValue(Action.SHORT_DESCRIPTION, tooltip);
        action.putValue(Action.LONG_DESCRIPTION, tooltip);
        action.putValue(Action.SMALL_ICON, icon);
        action.putValue(Action.LARGE_ICON_KEY, icon);
        action.putValue(Action.ACCELERATOR_KEY, acceleratorKey);

        return action;
    }


    private UiUtils() {
    }

    public static void forceWidth(final JComponent component, final int width) {
        final Dimension size = new Dimension(width, component.getHeight());
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }

    public static void forceHeight(final JComponent component, final int height) {
        final Dimension size = new Dimension(component.getWidth(), height);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }

    public static void forceSize(final JComponent component, int width, final int height) {
        final Dimension size = new Dimension(width, height);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setMinimumSize(size);
    }
}
