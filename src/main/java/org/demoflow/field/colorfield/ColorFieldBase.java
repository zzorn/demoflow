package org.demoflow.field.colorfield;

import org.demoflow.field.Field;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.CalculatorBase;

/**
 * Base class for ColorFields.
 */
public abstract class ColorFieldBase extends CalculatorBase<ColorField> implements ColorField {

    @Override
    protected final ColorField doCalculate(CalculationContext calculationContext, ColorField currentValue, Parameter<ColorField> parameter) {
        updateField(calculationContext);
        return this;
    }

    @Override public final Class<ColorField> getReturnType() {
        return ColorField.class;
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
