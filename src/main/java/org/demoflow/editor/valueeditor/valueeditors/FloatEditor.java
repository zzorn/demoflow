package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for float type values.
 */
public final class FloatEditor extends TextFieldEditorBase<Float> {

    public FloatEditor(Range<Float> range) {
        super(range);
    }

    @Override public Class<Float> getEditedType() {
        return Float.class;
    }
}
