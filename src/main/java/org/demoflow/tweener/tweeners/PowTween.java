package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;
import org.flowutils.Check;

/**
 *
 */
public final class PowTween extends TweenerBase {

    private final double exponent;

    public PowTween(double exponent) {
        Check.greaterOrEqual(exponent, "exponent", 0, "zero");

        this.exponent = exponent;
    }

    @Override public double tween(double t) {
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
