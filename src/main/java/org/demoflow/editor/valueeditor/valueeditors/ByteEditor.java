package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for byte type values.
 */
public final class ByteEditor extends TextFieldEditorBase<Byte> {

    public ByteEditor(Range<Byte> range) {
        super(range);
    }

    @Override protected Byte parseValue(String text) {
        return Byte.parseByte(text);
    }

    @Override protected DecimalFormat createTextFieldFormat() {
        return new DecimalFormat("#0");
    }

    @Override public Class<Byte> getEditedType() {
        return Byte.class;
    }
}
