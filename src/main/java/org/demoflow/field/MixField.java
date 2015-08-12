package org.demoflow.field;

import org.flowutils.MathUtils;

/**
 * Mixes between the return value of a and b, using a scalar parameter.
 * If a function is not specified (null), zero is used for that function.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class MixField extends FieldWithTwoBaseFields {

    private double t = 0.5;

    public MixField() {
        this(0.5, null, null);
    }

    public MixField(Field a, Field b) {
        this(0.5, a, b);
    }

    /**
     * @param t when 0, returns value of a, when 1, returns value of b.
     */
    public MixField(double t, Field a, Field b) {
        super(a, b);
        this.t = t;
    }

    /**
     * @return when 0, returns value of a, when 1, returns value of b.
     */
    public double getT() {
        return t;
    }

    /**
     * @param t when 0, returns value of a, when 1, returns value of b.
     */
    public void setT(double t) {
        this.t = t;
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        return MathUtils.mix(t, a.get(x, y), b.get(x, y));
    }
}
