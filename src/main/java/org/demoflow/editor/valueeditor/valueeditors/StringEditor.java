package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;
import org.demoflow.utils.SimpleStringFormat;

import java.text.Format;

/**
 * Editor for string type values.
 */
public final class StringEditor extends TextFieldEditorBase<String> {

    public StringEditor(Range<String> range) {
        super(range);
    }

    @Override public Class<String> getEditedType() {
        return String.class;
    }
}
