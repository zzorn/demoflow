package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;

/**
 * Uses the x^2 function to fade in and out.
 */
public final class QuadraticInterpolator extends InterpolatorBase {

    public static final QuadraticInterpolator IN_OUT = new QuadraticInterpolator();

    @Override public double interpolate(double t) {
        if (t < 0.5) {
            // Move t to 0..1 range.
            t *= 2.0;

            // Return quad
            return t * t * 0.5;
        } else {
            // Move t to 0..1 range
            t -= 0.5;
            t *= 2.0;

            // Flip t to 1..0 range
            t = 1.0 - t;

            // Return flipped over quad
            return 1.0 - t * t * 0.5;
        }
    }
}
