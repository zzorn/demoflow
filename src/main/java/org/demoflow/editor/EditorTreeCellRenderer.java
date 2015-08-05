package org.demoflow.editor;

import net.miginfocom.swing.MigLayout;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 *
 */
public class EditorTreeCellRenderer extends JPanel implements TreeCellRenderer {

    private JLabel label;
    private JLabel valueLabel;
    private JLabel expandIcon;

    private final Color selectionBackground;
    private final Color normalBackground;

    public EditorTreeCellRenderer() {
        super(new MigLayout());

        // Node name
        label = new JLabel("");
        add(label);

        // Value
        valueLabel = new JLabel("");
        add(valueLabel);

        selectionBackground = DefaultLookup.getColor(this, ui, "Tree.selectionBackground");
        normalBackground    = DefaultLookup.getColor(this, ui, "Tree.textBackground");
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean selected,
                                                  boolean expanded,
                                                  boolean leaf,
                                                  int row,
                                                  boolean hasFocus) {

        // Color selection according to look and feel
        setBackground(selected ? selectionBackground : normalBackground);

        // TODO: Draw focus as well


        label.setText(value.toString());

        valueLabel.setText("sdasd");

        return this;
    }

}
