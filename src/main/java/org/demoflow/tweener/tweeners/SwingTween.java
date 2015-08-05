package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;
import org.flowutils.Check;

/**
 * Swing interpolation.
 */
public final class SwingTween extends TweenerBase {

    private final double scale;

    public SwingTween() {
        this(1.5);
    }

    public SwingTween(double scale) {
        Check.normalNumber(scale, "scale");

        this.scale = scale;
    }

    @Override public double tween(double t) {
        if (t <= 0.5) {
            // Map t to 0..1
            t *= 2.0;
            return t * t * ((scale + 1.0) * t - scale) * 0.5;
        }
        else {
            // Map t to -1..1
            t -= 1.0;
            t *= 2.0;
            return t * t * ((scale + 1.0) * t + scale) * 0.5 + 1.0;
        }
    }
}
