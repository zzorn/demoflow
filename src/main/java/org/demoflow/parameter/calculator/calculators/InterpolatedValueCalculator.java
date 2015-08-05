package org.demoflow.parameter.calculator.calculators;

import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.CalculatorBase;
import org.demoflow.utils.gradient.Gradient;
import org.demoflow.utils.gradient.GradientImpl;

/**
 * Uses a Gradient to store values at different relative time points over the duration of the effect (0..1),
 * and interpolates them to calculate the value at a specific time.
 */
public final class InterpolatedValueCalculator<T> extends CalculatorBase<T> {

    private final Gradient<T> gradient = new GradientImpl<>();

    /**
     * @return gradient with the changes to the parameter value over the duration of the effect.
     *         A position of 0 refers to the start of the effect, and 1 to the end of the effect.
     */
    public Gradient<T> getGradient() {
        return gradient;
    }

    @Override protected T doCalculate(CalculationContext calculationContext, T currentValue, Parameter<T> parameter) {
        return gradient.getValueAt(calculationContext.getRelativeEffectPosition(), parameter.getRange());
    }

    @Override protected void doResetState() {
        // No changing state
    }
}
