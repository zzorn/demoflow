package org.demoflow.functions;

/**
 * Base class for Function2 implementations that have two Function2:s as properties.
 */
public abstract class Function2TwoFunctionBase implements Function2 {

    protected Function2 a;
    protected Function2 b;

    protected Function2TwoFunctionBase() {
        this(null, null);
    }

    protected Function2TwoFunctionBase(Function2 a, Function2 b) {
        this.a = a;
        this.b = b;
    }

    public final Function2 getA() {
        return a;
    }

    public final void setA(Function2 a) {
        this.a = a;
    }

    public final Function2 getB() {
        return b;
    }

    public final void setB(Function2 b) {
        this.b = b;
    }

    @Override public double get(double x, double y) {
        if (a != null && b != null) return combine(a, b, x, y);
        else if (a != null) return onlyA(a, x, y);
        else if (b != null) return onlyB(b, x, y);
        else return valueIfBothAreNull();
    }

    protected abstract double combine(Function2 a, Function2 b, double x, double y);

    protected double onlyA(Function2 a, double x, double y) {
        return a.get(x, y);
    }

    protected double onlyB(Function2 b, double x, double y){
        return b.get(x, y);
    }

    protected double valueIfBothAreNull() {
        return 0.0;
    }

}
