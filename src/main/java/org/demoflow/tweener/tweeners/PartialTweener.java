package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.Tweener;
import org.demoflow.tweener.TweenerBase;
import org.demoflow.tweener.TweeningDirection;

import static org.flowutils.Check.notNull;

/**
 * Takes a specific part of the tweening curve of the underlying tweener.
 */
public final class PartialTweener extends TweenerBase {

    private final Tweener baseTweener;
    private final TweeningDirection tweeningDirection;

    /**
     * Uses the IN direction of the underlying tweener.
     * @param baseTweener the underlying tweener to defer to.
     */
    public PartialTweener(Tweener baseTweener) {
        this(baseTweener, TweeningDirection.IN);
    }

    /**
     * @param baseTweener the underlying tweener to defer to.
     * @param tweeningDirection the part of the underlying tweener to use.
     */
    public PartialTweener(Tweener baseTweener, TweeningDirection tweeningDirection) {
        notNull(baseTweener, "baseTweener");
        notNull(tweeningDirection, "tweeningDirection");

        this.baseTweener = baseTweener;
        this.tweeningDirection = tweeningDirection;
    }

    @Override public double tween(double t) {
        return baseTweener.tween(t, tweeningDirection);
    }
}
