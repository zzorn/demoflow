package org.demoflow.editor;

import org.demoflow.editor.valueeditor.ValueEditor;
import org.demoflow.parameter.range.Range;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for EditorManagers.
 */
public abstract class EditorManagerBase implements EditorManager {

    private final Map<Class, Class<? extends ValueEditor>> registeredValueEditorTypes = new ConcurrentHashMap<>();


    @Override public final <T> ValueEditor<T> createValueEditor(Range<T> range, T initialValue) {
        // Create editor
        final ValueEditor<T> valueEditor = createValueEditor(range);
        if (valueEditor != null) {
            // Set initial edited value
            valueEditor.setValue(initialValue);
            return valueEditor;
        } else {
            return null;
        }
    }

    @Override public final <T> ValueEditor<T> createValueEditor(Range<T> range) {
        notNull(range, "range");

        final Class<ValueEditor<T>> valueEditorType = getValueEditorType(range.getType());

        if (valueEditorType != null) {
            return createInstance(valueEditorType, range);
        } else {
            return null;
        }
    }


    /**
     * Register a value editor.  Determines the type it edits automatically by creating an instance of it and querying it.
     */
    public final <T> void registerValueEditor(Class<? extends ValueEditor<T>> valueEditorType) {
        notNull(valueEditorType, "valueEditorType");

        // Determine type by creating an instance and querying it
        final Class<T> editedType = createInstance(valueEditorType, null).getEditedType();

        registerValueEditor(editedType, valueEditorType);
    }

    /**
     * Register a value editor type for the specified type.
     */
    public final <T> void registerValueEditor(Class<T> editedType, Class<? extends ValueEditor<T>> valueEditorType) {
        notNull(editedType, "editedType");
        notNull(valueEditorType, "valueEditorType");

        registeredValueEditorTypes.put(editedType, valueEditorType);
    }


    @Override public final <T> Class<ValueEditor<T>> getValueEditorType(Class<T> valueType) {
        return (Class<ValueEditor<T>>) registeredValueEditorTypes.get(valueType);
    }

    protected <T> ValueEditor<T> createInstance(Class<? extends ValueEditor<T>> valueEditorType, Range<T> range) {
        try {
            // Get constructor that takes one Range object
            final Constructor<? extends ValueEditor<T>> constructor = valueEditorType.getConstructor(Range.class);

            // Create instance
            return constructor.newInstance(range);

        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("The value editor class " + valueEditorType + " has no constructor that takes a single Range object, can not create editors of that type.");
        } catch (Exception e) {
            throw new IllegalStateException("Problem when invoking constructor for " + valueEditorType + ": " + e.getMessage(), e);
        }
    }
}
