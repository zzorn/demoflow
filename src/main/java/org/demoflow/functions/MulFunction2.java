package org.demoflow.functions;

/**
 * Multiplies the return values of two Function2:s.
 * If a function is not specified (null), one is used for that function.
 */
public final class MulFunction2 extends Function2TwoFunctionBase {

    public MulFunction2() {
    }

    public MulFunction2(Function2 a, Function2 b) {
        super(a, b);
    }

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        return a.get(x, y) * b.get(x, y);
    }
}
