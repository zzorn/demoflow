package org.demoflow.editor;


import org.demoflow.utils.uiutils.TextComboBoxRenderer;

/**
 * Renderer for combo boxes that contain classes.
 */
public final class ClassComboBoxRenderer extends TextComboBoxRenderer {

    private final String nameForNullValue;
    private final String stringToRemove;

    public ClassComboBoxRenderer() {
        this("null", null);
    }

    public ClassComboBoxRenderer(String nameForNullValue, String stringToRemove) {
        this.nameForNullValue = nameForNullValue;
        this.stringToRemove = stringToRemove;
    }

    @Override protected String getTextForValue(Object value) {
        if (value == null) return nameForNullValue;
        else {
            String name = ((Class) value).getSimpleName();
            if (stringToRemove != null) name = name.replace(stringToRemove, "");
            return name;
        }
    }
}
