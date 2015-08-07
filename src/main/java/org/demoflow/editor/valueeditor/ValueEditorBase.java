package org.demoflow.editor.valueeditor;

import net.miginfocom.swing.MigLayout;
import org.demoflow.parameter.range.Range;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * An editor component that can edit some type of value.
 */
public abstract class ValueEditorBase<T> extends JPanel implements ValueEditor<T> {

    private final List<ValueEditorListener<T>> listeners = new ArrayList<>(4);
    private final Range<T> range;

    private T value;
    private boolean listenToUiUpdates = true;

    public ValueEditorBase(Range<T> range) {
        this(range, range.getDefaultValue());
    }

    public ValueEditorBase(Range<T> range, T initialValue) {
        super(new MigLayout());

        notNull(range, "range");

        this.range = range;
        this.value = range.clampToRange(initialValue);

        // Build the UI
        final JComponent editorComponent = buildEditorUi(this, this.range, this.value);
        if (editorComponent != null) {
            add(editorComponent);
        }
    }

    @Override public final Range<T> getRange() {
        return range;
    }

    @Override public final T getValue() {
        return value;
    }

    @Override public final void setValue(T value) {
        // Clamp value to range and set it
        this.value = range.clampToRange(value);

        // Show the new value in the editor UI
        listenToUiUpdates = false;
        onValueUpdatedToUi(this.value);
        listenToUiUpdates = true;
    }

    @Override public final void addListener(ValueEditorListener<T> listener) {
        notNull(listener, "listener");
        if (listeners.contains(listener)) throw new IllegalArgumentException("The ValueEditorListener has already been added as a listener, can't add it twice");

        listeners.add(listener);
    }

    @Override public final void removeListener(ValueEditorListener<T> listener) {
        listeners.remove(listener);
    }

    @Override public final JComponent getEditorUi() {
        return this;
    }

    /**
     * Build the editor UI and add it to the provided editor panel.
     * Call onValueUpdatedInUi when the value is updated in the editor.
     *
     * @param editorPanel panel containing the editor.  Has MigLayout.
     * @param range range for values edited in this editor.  Can sometimes be used for slider ranges and such.
     * @param initialValue initial value to show in the editor.
     * @return a component to be added to the editor panel, or null if you add the editors yourself to the editorPanel.
     */
    protected abstract JComponent buildEditorUi(JPanel editorPanel, Range<T> range, T initialValue);

    /**
     * Called when the value to be edited is set from the code outside the editor.
     * The editor should replace the edited value with this new value.
     * Any calls to onValueUpdatedInUi are ignored while this command is executing.
     * @param value new value to edit.
     */
    protected abstract void onValueUpdatedToUi(T value);

    /**
     * Call this when the value was updated in the UI, and the listeners should be notified.
     * This applies a range check to the value automatically.
     * Also sets the value, so no need to call setValue.
     * Calls to this are ignored if they are called from within onValueUpdatedToUi.
     * @param newValue new value (or the existing value if it is mutable and has been internally edited).
     */
    protected final void onValueUpdatedInUi(T newValue) {
        if (listenToUiUpdates) {
            value = range.clampToRange(newValue);

            notifyValueEdited(value);
        }
    }

    /**
     * Notifies listeners about value edits.
     */
    private void notifyValueEdited(final T value) {
        for (ValueEditorListener<T> listener : listeners) {
            listener.onValueEdited(this, value);
        }
    }

}
