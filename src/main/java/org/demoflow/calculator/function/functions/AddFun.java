package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.DoubleFunctionBase;
import org.demoflow.calculator.function.FieldBase;

/**
 * Adds the two double typed parameters
 */
public final class AddFun extends FieldBase {
    @Override public double get(double x, double y) {
        return x + y;
    }
}
