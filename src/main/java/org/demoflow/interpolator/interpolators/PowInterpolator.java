package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;
import org.flowutils.Check;

/**
 * Uses the x^exponent function to fade in and out.
 */
public final class PowInterpolator extends InterpolatorBase {

    private final double exponent;

    public PowInterpolator(double exponent) {
        Check.greaterOrEqual(exponent, "exponent", 0, "zero");

        this.exponent = exponent;
    }

    @Override public double interpolate(double t) {
        if (t < 0.5) {
            // Move t to 0..1 range.
            t *= 2.0;

            // Return power
            return Math.pow(t, exponent) * 0.5;
        } else {
            // Move t to 0..1 range
            t -= 0.5;
            t *= 2.0;

            // Flip t to 1..0 range
            t = 1.0 - t;

            // Return flipped over power
            return 1.0 - Math.pow(t, exponent) * 0.5;
        }
    }
}
