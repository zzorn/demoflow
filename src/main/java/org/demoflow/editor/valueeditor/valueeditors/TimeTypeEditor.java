package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.calculator.TimeType;
import org.demoflow.editor.valueeditor.ComboBoxEditorBase;
import org.demoflow.parameter.range.Range;
import org.demoflow.parameter.range.ranges.InterpolatorRange;
import org.demoflow.parameter.range.SelectRange;
import org.demoflow.parameter.range.ranges.TimeTypeRange;
import org.demoflow.utils.uiutils.TextComboBoxRenderer;
import org.flowutils.StringUtils;

import javax.swing.*;

/**
 * Selects one of the constant time types from a drop down list.
 */
public final class TimeTypeEditor extends ComboBoxEditorBase<TimeType> {

    public TimeTypeEditor() {
        this(TimeTypeRange.FULL);
    }

    public TimeTypeEditor(Range<TimeType> timeTypeRange) {
        super(timeTypeRange);
    }

    @Override public Class<TimeType> getEditedType() {
        return TimeType.class;
    }

    @Override protected ListCellRenderer<TimeType> createRenderer() {
        return new TextComboBoxRenderer<TimeType>() {
            @Override protected String getTextForValue(TimeType value) {
                return StringUtils.capitalize(value.toString().toLowerCase().replace('_', ' '));
            }
        };
    }
}
