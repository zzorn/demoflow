package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.FieldBase;

/**
 * Subtracts the two double typed parameters
 */
public final class SubFun extends FieldBase {
    @Override public double get(double x, double y) {
        return x - y;
    }
}
