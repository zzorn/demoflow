package org.demoflow.effects;

import org.flowutils.random.RandomSequence;

/**
 * Represents a range for a parameter.
 * Used to get min, max values and to clamp the parameter.
 */
public interface ParameterRange<T> {

    /**
     * @return original value clamped to a valid range for this parameter.
     */
    T clampToRange(T originalValue);

    /**
     * @return a random value in the range.
     */
    T randomValue(RandomSequence randomSequence);

    /**
     * Returns a slightly changed value.
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


    // TODO: Serialize / deserialize value from text file, etc.
}
