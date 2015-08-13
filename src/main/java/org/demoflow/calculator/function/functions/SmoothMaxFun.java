package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.FieldBase;

/**
 * Mixes between the return value of a and b, depending on which one is larger, using a specified interpolator.
 */
public final class SmoothMaxFun extends FieldBase {



    @Override public double get(double x, double y) {
        return x >= y ? x : y;
    }
}
