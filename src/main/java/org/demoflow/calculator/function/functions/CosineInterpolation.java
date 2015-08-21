package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.flowutils.interpolator.Interpolator;
import org.flowutils.interpolator.interpolators.SineInterpolator;

/**
 * Cosine interpolation.
 */
public final class CosineInterpolation extends InterpolatorFunBase<SineInterpolator> {

    public CosineInterpolation() {
        super(SineInterpolator.IN_OUT);
    }

    @Override protected void updateInterpolator(SineInterpolator interpolator) {
    }
}
