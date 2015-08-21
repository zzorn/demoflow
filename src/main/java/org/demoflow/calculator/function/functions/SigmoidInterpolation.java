package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.interpolator.interpolators.SigmoidInterpolator;
import org.flowutils.interpolator.interpolators.WavyInterpolator;

/**
 * Sigmoid interpolation.
 */
public final class SigmoidInterpolation extends InterpolatorFunBase<SigmoidInterpolator> {

    public final Parameter<Double> sharpness;

    public SigmoidInterpolation() {
        super(new SigmoidInterpolator());

        sharpness = addParameter("sharpness", getInterpolator().getSharpness());
    }

    @Override protected void updateInterpolator(SigmoidInterpolator interpolator) {
        interpolator.setSharpness(sharpness.get());
    }
}
