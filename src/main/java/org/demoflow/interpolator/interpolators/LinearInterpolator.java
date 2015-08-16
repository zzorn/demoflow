package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;

/**
 * Linear interpolation.
 */
public final class LinearInterpolator extends InterpolatorBase {

    public static final LinearInterpolator DEFAULT = new LinearInterpolator();

    @Override public double interpolate(double t) {
        return t;
    }
}
