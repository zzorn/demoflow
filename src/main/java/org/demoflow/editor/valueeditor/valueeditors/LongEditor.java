package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for long type values.
 */
public final class LongEditor extends TextFieldEditorBase<Long> {

    public LongEditor(Range<Long> range) {
        super(range);
    }

    @Override protected DecimalFormat createTextFieldFormat() {
        return new DecimalFormat("#0");
    }

    @Override public Class<Long> getEditedType() {
        return Long.class;
    }
}
