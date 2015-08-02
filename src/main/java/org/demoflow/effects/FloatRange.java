package org.demoflow.effects;

import org.flowutils.Check;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.MathUtils.*;

/**
 * A range for a floating point value.
 */
public final class FloatRange implements ParameterRange<Float> {

    private final float min;
    private final float max;
    private final float defaultValue;
    private final float standardDeviation;

    public static final FloatRange ZERO_TO_ONE = new FloatRange(0, 1);
    public static final FloatRange MINUS_ONE_TO_ONE = new FloatRange(-1, 1);
    public static final FloatRange SMALL = new FloatRange(-100, 100);
    public static final FloatRange SMALL_NON_NEGATIVE = new FloatRange(0, 100);

    public FloatRange(float min, float max) {
        this(min, max, average(min, max));
    }

    public FloatRange(float min, float max, float defaultValue) {
        this(min, max, defaultValue, average(min, max) - min);
    }

    public FloatRange(float min, float max, float defaultValue, float standardDeviation) {
        Check.greater(max, "max", min, "min");
        Check.normalNumber(defaultValue, "defaultValue");
        Check.normalNumber(standardDeviation, "standardDeviation");
        this.min = min;
        this.max = max;
        this.defaultValue = defaultValue;
        this.standardDeviation = standardDeviation;
    }

    @Override public Float clampToRange(Float originalValue) {
        return MathUtils.clampToRange(originalValue, min, max);
    }

    @Override public Float randomValue(RandomSequence randomSequence) {
        return randomSequence.nextFloat(min, max);
    }

    @Override public Float mutateValue(Float value, float mutationAmount, RandomSequence randomSequence) {
        return clampToRange(value + mutationAmount * randomSequence.nextGaussianFloat() * standardDeviation);
    }

    @Override public Float getDefaultValue() {
        return defaultValue;
    }

    @Override public Float copy(Float source) {
        return source;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }
}
