package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.Interpolator;
import org.demoflow.tweener.InterpolatorBase;
import org.demoflow.tweener.InterpolationDirection;

import static org.flowutils.Check.notNull;

/**
 * Takes a specific part of the tweening curve of the underlying tweener.
 */
public final class PartialInterpolator extends InterpolatorBase {

    private final Interpolator baseInterpolator;
    private final InterpolationDirection interpolationDirection;

    /**
     * Uses the IN direction of the underlying tweener.
     * @param baseInterpolator the underlying tweener to defer to.
     */
    public PartialInterpolator(Interpolator baseInterpolator) {
        this(baseInterpolator, InterpolationDirection.IN);
    }

    /**
     * @param baseInterpolator the underlying tweener to defer to.
     * @param interpolationDirection the part of the underlying tweener to use.
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
