package org.demoflow.calculator.field;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithTwoBaseFields;

/**
 * Subtracts the return values of two Function2:s.
 * If a function is not specified (null), zero is used for that function.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class SubField extends FieldWithTwoBaseFields {

    public SubField() {
    }

    public SubField(Field a, Field b) {
        super(a, b);
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        return a.get(x, y) - b.get(x, y);
    }
}
