package org.demoflow.field;

import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.CalculatorBase;

/**
 *
 */
public abstract class FieldBase extends CalculatorBase<Field> implements Field {

    @Override
    protected final Field doCalculate(CalculationContext calculationContext, Field currentValue, Parameter<Field> parameter) {
        updateField(calculationContext);
        return this;
    }

    @Override public final Class<Field> getReturnType() {
        return Field.class;
    }

    /**
     * Called every update, allowing the field to do any necessary state changes.
     * @param calculationContext context with current time.
     */
    protected void updateField(CalculationContext calculationContext) {
    }


    // Override if a field has changing state
    @Override protected void doResetState() {
    }

}
