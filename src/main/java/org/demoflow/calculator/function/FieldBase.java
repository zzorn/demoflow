package org.demoflow.calculator.function;

import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;

/**
 *
 */
public abstract class FieldBase extends FunctionBase<Field> implements Field {

    @Override public final Class<Field> getReturnType() {
        return Field.class;
    }

}
