package org.demoflow.editor;

import org.demoflow.editor.valueeditor.ValueEditor;
import org.demoflow.editor.valueeditor.valueeditors.DoubleEditor;
import org.demoflow.parameter.range.Range;

import static org.flowutils.Check.notNull;

/**
 * Registers default value editors.
 */
public class DefaultEditorManager extends EditorManagerBase {

    public DefaultEditorManager() {

        registerValueEditor(Double.class, DoubleEditor.class);

    }
}
