package org.demoflow.parameter.range.ranges;

import org.demoflow.parameter.range.RangeBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.Check.notNull;

/**
 * A range for a string value.
 */
public final class StringRange extends RangeBase<String> {

    private final int maxLength;

    public static final StringRange FULL = new StringRange(Integer.MAX_VALUE);
    public static final StringRange LARGE = new StringRange(Short.MAX_VALUE);
    public static final StringRange SMALL = new StringRange(256);
    public static final StringRange ONE_CHAR = new StringRange(1);

    public StringRange(int maxLength) {
        super(String.class, "String");
        Check.positive(maxLength, "maxLength");

        this.maxLength = maxLength;
    }

    @Override protected String createRandomValue(RandomSequence randomSequence) {
        return "";
    }

    @Override protected String doMutateValue(String value, float mutationAmount, RandomSequence randomSequence) {
        return value;
    }

    @Override protected String createCopy(String source) {
        return source;
    }

    @Override protected String createDefaultValue() {
        return "";
    }

    @Override public String clampToRange(String originalValue) {
        return originalValue.substring(0, Math.min(originalValue.length(), maxLength));
    }

    @Override public String interpolate(double t, String a, String b, String out) {
        return t >= 0 ? b : a;
    }
}
