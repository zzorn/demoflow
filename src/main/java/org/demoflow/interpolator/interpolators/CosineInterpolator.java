package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;

/**
 * Cosine interpolation.
 */
public final class CosineInterpolator extends InterpolatorBase {

    public static final CosineInterpolator DEFAULT = new CosineInterpolator();

    @Override public double interpolate(double t) {
        return 0.5 - 0.5 * Math.cos(t * Math.PI);
    }
}
