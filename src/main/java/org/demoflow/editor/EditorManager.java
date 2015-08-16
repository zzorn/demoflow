package org.demoflow.editor;

import org.demoflow.editor.valueeditor.ValueEditor;
import org.demoflow.parameter.range.Range;

/**
 *
 */
public interface EditorManager {

    /**
     * @return a new value editor for a value with the specified range, or null if there are no editors for the specified range.
     */
    <T> ValueEditor<T> createValueEditor(Range<T> range);

    /**
     * @return a new value editor for a value with the specified range and initial value, or null if there are no editors for the specified range.
     */
    <T> ValueEditor<T> createValueEditor(Range<T> range, T initialValue);

    /**
     * @return the value editor type that can edit values of the specified type, or null if there are no registered value editors for that type.
     */
    <T> Class<ValueEditor<T>> getValueEditorType(Class<T> valueType);
}
