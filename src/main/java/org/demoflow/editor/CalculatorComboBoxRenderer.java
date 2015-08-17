package org.demoflow.editor;


import org.demoflow.utils.uiutils.TextComboBoxRenderer;

/**
 * Renderer for combo boxes that allow selecting a calculator.
 */
public final class CalculatorComboBoxRenderer extends TextComboBoxRenderer {

    @Override protected String getTextForValue(Object value) {
        return value == null ? "Constant" : ((Class) value).getSimpleName().replace("Calculator", "");
    }
}
