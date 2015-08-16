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

    @Override protected Double parseValue(String text) {
        return Double.parseDouble(text);
    }

    @Override protected DecimalFormat createTextFieldFormat() {
        return new DecimalFormat("#0.0###");
    }

    @Override public Class<Double> getEditedType() {
        return Double.class;
    }
}
