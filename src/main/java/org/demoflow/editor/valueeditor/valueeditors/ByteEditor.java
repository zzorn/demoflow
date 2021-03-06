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

    @Override public Class<Byte> getEditedType() {
        return Byte.class;
    }
}
