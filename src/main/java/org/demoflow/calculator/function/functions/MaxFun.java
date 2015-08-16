package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.FieldBase;

/**
 * Returns largest of the two parameters.
 */
public final class MaxFun extends FieldBase {
    @Override public double get(double x, double y) {
        return x >= y ? x : y;
    }
}
