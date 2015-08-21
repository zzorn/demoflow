package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.flowutils.interpolator.interpolators.PowInterpolator;
import org.flowutils.interpolator.interpolators.SineInterpolator;

/**
 * x^n interpolation.
 */
public final class PowInterpolation extends InterpolatorFunBase<PowInterpolator> {

    public final Parameter<Double> exponent;

    public PowInterpolation() {
        super(new PowInterpolator());

        exponent = addParameter("exponent", getInterpolator().getExponent(), DoubleRange.POSITIVE);
    }

    @Override protected void updateInterpolator(PowInterpolator interpolator) {
        interpolator.setExponent(exponent.get());
    }
}
