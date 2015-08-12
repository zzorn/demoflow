package org.demoflow.field;

import org.demoflow.parameter.Parameter;

/**
 * Base class for Field implementations that have two underlying Field:s as properties.
 */
public abstract class FieldWithTwoBaseFields extends FieldBase {

    public final Parameter<Field> a;
    public final Parameter<Field> b;

    protected FieldWithTwoBaseFields() {
        this(null, null);
    }

    protected FieldWithTwoBaseFields(Field a, Field b) {
        this("a", a, "b", b);
    }

    protected FieldWithTwoBaseFields(String aName, Field a, String bName, Field b) {
        this.a = addParameter(aName, a);
        this.b = addParameter(bName, b);
    }

    @Override public double get(double x, double y) {
        final Field aField = a.get();
        final Field bField = b.get();
        if (aField != null && bField != null) {
            return calculate(aField, bField, x, y);
        }
        else {
            return valueIfBaseFieldIsNull();
        }
    }

    /**
     * @return the value for this field at the specified location, using the specified underlying base fields.
     */
    protected abstract double calculate(Field a, Field b, double x, double y);

    /**
     * @return value to return if any of the underlying fields are null.  Zero by default.
     */
    protected double valueIfBaseFieldIsNull() {
        return 0.0;
    }

}
