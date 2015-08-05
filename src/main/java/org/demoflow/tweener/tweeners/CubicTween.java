package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;

/**
 * Uses the x^3 function to fade in and out.
 */
public final class CubicTween extends TweenerBase {

    public static final CubicTween DEFAULT = new CubicTween();

    @Override public double tween(double t) {
        if (t < 0.5) {
            // Move t to 0..1 range.
            t *= 2.0;

            // Return cube
            return t * t * t * 0.5;
        } else {
            // Move t to 0..1 range
            t -= 0.5;
            t *= 2.0;

            // Flip t to 1..0 range
            t = 1.0 - t;

            // Return flipped over cube
            return 1.0 - t * t * t * 0.5;
        }
    }
}
