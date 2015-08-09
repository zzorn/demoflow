package org.demoflow.functions;

/**
 * Returns the larger of the return values of two Function2:s.
 * If a function is not specified (null), zero is used for that function.
 */
public final class MaxFunction2 extends Function2TwoFunctionBase {

    public MaxFunction2() {
    }

    public MaxFunction2(Function2 a, Function2 b) {
        super(a, b);
    }

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        final double aValue = a.get(x, y);
        final double bValue = b.get(x, y);
        return aValue >= bValue ? aValue : bValue;
    }
}
