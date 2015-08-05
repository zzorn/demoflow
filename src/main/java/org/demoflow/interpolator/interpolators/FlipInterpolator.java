package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;
import org.flowutils.Check;

/**
 * Flips from zero to one at the specified threshold.
 */
public final class FlipInterpolator extends InterpolatorBase {

    public static final FlipInterpolator DEFAULT = new FlipInterpolator();

    private final double threshold;

    public FlipInterpolator() {
        this(0.5);
    }

    public FlipInterpolator(double threshold) {
        Check.inRangeZeroToOne(threshold, "threshold");

        this.threshold = threshold;
    }

    @Override public double interpolate(double t) {
        return t < threshold ? 0 : 1;
    }
}
