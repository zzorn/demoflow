package org.demoflow.calculator.function;

import org.demoflow.calculator.Calculator;

/**
 * A function that takes one double value and returns a double value.
 */
public interface DoubleFunction extends Calculator<DoubleFunction> {

    double get(double x);

}
