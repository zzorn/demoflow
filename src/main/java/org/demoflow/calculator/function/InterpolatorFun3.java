package org.demoflow.calculator.function;

import org.demoflow.interpolator.InterpolationDirection;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.InterpolatorRange;

/**
 *
 */
public final class InterpolatorFun3 extends Fun3Base {
    @Override public double get(double x, double y, double z) {
        return 0;
    }
    /*
    private final Parameter<Interpolator> interpolator;

    public InterpolatorFun3() {
        this(InterpolatorRange.FULL.getDefaultValue(), null, 0, 1)
    }

    public InterpolatorFun3(Interpolator interpolator, InterpolationDirection interpolationDirection, double sourceStart, double sourceEnd, double targetStart, double targetEnd, boolean clampSource) {
        this.interpolator = addParameter("interpolator", interpolator);
    }

    @Override public double get(double x, double y, double z) {
        return interpolator.interpolate(x, y, z);
    }
*/
}
