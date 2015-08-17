package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Fun3Base;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.LinearInterpolator;
import org.demoflow.parameter.Parameter;

/**
 *
 */
public class InterpolationFun extends Fun3Base {

    public final Parameter<Interpolator> interpolator;

    public InterpolationFun() {
        this(LinearInterpolator.DEFAULT);
    }

    public InterpolationFun(Interpolator interpolator) {
        this.interpolator = addParameter("interpolator", interpolator);
    }

    @Override public double get(double x, double y, double z) {
        return interpolator.get().interpolate(x, y, z);
    }
}
