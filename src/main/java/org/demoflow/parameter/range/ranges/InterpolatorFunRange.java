package org.demoflow.parameter.range.ranges;

import org.demoflow.calculator.function.InterpolatorFun;
import org.demoflow.parameter.range.RangeBase;
import org.flowutils.random.RandomSequence;

/**
 * Range of interpolator functions
 */
public final class InterpolatorFunRange extends RangeBase<InterpolatorFun> {

    public static final InterpolatorFunRange FULL = new InterpolatorFunRange();

    public InterpolatorFunRange() {
        super(InterpolatorFun.class, "Interpolator function (function from 0..1 values to 0..1 values)");
    }

    @Override protected InterpolatorFun createRandomValue(RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for InterpolatorFuns");
    }

    @Override protected InterpolatorFun doMutateValue(InterpolatorFun value, float mutationAmount, RandomSequence randomSequence) {
        throw new UnsupportedOperationException("This operation is not supported for InterpolatorFuns");
    }

    @Override protected InterpolatorFun createCopy(InterpolatorFun source) {
        // No support for copies, return source
        return source;
    }

    @Override protected InterpolatorFun createDefaultValue() {
        throw new UnsupportedOperationException("This operation is not supported for InterpolatorFuns");
    }

    @Override public InterpolatorFun clampToRange(InterpolatorFun originalValue) {
        // No support for ranges, return source
        return originalValue;
    }

    @Override public InterpolatorFun interpolate(double t, InterpolatorFun a, InterpolatorFun b, InterpolatorFun out) {
        throw new UnsupportedOperationException("This operation is not supported for InterpolatorFuns");
    }

    @Override protected InterpolatorFun doValueFromString(String text) throws Exception {
        return null;
    }
}
