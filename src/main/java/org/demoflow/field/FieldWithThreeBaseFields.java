package org.demoflow.field;

import org.demoflow.parameter.Parameter;

/**
 * Base class for Field implementations that have three underlying Field:s as properties.
 */
public abstract class FieldWithThreeBaseFields extends FieldBase {

    public final Parameter<Field> a;
    public final Parameter<Field> b;
    public final Parameter<Field> c;

    protected FieldWithThreeBaseFields() {
        this(null, null, null);
    }

    protected FieldWithThreeBaseFields(Field a, Field b, Field c) {
        this("a", a, "b", b, "c", c);
    }

    protected FieldWithThreeBaseFields(String aName, Field a, String bName, Field b, String cName, Field c) {
        this.a = addParameter(aName, a);
        this.b = addParameter(bName, b);
        this.c = addParameter(cName, c);
    }

    @Override public double get(double x, double y) {
        final Field aField = a.get();
        final Field bField = b.get();
        final Field cField = c.get();
        if (aField != null && bField != null && cField != null) {
            return calculate(aField, bField, cField, x, y);
        }
        else {
            return valueIfBaseFieldIsNull();
        }
    }

    /**
     * @return the value for this field at the specified location, using the specified underlying base fields.
     */
    protected abstract double calculate(Field a, Field b, Field c, double x, double y);

    /**
     * @return value to return if any of the underlying fields are null.  Zero by default.
     */
    protected double valueIfBaseFieldIsNull() {
        return 0.0;
    }

}
