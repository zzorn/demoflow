package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ComboBoxEditorBase;
import org.demoflow.parameter.range.Range;
import org.demoflow.parameter.range.ranges.InterpolationRemapRange;
import org.demoflow.utils.uiutils.TextComboBoxRenderer;
import org.flowutils.StringUtils;
import org.flowutils.interpolator.InterpolationRemap;

import javax.swing.*;

/**
 * Selects one of the InterpolationRemap types from a drop down list.
 */
public final class InterpolationRemapEditor extends ComboBoxEditorBase<InterpolationRemap> {

    public InterpolationRemapEditor() {
        this(InterpolationRemapRange.FULL);
    }

    public InterpolationRemapEditor(Range<InterpolationRemap> range) {
        super(range);
    }

    @Override public Class<InterpolationRemap> getEditedType() {
        return InterpolationRemap.class;
    }

    @Override protected ListCellRenderer<InterpolationRemap> createRenderer() {
        return new TextComboBoxRenderer<InterpolationRemap>() {
            @Override protected String getTextForValue(InterpolationRemap value) {
                return StringUtils.capitalize(value.toString().toLowerCase().replace('_', ' '));
            }
        };
    }
}
