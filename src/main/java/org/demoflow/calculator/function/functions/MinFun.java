package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.FieldBase;

/**
 * Returns smallest of the two parameters.
 */
public final class MinFun extends FieldBase {
    @Override public double get(double x, double y) {
        return x < y ? x : y;
    }
}
