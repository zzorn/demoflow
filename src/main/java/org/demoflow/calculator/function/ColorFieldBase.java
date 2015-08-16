package org.demoflow.calculator.function;

import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;

/**
 * Base class for ColorFields.
 */
public abstract class ColorFieldBase extends FunctionBase<ColorField> implements ColorField {

    @Override public final Class<ColorField> getReturnType() {
        return ColorField.class;
    }

}
