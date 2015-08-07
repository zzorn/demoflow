package org.demoflow.editor.valueeditor;

import org.demoflow.parameter.range.Range;

import javax.swing.*;

/**
 * Something that provides an UI for editing a value.
 */
public interface ValueEditor<T> {

    /**
     * @return current value in the editor.
     */
    T getValue();

    /**
     * Update the value to show and edit.
     */
    void setValue(T value);

    /**
     * @return allowed range for the edited value.
     */
    Range<T> getRange();

    /**
     * @return editor UI component.
     */
    JComponent getEditorUi();

    /**
     * @param listener listener to notify about edits to the value.
     */
    void addListener(ValueEditorListener<T> listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(ValueEditorListener<T> listener);
}
