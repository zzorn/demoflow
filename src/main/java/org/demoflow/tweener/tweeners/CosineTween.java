package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;

/**
 * Cosine interpolation.
 */
public final class CosineTween extends TweenerBase {

    public static final CosineTween DEFAULT = new CosineTween();

    @Override public double tween(double t) {
        return 0.5 - 0.5 * Math.cos(t * Math.PI);
    }
}
