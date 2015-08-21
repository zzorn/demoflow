package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldBase;
import org.demoflow.calculator.function.Fun;
import org.demoflow.parameter.Parameter;

/**
 * Applies a function to the outputs of a field.
 */
public final class ModifiedField extends FieldBase {

    public final Parameter<Field> baseField;
    public final Parameter<Fun> function;

    public ModifiedField() {
        this(null, null);
    }

    public ModifiedField(Field baseField, Fun function) {
        this.baseField = addParameter("baseField", baseField);
        this.function = addParameter("function", function);
    }

    @Override public double get(double x, double y) {
        final Field field = baseField.get();
        final Fun fun = function.get();

        double fieldValue = field != null ? field.get(x, y) : 0;
        return fun != null ? fun.get(fieldValue) : fieldValue;
    }
}
