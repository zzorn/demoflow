package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.Interpolator;
import org.demoflow.tweener.InterpolatorBase;
import org.demoflow.tweener.InterpolationDirection;
import org.flowutils.Check;

import static org.flowutils.Check.notNull;

/**
 * Uses one tweener for the start of the slope and another for the end of the slope.
 */
public final class CombinedInterpolator extends InterpolatorBase {

    private final Interpolator startInterpolator;
    private final Interpolator endInterpolator;
    private final InterpolationDirection startDirection;
    private final InterpolationDirection endDirection;
    private final double transitionPoint;

    /**
     * @param startInterpolator tweener to use below 0.5 t.
     * @param endInterpolator tweener to use at and above 0.5 t.
     */
    public CombinedInterpolator(Interpolator startInterpolator, Interpolator endInterpolator) {
        this(startInterpolator, endInterpolator, 0.5);
    }

    /**
     * @param startInterpolator tweener to use below the transition point.
     * @param endInterpolator tweener to use at and above the transition point.
     * @param transitionPoint point along the 0..1 interval where we should switch from startTweener to endTweener.
     */
    public CombinedInterpolator(Interpolator startInterpolator, Interpolator endInterpolator, double transitionPoint) {
        this(startInterpolator, endInterpolator, transitionPoint, InterpolationDirection.IN, InterpolationDirection.OUT);
    }

    /**
     * @param startInterpolator tweener to use below the transition point.
     * @param endInterpolator tweener to use at and above the transition point.
     * @param transitionPoint point along the 0..1 interval where we should switch from startTweener to endTweener.
     * @param startDirection tweening type to use from the first tweener.  IN by default.
     * @param endDirection tweening type to use from the second tweener.  OUT by default.
     */
    public CombinedInterpolator(Interpolator startInterpolator,
                                Interpolator endInterpolator,
                                double transitionPoint,
                                InterpolationDirection startDirection,
                                InterpolationDirection endDirection) {
        notNull(startInterpolator, "startTweener");
        notNull(endInterpolator, "endTweener");
        Check.normalNumber(transitionPoint, "transitionPoint");
        notNull(startDirection, "startDirection");
        notNull(endDirection, "endDirection");

        this.startInterpolator = startInterpolator;
        this.endInterpolator = endInterpolator;
        this.transitionPoint = transitionPoint;
        this.startDirection = startDirection;
        this.endDirection = endDirection;
    }

    @Override public double interpolate(double t) {
        if (t < transitionPoint) {
            return startInterpolator.interpolate(t, 0, transitionPoint, 0, 1, false, startDirection);
        }
        else {
            return endInterpolator.interpolate(t, transitionPoint, 1, 0, 1, false, endDirection);
        }
    }
}
