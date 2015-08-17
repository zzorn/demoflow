package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ValueEditorBase;
import org.demoflow.parameter.range.Range;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Editor for boolean values.
 */
public final class BooleanEditor extends ValueEditorBase<Boolean> {

    private JCheckBox checkBox;

    public BooleanEditor(Range<Boolean> range) {
        super(range);
    }

    @Override protected JComponent buildEditorUi(JPanel editorPanel, Range<Boolean> range, Boolean initialValue) {
        checkBox = new JCheckBox();

        checkBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                onValueUpdatedInUi(checkBox.isSelected());
            }
        });

        return checkBox;
    }

    @Override protected void onValueUpdatedToUi(Boolean value) {
        checkBox.setSelected(value);
    }

    @Override public Class<Boolean> getEditedType() {
        return Boolean.class;
    }
}
