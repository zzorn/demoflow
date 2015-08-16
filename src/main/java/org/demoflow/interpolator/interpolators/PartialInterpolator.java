package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.InterpolatorBase;
import org.demoflow.interpolator.InterpolationDirection;

import static org.flowutils.Check.notNull;

/**
 * Takes a specific part of the tweening curve of the underlying interpolator.
 */
public final class PartialInterpolator extends InterpolatorBase {

    private final Interpolator baseInterpolator;
    private final InterpolationDirection interpolationDirection;

    /**
     * Uses the IN direction of the underlying interpolator.
     * @param baseInterpolator the underlying interpolator to defer to.
     */
    public PartialInterpolator(Interpolator baseInterpolator) {
        this(baseInterpolator, InterpolationDirection.IN);
    }

    /**
     * @param baseInterpolator the underlying interpolator to defer to.
     * @param interpolationDirection the part of the underlying interpolator to use.
     */
    public PartialInterpolator(Interpolator baseInterpolator, InterpolationDirection interpolationDirection) {
        notNull(baseInterpolator, "baseTweener");
        notNull(interpolationDirection, "tweeningDirection");

        this.baseInterpolator = baseInterpolator;
        this.interpolationDirection = interpolationDirection;
    }

    @Override public double interpolate(double t) {
        return baseInterpolator.interpolate(t, interpolationDirection);
    }
}
