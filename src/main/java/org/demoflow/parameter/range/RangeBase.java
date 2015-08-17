package org.demoflow.parameter.range;

import org.demoflow.utils.UiUtils;
import org.flowutils.random.RandomSequence;

import javax.swing.*;
import java.awt.*;
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
}
