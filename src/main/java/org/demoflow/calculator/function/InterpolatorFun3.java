package org.demoflow.calculator.function;

import org.demoflow.interpolator.InterpolationDirection;
import org.demoflow.interpolator.Interpolator;

/**
 *
 */
public final class InterpolatorFun3 extends Fun3Base {
    @Override public double get(double x, double y, double z) {
        // IMPLEMENT: Implement get
        return 0;
    }
    /*
    public InterpolatorFun3(Class<Interpolator> interpolator, InterpolationDirection interpolationDirection, double sourceStart, double sourceEnd, double targetStart, double targetEnd, boolean clampSource) {
        addParameter("interpolator", interpolator);
    }

    @Override public double get(double x, double y, double z) {
        return interpolator.interpolate(x, y, z);
    }
    */
}
