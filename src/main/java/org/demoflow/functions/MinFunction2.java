package org.demoflow.functions;

/**
 * Returns the smaller of the return values of two Function2:s.
 * If a function is not specified (null), zero is used for that function.
 */
public final class MinFunction2 extends Function2TwoFunctionBase {

    public MinFunction2() {
    }

    public MinFunction2(Function2 a, Function2 b) {
        super(a, b);
    }

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        final double aValue = a.get(x, y);
        final double bValue = b.get(x, y);
        return aValue < bValue ? aValue : bValue;
    }
}
