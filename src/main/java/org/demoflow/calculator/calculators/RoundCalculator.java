package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;

/**
 * Rounds double to int.
 */
public final class RoundCalculator extends CalculatorBase<Integer> {

    public final Parameter<Double> sourceValue;

    public RoundCalculator() {
        this(0);
    }

    public RoundCalculator(double sourceValue) {
        this.sourceValue = addParameter("sourceValue", sourceValue);
    }

    @Override
    protected Integer doCalculate(CalculationContext calculationContext,
                                  Integer currentValue,
                                  Parameter<Integer> parameter) {
        return (int)(double) sourceValue.get();
    }

    @Override protected void doResetState() {
    }

    @Override public Class<Integer> getReturnType() {
        return Integer.class;
    }
}
