package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Fun3;
import org.demoflow.calculator.function.Fun3Base;
import org.demoflow.interpolator.interpolators.LinearInterpolator;

/**
 *
 */
public class LinearInterpolationFun3 extends Fun3Base {

    @Override public double get(double x, double y, double z) {
        return LinearInterpolator.DEFAULT.interpolate(x, y, z);
    }
}
