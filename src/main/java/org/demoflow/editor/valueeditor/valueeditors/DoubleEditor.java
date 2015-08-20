package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for double type values.
 */
public final class DoubleEditor extends TextFieldEditorBase<Double> {

    public DoubleEditor(Range<Double> range) {
        super(range);
    }

    @Override public Class<Double> getEditedType() {
        return Double.class;
    }
}
