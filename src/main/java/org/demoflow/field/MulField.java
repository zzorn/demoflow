package org.demoflow.field;

/**
 * Multiplies the return values of two Function2:s.
 * If a function is not specified (null), one is used for that function.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class MulField extends FieldWithTwoBaseFields {

    public MulField() {
    }

    public MulField(Field a, Field b) {
        super(a, b);
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        return a.get(x, y) * b.get(x, y);
    }
}
