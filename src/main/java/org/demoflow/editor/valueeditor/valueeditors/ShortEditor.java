package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

import java.text.DecimalFormat;

/**
 * Editor for short type values.
 */
public final class ShortEditor extends TextFieldEditorBase<Short> {

    public ShortEditor(Range<Short> range) {
        super(range);
    }

    @Override public Class<Short> getEditedType() {
        return Short.class;
    }
}
