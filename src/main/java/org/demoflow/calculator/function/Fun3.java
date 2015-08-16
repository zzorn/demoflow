package org.demoflow.calculator.function;

import org.demoflow.calculator.Calculator;

/**
 * Returns a double value based on the three input double values.
 * Can be used e.g. for volumetric fields or interpolators.
 */
public interface Fun3 extends Calculator<Fun3> {

    double get(double x, double y, double z);

}
