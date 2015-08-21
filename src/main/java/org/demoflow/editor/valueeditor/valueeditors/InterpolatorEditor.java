package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ComboBoxEditorBase;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.parameter.range.Range;
import org.demoflow.parameter.range.ranges.InterpolatorRange;
import org.demoflow.parameter.range.SelectRange;
import org.demoflow.utils.uiutils.TextComboBoxRenderer;

import javax.swing.*;

/**
 * Selects one of the constant interpolators from a drop down list.
 */
public final class InterpolatorEditor extends ComboBoxEditorBase<Interpolator> {

    public InterpolatorEditor() {
        this(InterpolatorRange.FULL);
    }

    public InterpolatorEditor(Range<Interpolator> range) {
        super((SelectRange<Interpolator>) range);
    }

    @Override public Class<Interpolator> getEditedType() {
        return Interpolator.class;
    }

    @Override protected ListCellRenderer<Interpolator> createRenderer() {
        return new TextComboBoxRenderer<Interpolator>() {
            @Override protected String getTextForValue(Interpolator value) {
                return value.getClass().getSimpleName().replace("Interpolator", "");
            }
        };
    }
}
