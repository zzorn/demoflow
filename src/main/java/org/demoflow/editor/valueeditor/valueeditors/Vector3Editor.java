package org.demoflow.editor.valueeditor.valueeditors;

import com.badlogic.gdx.math.Vector3;
import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;
import org.demoflow.utils.SimpleStringFormat;

import java.text.Format;

/**
 * Editor for Vector3 type values.
 */
public final class Vector3Editor extends TextFieldEditorBase<Vector3> {

    public Vector3Editor(Range<Vector3> range) {
        super(range);
    }

    @Override public Class<Vector3> getEditedType() {
        return Vector3.class;
    }
}
