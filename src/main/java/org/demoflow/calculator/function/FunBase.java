package org.demoflow.calculator.function;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;

/**
 *
 */
public abstract class FunBase extends CalculatorBase<Fun> implements Fun<Fun> {

    @Override
    protected Fun doCalculate(CalculationContext calculationContext, Fun currentValue, Parameter<Fun> parameter) {
        return this;
    }

    @Override protected void doResetState() {
    }

    @Override public Class<Fun> getReturnType() {
        return Fun.class;
    }
}
