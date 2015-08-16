package org.demoflow.calculator.function;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldBase;
import org.demoflow.parameter.Parameter;

/**
 * Base class for Field implementations that have one base Field as a parameter.
 */
public abstract class FieldWithOneBaseField extends FieldBase {

    /**
     * The underlying base field that this field uses.
     */
    public final Parameter<Field> base;

    protected FieldWithOneBaseField() {
        this(null);
    }

    protected FieldWithOneBaseField(Field base) {
        this("base", base);
    }

    /**
     * @param base the underlying base field.
     */
    protected FieldWithOneBaseField(String baseName, Field base) {
        this.base = addParameter(baseName, base);
    }

    /**
     * @return the underlying base field.
     */
    public Field getBase() {
        return base.get();
    }

    /**
     * @param base the underlying base field.
     */
    public final void setBase(Field base) {
        this.base.setCalculator(base);
    }

    @Override public final double get(double x, double y) {
        final Field baseField = base.get();
        if (baseField != null) {
            return calculate(baseField, x, y);
        }
        else {
            return valueIfBaseFieldIsNull();
        }
    }

    /**
     * @return the value for this field at the specified location, using the specified underlying base field.
     */
    protected abstract double calculate(Field base, double x, double y);

    /**
     * @return value if the base field is null.  Zero by default.
     */
    protected double valueIfBaseFieldIsNull() {
        return 0.0;
    }

}
