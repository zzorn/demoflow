package org.demoflow.functions;

/**
 * Base class for Function2 implementations that have one Function2 as a property.
 */
public abstract class Function2OneFunctionBase implements Function2 {

    protected Function2 base;

    protected Function2OneFunctionBase() {
        this(null);
    }

    protected Function2OneFunctionBase(Function2 base) {
        this.base = base;
    }

    public Function2 getBase() {
        return base;
    }

    public void setBase(Function2 base) {
        this.base = base;
    }

    @Override public double get(double x, double y) {
        if (base != null) return calculate(base, x, y);
        else return valueIfBaseIsNull();
    }

    protected abstract double calculate(Function2 base, double x, double y);

    protected double valueIfBaseIsNull() {
        return 0.0;
    }

}
