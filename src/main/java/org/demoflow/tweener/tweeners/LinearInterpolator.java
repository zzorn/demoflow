package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.InterpolatorBase;

/**
 * Linear interpolation.
 */
public final class LinearInterpolator extends InterpolatorBase {

    public static final LinearInterpolator DEFAULT = new LinearInterpolator();

    @Override public double interpolate(double t) {
        return t;
    }
}
