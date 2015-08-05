package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;

/**
 * Linear interpolation.
 */
public final class LinearTween extends TweenerBase {

    @Override public double tween(double t) {
        return t;
    }
}
