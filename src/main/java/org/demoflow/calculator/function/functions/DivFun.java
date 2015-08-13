package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.FieldBase;

/**
 * Divides the two double typed parameters
 */
public final class DivFun extends FieldBase {
    @Override public double get(double x, double y) {
        return x / y;
    }
}
