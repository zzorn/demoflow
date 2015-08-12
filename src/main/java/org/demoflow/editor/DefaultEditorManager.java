package org.demoflow.editor;

import org.demoflow.editor.valueeditor.ValueEditor;
import org.demoflow.editor.valueeditor.valueeditors.DoubleEditor;
import org.demoflow.parameter.range.Range;
import org.demoflow.utils.ClassUtils;
import org.flowutils.LogUtils;

import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Registers all value editors defined in the default value editor package.
 */
public class DefaultEditorManager extends EditorManagerBase {

    public DefaultEditorManager() {

        // Find value editor implementations from the default value editor package
        final List<Class<? extends ValueEditor>> valueEditors = ClassUtils.getClassesImplementing("org.demoflow",
                                                                                                  ValueEditor.class,
                                                                                                  "org.demoflow.editor.valueeditor.valueeditors");

        // Register the found value editor types
        for (Class<? extends ValueEditor> valueEditor : valueEditors) {
            registerValueEditor((Class<? extends ValueEditor<Object>>)valueEditor);
        }
    }

}
