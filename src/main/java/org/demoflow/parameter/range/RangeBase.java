package org.demoflow.parameter.range;

import com.thoughtworks.xstream.XStream;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.demoflow.DemoComponentManager;
import org.demoflow.calculator.Calculator;
import org.demoflow.node.DemoNode;
import org.demoflow.utils.UiUtils;
import org.flowutils.random.RandomSequence;

import javax.swing.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for Ranges.
 */
public abstract class RangeBase<T> implements Range<T> {

    private static final String DEFAULT_TYPE_ICONS_PATH = "assets/icons/types/";
    private final Class<T> type;

    private final NumberFormat numberFormat = new DecimalFormat("#0.0###");

    private final String iconName;
    private final String description;

    private ImageIcon cachedIcon;

    /**
     * @param type type of values in this range.
     * @param description human readable description of this type / range, or null if none provided.
     */
    public RangeBase(Class<T> type, String description) {
        this(type, description, null);
    }

    /**
     * @param type type of values in this range.
     * @param description human readable description of this type / range, or null if none provided.
     * @param iconName name of icon to use for this type / range, or null if none provided.
     */
    public RangeBase(Class<T> type, String description, String iconName) {
        notNull(type, "type");
        this.type = type;
        this.description = description;

        // Provide a default icon name
        if (iconName == null) iconName = getClass().getSimpleName().replace("Range", "");
        this.iconName = iconName;
    }

    @Override public final T randomValue(RandomSequence randomSequence) {
        return clampToRange(createRandomValue(randomSequence));
    }

    @Override public final T mutateValue(T value, float mutationAmount, RandomSequence randomSequence) {
        return clampToRange(doMutateValue(value, mutationAmount, randomSequence));
    }

    @Override public final T copy(T source) {
        return clampToRange(source == null ? null : createCopy(source));
    }

    @Override public final T getDefaultValue() {
        return clampToRange(createDefaultValue());
    }

    @Override public final Class<T> getType() {
        return type;
    }

    @Override public String valueToString(T value) {
        if (value == null) return "null";
        else if (value instanceof Float ||
                 value instanceof Double) {
            return numberFormat.format(value);
        }
        else return value.toString();
    }

    public final T valueFromString(String text) {
        T value;
        try {
            value = doValueFromString(text);
        }
        catch (Exception e) {
            value = createDefaultValue();
        }

        return clampToRange(value);
    }

    protected abstract T doValueFromString(String text) throws Exception;


    @Override public final T interpolate(double t, T a, T b) {
        return interpolate(t, a, b, createDefaultValue());
    }

    protected abstract T createRandomValue(RandomSequence randomSequence);
    protected abstract T doMutateValue(T value, float mutationAmount, RandomSequence randomSequence);
    protected abstract T createCopy(T source);
    protected abstract T createDefaultValue();

    @Override public final String getDescription() {
        return description;
    }

    @Override public final String getIconName() {
        return iconName;
    }

    @Override public final Icon getIcon() {
        if (iconName == null) return null;

        if (cachedIcon == null) {
            cachedIcon = UiUtils.loadIcon(DEFAULT_TYPE_ICONS_PATH + iconName + ".png");
        }

        return cachedIcon;
    }

    @Override public final Node valueToXml(T value) {
        return value == null ? new Text("") : doValueToXml(value);
    }

    @Override public final T valueFromXml(Node element, DemoComponentManager typeManager) throws IOException {
        if (element == null) return null;
        else return clampToRange(doValueFromXml(element, element.getValue(), typeManager));
    }

    protected Node doValueToXml(T value) {
        if (value instanceof Calculator) {
            // Calculator type values are saved in the calculator field instead
            return null;
        }
        else {
            return new Text(valueToString(value));
        }
    }

    protected T doValueFromXml(Node element, String elementText, DemoComponentManager typeManager) throws IOException {
        if (Calculator.class.isAssignableFrom(getType())) {
            // Calculator type values are saved in the calculator field instead
            return null;
        }
        else {
            return valueFromString(elementText);
        }
    }

    @Override public String rangeToXml() {
        return new XStream().toXML(this);
    }

    @Override public boolean isParametrizedValue() {
        return Calculator.class.isAssignableFrom(type);
    }

    protected final void addAttribute(Element element, final String name, final Number value) {
        addAttribute(element, name, value.toString());
    }

    protected final void addAttribute(Element element, final String name, final String value) {
        element.addAttribute(new Attribute(name, value));
    }

    protected double getDoubleAttribute(Element element, String attributeName) {
        return getDoubleAttribute(element, attributeName, 0.0);
    }

    private double getDoubleAttribute(Element element, String attributeName, final double defaultValue) {
        try {
            return Double.parseDouble(element.getAttributeValue(attributeName));
        }
        catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
