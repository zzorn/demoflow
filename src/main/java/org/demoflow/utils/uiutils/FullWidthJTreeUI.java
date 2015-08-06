package org.demoflow.utils.uiutils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreePath;
import java.awt.*;

import static org.flowutils.Check.notNull;

/**
 * TreeUI that supports cells with width spanning the whole tree.
 */
public class FullWidthJTreeUI extends BasicTreeUI {

    private final JScrollPane surroundingScrollPanel;

    public FullWidthJTreeUI(JScrollPane surroundingScrollPanel) {
        notNull(surroundingScrollPanel, "surroundingScrollPanel");

        this.surroundingScrollPanel = surroundingScrollPanel;
    }

    @Override
    protected AbstractLayoutCache.NodeDimensions createNodeDimensions() {
        return new NodeDimensionsHandler() {
            @Override
            public Rectangle getNodeDimensions(
                    Object value, int row, int depth, boolean expanded,
                    Rectangle size) {
                Rectangle dimensions = super.getNodeDimensions(value, row, depth, expanded, size);
                dimensions.width = surroundingScrollPanel.getViewport().getWidth() - getRowX(row, depth);
                return dimensions;
            }
        };
    }

    @Override
    protected void paintHorizontalLine(Graphics g, JComponent c,
                                       int y, int left, int right) {
        // Don't paint horizontal line
    }

    @Override
    protected void paintVerticalPartOfLeg(Graphics g, Rectangle clipBounds,
                                          Insets insets, TreePath path) {
        // Don't paint vertical part of leg
    }
}