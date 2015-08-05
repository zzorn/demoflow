package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.Tweener;
import org.demoflow.tweener.TweenerBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;

import static org.flowutils.Check.notNull;

/**
 * Interpolates in a specific number of discrete steps.
 * Uses an underlying tweener to determine the function to sample (defaults to linear).
 */
public final class SteppingTween extends TweenerBase {

    public static final SteppingTween DEFAULT = new SteppingTween();

    private final int steps;
    private final Tweener baseTweener;

    public SteppingTween() {
        this(8);
    }

    public SteppingTween(int steps) {
        this(steps, LinearTween.DEFAULT);
    }

    public SteppingTween(int steps, Tweener baseTweener) {
        Check.positive(steps, "steps");
        notNull(baseTweener, "baseTweener");

        this.steps = steps;
        this.baseTweener = baseTweener;
    }

    @Override public double tween(double t) {
        final double stepSize = 1.0 / (steps + 1.0);
        final int step = MathUtils.fastFloor(t / stepSize);
        return baseTweener.tween(step / steps);
    }
}
