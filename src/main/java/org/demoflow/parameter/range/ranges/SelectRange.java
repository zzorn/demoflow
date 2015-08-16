package org.demoflow.parameter.range.ranges;

import org.demoflow.parameter.range.RangeBase;
import org.flowutils.Check;
import org.flowutils.random.RandomSequence;

import java.util.concurrent.ConcurrentHashMap;

import static org.flowutils.Check.notNull;

/**
 * A range that has a number of allowed values.
 */
public class SelectRange<T> extends RangeBase<T> {

    // Static cache with Range objects, to avoid having to recreate them all the time.
    private static final ConcurrentHashMap<Object[], SelectRange> cachedEnumRanges = new ConcurrentHashMap<>();

    private final T[] allowedValues;


    /**
     * @param allowedValues the allowed values as returned by Enum.values(), or a handpicked selection of allowed values.
     * @return a range object for the specified allowed values.
     */
    public static <E, I extends E> SelectRange<E> get(Class<E> enumType, I ... allowedValues) {
        notNull(enumType, "enumType");

        SelectRange<E> selectRange = cachedEnumRanges.get(allowedValues);
        if (selectRange == null) {
            cachedEnumRanges.putIfAbsent(allowedValues, new SelectRange(enumType, allowedValues));
            selectRange = cachedEnumRanges.get(allowedValues);
        }

        return selectRange;
    }


    /**
     * Use the static getEnumRange method, or implement a subclass.
     */
    protected <I extends T> SelectRange(Class<T> type, I ... allowedValues) {
        super(type);
        notNull(allowedValues, "allowedValues");
        Check.greater(allowedValues.length, "number of values", 1, "one");

        this.allowedValues = allowedValues;
    }

    /**
     * @return array with allowed values for this enum.
     */
    public T[] getAllowedValues() {
        return allowedValues;
    }

    /**
     * @return index of the specified value.
     */
    public int getEnumIndex(T enumValue) {
        notNull(enumValue, "enumValue");

        for (int i = 0; i < allowedValues.length; i++) {
            if (enumValue.equals(allowedValues[i])) return i;
        }

        throw new IllegalArgumentException("Not in enum range: " + enumValue.toString());
    }

    /**
     * @return enum value for the specified index, or null for index values outside the range.
     */
    public T getEnumValue(int index) {
        if (index >= 0 && index < allowedValues.length) {
            return allowedValues[index];
        } else {
            return null;
        }
    }

    @Override protected T createRandomValue(RandomSequence randomSequence) {
        return randomSequence.nextElement(allowedValues);
    }

    @Override protected T doMutateValue(T value, float mutationAmount, RandomSequence randomSequence) {
        if (randomSequence.nextBoolean(mutationAmount)) {
            return randomSequence.nextElement(allowedValues);
        }
        else return value;
    }

    @Override protected T createCopy(T source) {
        return source;
    }

    @Override protected T createDefaultValue() {
        return allowedValues[0];
    }

    @Override public T clampToRange(T originalValue) {
        notNull(originalValue, "originalValue");

        for (T allowedValue : allowedValues) {
            if (originalValue.equals(allowedValue)) return originalValue;
        }

        return createDefaultValue();
    }

    @Override public T interpolate(double t, T a, T b, T out) {
        return t < 0.5 ? a : b;
    }
}
