package org.demoflow.calculator.field;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithTwoBaseFields;

/**
 * Returns the larger of the return values of two Function2:s.
 * If a function is not specified (null), zero is used for that function.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class MaxField extends FieldWithTwoBaseFields {

    public MaxField() {
    }

    public MaxField(Field a, Field b) {
        super(a, b);
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        final double aValue = a.get(x, y);
        final double bValue = b.get(x, y);
        return aValue >= bValue ? aValue : bValue;
    }
}
