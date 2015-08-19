package org.demoflow.editor.valueeditor.valueeditors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

/**
 * Editor for Vector2 type values.
 */
public final class Vector2Editor extends TextFieldEditorBase<Vector2> {

    public Vector2Editor(Range<Vector2> range) {
        super(range);
    }

    @Override public Class<Vector2> getEditedType() {
        return Vector2.class;
    }
}
