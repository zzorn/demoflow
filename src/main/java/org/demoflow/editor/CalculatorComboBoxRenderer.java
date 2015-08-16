package org.demoflow.editor;


import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;

/**
 * Renderer for combo boxes that allow selecting a calculator.
 */
public final class CalculatorComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer {

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
        final String text = value == null ? "Constant" : ((Class) value).getSimpleName().replace("Calculator", "");
        setText(text);

        return superResult;
    }
}
