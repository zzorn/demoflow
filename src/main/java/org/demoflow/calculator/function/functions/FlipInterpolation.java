package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.interpolator.interpolators.FlipInterpolator;
import org.flowutils.interpolator.interpolators.LinearInterpolator;

/**
 * Interpolation that flips from one value to the other at the specified threshold.
 */
public final class FlipInterpolation extends InterpolatorFunBase<FlipInterpolator> {

    public final Parameter<Double> threshold;

    public FlipInterpolation() {
        super(new FlipInterpolator());

        threshold = addParameter("threshold", getInterpolator().getThreshold());
    }

    @Override protected void updateInterpolator(FlipInterpolator interpolator) {
        interpolator.setThreshold(threshold.get());
    }
}
