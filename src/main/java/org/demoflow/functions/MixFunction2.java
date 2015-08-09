package org.demoflow.functions;

import org.flowutils.MathUtils;

/**
 * Mixes between the return value of a and b, using a scalar parameter.
 * If a function is not specified (null), zero is used for that function.
 */
public final class MixFunction2 extends Function2TwoFunctionBase {

    private double t = 0.5;

    public MixFunction2() {
        this(0.5, null, null);
    }

    public MixFunction2(Function2 a, Function2 b) {
        this(0.5, a, b);
    }

    /**
     * @param t when 0, returns value of a, when 1, returns value of b.
     */
    public MixFunction2(double t, Function2 a, Function2 b) {
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

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        return MathUtils.mix(t, a.get(x, y), b.get(x, y));
    }
}
