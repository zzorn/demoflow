package org.demoflow.editor.valueeditor.valueeditors;

import org.demoflow.editor.valueeditor.ComboBoxEditorBase;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.parameter.range.ranges.InterpolatorRange;
import org.demoflow.parameter.range.ranges.SelectRange;

/**
 *
 */
public class InterpolatorEditor extends ComboBoxEditorBase<Interpolator> {

    public InterpolatorEditor() {
        super(InterpolatorRange.FULL);
    }
}
