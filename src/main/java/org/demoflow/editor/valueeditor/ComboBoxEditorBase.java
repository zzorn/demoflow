package org.demoflow.editor.valueeditor;

import org.demoflow.parameter.range.Range;
import org.demoflow.parameter.range.ranges.SelectRange;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Base class for editors that use a combo box to select from allowed values.
 */
public abstract class ComboBoxEditorBase<T> extends ValueEditorBase<T> {

    private JComboBox<T> selectCombo;

    public ComboBoxEditorBase(SelectRange<T> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<T> range, T initialValue) {
        selectCombo = new JComboBox<T>(((SelectRange<T>) range).getAllowedValues());

        ListCellRenderer<T> renderer = createRenderer();
        if (renderer != null) selectCombo.setRenderer(renderer);

        selectCombo.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                onValueUpdatedInUi((T) selectCombo.getSelectedItem());
            }
        });
        return selectCombo;
    }

    protected JComboBox<T> getSelectCombo() {
        return selectCombo;
    }

    @Override protected void onValueUpdatedToUi(T value) {
        selectCombo.setSelectedItem(value);
    }

    @Override public Class<T> getEditedType() {
        return getRange().getType();
    }

    protected ListCellRenderer<T> createRenderer() {
        return null;
    }
}
