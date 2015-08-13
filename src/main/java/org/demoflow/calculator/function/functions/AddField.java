package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithTwoBaseFields;

/**
 * Adds the return values of two Field:s.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class AddField extends FieldWithTwoBaseFields {

    public AddField() {
    }

    public AddField(Field a, Field b) {
        super(a, b);
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        return a.get(x,y) + b.get(x, y);
    }
}
