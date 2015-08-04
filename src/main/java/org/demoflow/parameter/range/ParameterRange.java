package org.demoflow.parameter.range;

import org.flowutils.random.RandomSequence;

/**
 * Represents a range for a parameter.
 * Used to get min, max values and to clamp the parameter.
 */
public interface ParameterRange<T> {

    /**
     * @return original value clamped to a valid range for this parameter.
     * In case of mutable values, the provided value is changed.
     */
    T clampToRange(T originalValue);

    /**
     * @return a random value in the range.
     */
    T randomValue(RandomSequence randomSequence);

    /**
     * Returns a slightly changed value.
     * In case of mutable values, the provided value is changed.
     */
    T mutateValue(T value, float mutationAmount, RandomSequence randomSequence);

    /**
     * Creates an unique copy of the source value, in case of non immutable parameter values.
     */
    T copy(T source);

    /**
     * @return a good default value for this range.
     */
    T getDefaultValue();

    /**
     * @return the type of parameters supported by this ParameterRange.
     */
    Class<T> getType();

    /**
     * @return the value as an user readable string.
     */
    String valueToString(T value);

    // TODO: Serialize / deserialize value from text/xml (or binary)
}
