package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.flowutils.interpolator.Interpolator;
import org.flowutils.interpolator.interpolators.LinearInterpolator;

/**
 * Basic linear interpolation.
 */
public final class LinearInterpolation extends InterpolatorFunBase<LinearInterpolator> {

    public LinearInterpolation() {
        super(LinearInterpolator.IN_OUT);
    }

    @Override protected void updateInterpolator(LinearInterpolator interpolator) {
    }
}
