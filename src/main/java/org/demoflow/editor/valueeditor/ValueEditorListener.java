package org.demoflow.editor.valueeditor;

/**
 * Listens to ValueEditors.
 */
public interface ValueEditorListener<T> {

    /**
     * Called when a value is edited in the editor.
     * This is not called if the value in the editor was just updated from code with the setter.
     *
     * @param editor editor that the value is edited in.
     * @param editedValue new edited value.  If the value is mutable, this is typically the same value object with new internal values.
     */
    void onValueEdited(ValueEditor<T> editor, T editedValue);

}
