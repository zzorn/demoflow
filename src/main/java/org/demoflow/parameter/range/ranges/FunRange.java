package org.demoflow.parameter.range.ranges;

import org.demoflow.calculator.function.Fun;
import org.demoflow.parameter.range.RangeBase;
import org.flowutils.random.RandomSequence;

/**
 * Range of double -> double functions
 */
public final class FunRange extends RangeBase<Fun> {

    public static final FunRange FULL = new FunRange();

    public FunRange() {
        super(Fun.class, "Function from double to double values.");
    }

    @Override protected Fun createRandomValue(RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Funs");
    }

    @Override protected Fun doMutateValue(Fun value, float mutationAmount, RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for Funs");
    }

    @Override protected Fun createCopy(Fun source) {
        // No support for copies, return source
        return source;
    }

    @Override protected Fun createDefaultValue() {
        throw new UnsupportedOperationException("This operation is not supported for Funs");
    }

    @Override public Fun clampToRange(Fun originalValue) {
        // No support for ranges, return source
        return originalValue;
    }

    @Override public Fun interpolate(double t, Fun a, Fun b, Fun out) {
        throw new UnsupportedOperationException("This operation is not supported for Funs");
    }

    @Override protected Fun doValueFromString(String text) throws Exception {
        return null;
    }
}
