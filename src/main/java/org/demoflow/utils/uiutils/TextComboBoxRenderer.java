package org.demoflow.utils.uiutils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Renders items in a combo box using a method to get the textual representation of them.
 */
public abstract class TextComboBoxRenderer<V> extends BasicComboBoxRenderer implements ListCellRenderer {


    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        final Component superResult = super.getListCellRendererComponent(list,
                                                                         value,
                                                                         index,
                                                                         isSelected,
                                                                         cellHasFocus);

        // Set a readable text
        setText(getTextForValue((V) value));

        return superResult;
    }

    protected abstract String getTextForValue(V value);
}
