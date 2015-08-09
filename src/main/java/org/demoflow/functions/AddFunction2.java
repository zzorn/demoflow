package org.demoflow.functions;

/**
 * Adds the return values of two Function2:s.
 * If a function is not specified (null), zero is used for that function.
 */
public final class AddFunction2 extends Function2TwoFunctionBase {

    public AddFunction2() {
    }

    public AddFunction2(Function2 a, Function2 b) {
        super(a, b);
    }

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        return a.get(x,y) + b.get(x, y);
    }
}
