package org.demoflow.parameter.range.ranges;

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.demoflow.DemoComponentManager;
import org.demoflow.calculator.Calculator;
import org.demoflow.calculator.function.ColorField;
import org.demoflow.calculator.function.Field;
import org.demoflow.parameter.range.RangeBase;
import org.flowutils.random.RandomSequence;

import java.io.IOException;

/**
 *
 */
public final class FieldRange extends RangeBase<Field> {

    public static final FieldRange FULL = new FieldRange();

    public FieldRange() {
        super(Field.class, "2D Field with double values (or function taking two doubles and returning a double)");
    }

    @Override protected Field createRandomValue(RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override protected Field doMutateValue(Field value, float mutationAmount, RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override protected Field createCopy(Field source) {
        // No support for copies, return source
        return source;
    }

    @Override protected Field createDefaultValue() {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override public Field clampToRange(Field originalValue) {
        // No support for ranges, return source
        return originalValue;
    }

    @Override public Field interpolate(double t, Field a, Field b, Field out) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override protected Field doValueFromString(String text) throws Exception {
        return null;
    }
}
