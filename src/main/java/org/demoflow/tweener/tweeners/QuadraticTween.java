package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;

/**
 *
 */
public final class QuadraticTween extends TweenerBase {

    @Override public double tween(double t) {
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
