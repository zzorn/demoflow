package org.demoflow.calculator.function;

import org.demoflow.calculator.Calculator;

/**
 * Returns a double value based on one input double value.
 */
public interface Fun<T extends Fun> extends Calculator<T> {

    double get(double x);

}
