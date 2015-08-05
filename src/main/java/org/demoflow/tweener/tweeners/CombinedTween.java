package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.Tweener;
import org.demoflow.tweener.TweenerBase;
import org.demoflow.tweener.TweeningDirection;
import org.flowutils.Check;

import static org.flowutils.Check.notNull;

/**
 * Uses one tweener for the start of the slope and another for the end of the slope.
 */
public final class CombinedTween extends TweenerBase {

    private final Tweener startTweener;
    private final Tweener endTweener;
    private final TweeningDirection startDirection;
    private final TweeningDirection endDirection;
    private final double transitionPoint;

    /**
     * @param startTweener tweener to use below 0.5 t.
     * @param endTweener tweener to use at and above 0.5 t.
     */
    public CombinedTween(Tweener startTweener, Tweener endTweener) {
        this(startTweener, endTweener, 0.5);
    }

    /**
     * @param startTweener tweener to use below the transition point.
     * @param endTweener tweener to use at and above the transition point.
     * @param transitionPoint point along the 0..1 interval where we should switch from startTweener to endTweener.
     */
    public CombinedTween(Tweener startTweener, Tweener endTweener, double transitionPoint) {
        this(startTweener, endTweener, transitionPoint, TweeningDirection.IN, TweeningDirection.OUT);
    }

    /**
     * @param startTweener tweener to use below the transition point.
     * @param endTweener tweener to use at and above the transition point.
     * @param transitionPoint point along the 0..1 interval where we should switch from startTweener to endTweener.
     * @param startDirection tweening type to use from the first tweener.  IN by default.
     * @param endDirection tweening type to use from the second tweener.  OUT by default.
     */
    public CombinedTween(Tweener startTweener,
                         Tweener endTweener,
                         double transitionPoint,
                         TweeningDirection startDirection,
                         TweeningDirection endDirection) {
        notNull(startTweener, "startTweener");
        notNull(endTweener, "endTweener");
        Check.normalNumber(transitionPoint, "transitionPoint");
        notNull(startDirection, "startDirection");
        notNull(endDirection, "endDirection");

        this.startTweener = startTweener;
        this.endTweener = endTweener;
        this.transitionPoint = transitionPoint;
        this.startDirection = startDirection;
        this.endDirection = endDirection;
    }

    @Override public double tween(double t) {
        if (t < transitionPoint) {
            return startTweener.tween(t, 0, transitionPoint, 0, 1, false, startDirection);
        }
        else {
            return endTweener.tween(t, transitionPoint, 1, 0, 1, false, endDirection);
        }
    }
}
