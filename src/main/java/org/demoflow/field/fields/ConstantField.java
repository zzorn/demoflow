package org.demoflow.field.fields;

import org.demoflow.field.FieldBase;
import org.demoflow.parameter.Parameter;

/**
 * A field with a constant value, independent of the coordiantes.
 */
public final class ConstantField extends FieldBase {

    public final Parameter<Double> value;

    public ConstantField() {
        this(0);
    }

    public ConstantField(double value) {
        this.value = addParameter("value", value);
    }

    @Override public double get(double x, double y) {
        return value.get();
    }
}
