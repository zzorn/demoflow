package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for integer type values.
 */
public final class IntEditor extends TextFieldEditorBase<Integer> {

    public IntEditor(Range<Integer> range) {
        super(range);
    }

    @Override protected Integer parseValue(String text) {
        return Integer.parseInt(text);
    }

    @Override protected DecimalFormat createTextFieldFormat() {
        return new DecimalFormat("#0");
    }

    @Override public Class<Integer> getEditedType() {
        return Integer.class;
    }
}
