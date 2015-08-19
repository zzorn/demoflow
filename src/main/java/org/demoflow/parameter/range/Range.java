package org.demoflow.parameter.range;

import nu.xom.Element;
import nu.xom.Node;
import org.demoflow.DemoComponentManager;
import org.demoflow.utils.gradient.ValueInterpolator;
import org.flowutils.random.RandomSequence;

import javax.swing.*;
import java.io.IOException;

/**
 * Represents a range for a parameter.
 * Used to get min, max values and to clamp the parameter.
 * Also contains the type of the parameter.
 */
public interface Range<T> extends ValueInterpolator<T> {

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

    /**
     * @return the value parsed from the string, or default value if it could not be parsed.
     */
    T valueFromString(String text);

    /**
     * @return the value parsed from the string, or null if it could not be parsed.
     */
    T valueFromStringOrNull(String text);

    /**
     * @return human readable description of this range / type of value, or null if none provided.
     */
    String getDescription();

    /**
     * @return name of icon to use for this type of value, or null if there is no specific icon.
     */
    String getIconName();

    /**
     * @return an icon for this type of range / value, or null if no icon is available.
     */
    Icon getIcon();

    // TODO: Serialize / deserialize value from text/xml (or binary)

    Node valueToXml(T value);

    T valueFromXml(Node element, DemoComponentManager typeManager) throws IOException;

    String rangeToXml();

    /**
     * @return true if the value of this range is a function (calculator).
     */
    boolean isParametrizedValue();
}
