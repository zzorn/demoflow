package org.demoflow.calculator.function;

import org.demoflow.calculator.Calculator;

/**
 * For interpolators.
 */
public interface InterpolatorFun extends Fun<InterpolatorFun> {

    /**
     * @return interpolation between a and b depending on position of t (t = 0 -> a, t= 1 -> b)
     */
    double interpolate(double t, double a, double b);

    /**
     * @return interpolates between targetStart and targetEnd, depending on the position of sourcePos between sourceStart and sourceEnd.
     */
    double interpolate(double sourcePos, double sourceStart, double sourceEnd, double targetStart, double targetEnd);
}
