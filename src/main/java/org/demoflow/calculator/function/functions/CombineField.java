package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.DoubleFunction;
import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldBase;
import org.demoflow.calculator.function.FieldWithThreeBaseFields;
import org.demoflow.parameter.Parameter;

/**
 * Combines two fields with a function.
 */
public class CombineField extends FieldWithThreeBaseFields {

    public CombineField() {
        this(new AddFun(), null, null);
    }

    public CombineField(Field combinator, Field a, Field b) {
        super("combinator", combinator, "a", a, "b", b);
    }

    @Override protected double calculate(Field a, Field b, Field c, double x, double y) {
        return a.get(b.get(x, y), c.get(x, y));
    }
}
