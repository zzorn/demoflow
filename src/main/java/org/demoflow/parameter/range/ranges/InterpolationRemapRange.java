package org.demoflow.parameter.range.ranges;

import org.demoflow.parameter.range.SelectRange;
import org.flowutils.interpolator.InterpolationRemap;

/**
 * Range for InterpolationRemaps.
 */
public final class InterpolationRemapRange extends SelectRange<InterpolationRemap> {

    public static final InterpolationRemapRange FULL = new InterpolationRemapRange();

    public InterpolationRemapRange() {
        super(InterpolationRemap.class, InterpolationRemap.values());
    }
}
