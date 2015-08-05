package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;

/**
 * Linear interpolation.
 */
public final class LinearTween extends TweenerBase {

    public static final LinearTween DEFAULT = new LinearTween();

    @Override public double tween(double t) {
        return t;
    }
}
