package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.InterpolatorBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;

import static org.flowutils.Check.notNull;

/**
 * Interpolates in a specific number of discrete steps.
 * Uses an underlying interpolator to determine the function to sample (defaults to linear).
 */
public final class SteppingInterpolator extends InterpolatorBase {

    public static final SteppingInterpolator DEFAULT = new SteppingInterpolator();

    private final int steps;
    private final Interpolator baseInterpolator;

    public SteppingInterpolator() {
        this(8);
    }

    public SteppingInterpolator(int steps) {
        this(steps, LinearInterpolator.DEFAULT);
    }

    public SteppingInterpolator(int steps, Interpolator baseInterpolator) {
        Check.positive(steps, "steps");
        notNull(baseInterpolator, "baseTweener");

        this.steps = steps;
        this.baseInterpolator = baseInterpolator;
    }

    @Override public double interpolate(double t) {
        final double stepSize = 1.0 / (steps + 1.0);
        final int step = MathUtils.fastFloor(t / stepSize);
        return baseInterpolator.interpolate(step / steps);
    }
}
