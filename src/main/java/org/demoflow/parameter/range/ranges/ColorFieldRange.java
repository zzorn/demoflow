package org.demoflow.parameter.range.ranges;

import org.demoflow.calculator.function.ColorField;
import org.demoflow.parameter.range.RangeBase;
import org.flowutils.random.RandomSequence;

/**
 *
 */
public final class ColorFieldRange extends RangeBase<ColorField> {

    public static final ColorFieldRange FULL = new ColorFieldRange();

    public ColorFieldRange() {
        super(ColorField.class, "2D field with colors (or function taking two doubles and returning a color)");
    }

    @Override protected ColorField createRandomValue(RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override protected ColorField doMutateValue(ColorField value, float mutationAmount, RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override protected ColorField createCopy(ColorField source) {
        // No support for copies, return source
        return source;
    }

    @Override protected ColorField createDefaultValue() {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }

    @Override public ColorField clampToRange(ColorField originalValue) {
        // No support for ranges, return source
        return originalValue;
    }

    @Override public ColorField interpolate(double t, ColorField a, ColorField b, ColorField out) {
        throw new UnsupportedOperationException("This operation is not supported for Fields");
    }
}
