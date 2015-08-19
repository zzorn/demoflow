package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;

/**
 * Returns relative position within the demo as a value from 0 to 1.
 */
// TODO: Add enum parameter for selecting the type of time (relative effect pos or demo pos, or absolute time, etc).
public final class TimeCalculator extends CalculatorBase<Double> {

    @Override
    protected Double doCalculate(CalculationContext calculationContext,
                                 Double currentValue,
                                 Parameter<Double> parameter) {
        return calculationContext.getRelativeEffectPosition();
    }

    @Override protected void doResetState() {
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }
}
