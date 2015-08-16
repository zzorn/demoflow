package org.demoflow.calculator.function;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;

/**
 * Base class for various functions.
 * Should extend T
 */
public abstract class FunctionBase<T> extends CalculatorBase<T> {

    @Override
    protected final T doCalculate(CalculationContext calculationContext, T currentValue, Parameter<T> parameter) {
        updateField(calculationContext);
        return (T) this;
    }

    /**
     * Called every update, allowing the function to do any necessary state changes.
     * @param calculationContext context with current time.
     */
    protected void updateField(CalculationContext calculationContext) {
    }


    // Override if a function has changing state
    @Override protected void doResetState() {
    }


}
