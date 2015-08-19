package org.demoflow.calculator.calculators;

import org.demoflow.calculator.TimeVaryingCalculatorBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;

import static org.flowutils.MathUtils.*;

/**
 * Parameter calculator that creates a sine wave that varies over time.
 * The wave length in seconds, output range, and phase shift can all be configured or updated on the fly with other calculators.
 */
public final class SineCalculator extends TimeVaryingCalculatorBase {

    public SineCalculator() {
    }

    public SineCalculator(double wavelength) {
        super(wavelength);
    }

    public SineCalculator(double wavelength, double minOutput, double maxOutput) {
        super(wavelength, minOutput, maxOutput);
    }

    public SineCalculator(double wavelength, double minOutput, double maxOutput, double phase) {
        super(wavelength, minOutput, maxOutput, phase);
    }

    @Override protected double calculateValue(double phase) {
        return Math.sin(phase * Tau);
    }
}
