package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;
import org.flowutils.Check;

/**
 * Flips from zero to one at the specified threshold.
 */
public final class FlipTween extends TweenerBase {

    public static final FlipTween DEFAULT = new FlipTween();

    private final double threshold;

    public FlipTween() {
        this(0.5);
    }

    public FlipTween(double threshold) {
        Check.inRangeZeroToOne(threshold, "threshold");

        this.threshold = threshold;
    }

    @Override public double tween(double t) {
        return t < threshold ? 0 : 1;
    }
}
