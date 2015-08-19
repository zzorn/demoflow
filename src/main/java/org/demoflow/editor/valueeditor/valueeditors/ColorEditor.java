package org.demoflow.editor.valueeditor.valueeditors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.demoflow.editor.valueeditor.TextFieldEditorBase;
import org.demoflow.parameter.range.Range;

/**
 * Editor for Color type values.
 */
// TODO: Add color preview button that opens a color picker dialog
public final class ColorEditor extends TextFieldEditorBase<Color> {

    public ColorEditor(Range<Color> range) {
        super(range);
    }

    @Override public Class<Color> getEditedType() {
        return Color.class;
    }
}
