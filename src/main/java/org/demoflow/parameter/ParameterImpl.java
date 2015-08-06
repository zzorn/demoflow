package org.demoflow.parameter;

import com.badlogic.gdx.utils.Array;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeBase;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.Calculator;
import org.demoflow.parameter.range.ParameterRange;
import org.demoflow.utils.ArrayUtils;
import org.demoflow.utils.EmptyArray;
import org.demoflow.utils.EmptyEnumeration;
import org.flowutils.Symbol;

import java.util.Arrays;
import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * Implementation of Parameter.
 */
public final class ParameterImpl<T> extends DemoNodeBase implements Parameter<T> {

    private final Symbol id;
    private final boolean constant;
    private ParameterRange<T> range;
    private T value;
    private T initialValue;
    private Calculator<T> calculator;
    private ParameterListener<T> listener;

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     */
    public ParameterImpl(Symbol id, Parametrized host, ParameterRange<T> range) {
        this(id, host, range, range.getDefaultValue());
    }

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     * @param value initial value of the parameter.
     */
    public ParameterImpl(Symbol id, Parametrized host, ParameterRange<T> range, T value) {
        this(id, host, range, value, false);
    }

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     * @param value initial value of the parameter.
     * @param constant if true, the parameter can not be changed over time.
     */
    public ParameterImpl(Symbol id, Parametrized host, ParameterRange<T> range, T value, boolean constant) {
        this(id, host, range, value, constant, null, null);
    }

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     * @param value initial value of the parameter.
     * @param constant if true, the parameter can not be changed over time.
     * @param calculator calculator to use to calculate the parameter, or null if no calculator assigned initially.
     * @param listener listener that is notified when the parameter value is changed.
     */
    public ParameterImpl(Symbol id,
                         Parametrized host,
                         ParameterRange<T> range,
                         T value,
                         boolean constant,
                         Calculator<T> calculator,
                         ParameterListener<T> listener) {
        super(host);

        notNull(id, "id");
        notNull(host, "host");
        notNull(range, "range");

        this.id = id;
        this.constant = constant;
        this.range = range;
        this.value = range.copy(value);
        this.initialValue = range.copy(value);
        this.listener = listener;
        this.calculator = calculator;
    }

    @Override public Symbol getId() {
        return id;
    }

    @Override public String getName() {
        return id.getString();
    }

    @Override public Parametrized getHost() {
        return (Parametrized) getParent();
    }

    @Override public T get() {
        return value;
    }

    @Override public void set(T newValue) {
        set(newValue, true);
    }

    @Override public void set(T newValue, boolean alsoSetInitialValue) {
        value = range.clampToRange(newValue);

        // Update initial value as well if requested
        if (alsoSetInitialValue) {
            initialValue = range.copy(value);
        }

        // Notify listeners
        getHost().onParameterChanged(this, id, value);
        if (listener != null) listener.onChanged(this, newValue);
        notifyNodeUpdated();
    }

    @Override public ParameterRange<T> getRange() {
        return range;
    }

    @Override public Calculator<T> getCalculator() {
        return calculator;
    }

    @Override public <C extends Calculator<T>> C setCalculator(C calculator) {
        if (this.calculator != calculator) {
            if (this.calculator != null) {
                this.calculator.setParent(null);
                notifyChildNodeRemoved(this.calculator);
            }

            this.calculator = calculator;

            if (this.calculator != null) {
                this.calculator.setParent(this);
                notifyChildNodeAdded(this.calculator);
            }
            else {
                // Revert to previously manually set value when a calculator is removed
                set(initialValue, false);
            }

            notifyNodeUpdated();
        }

        return calculator;
    }

    @Override public boolean isConstant() {
        return constant;
    }

    @Override public Class<T> getType() {
        return range.getType();
    }

    @Override public void recalculateParameter(CalculationContext calculationContext) {
        if (calculator != null) {
            // Set value without changing the initial value
            set(calculator.calculate(calculationContext, value, this), false);
        }
    }

    @Override public void resetToInitialValue() {
        // Revert to initial value (or manually specified value)
        set(range.copy(initialValue), false);

        // Reset calculator state as well
        if (calculator != null) {
            calculator.resetState();
        }
    }

    public ParameterListener<T> getListener() {
        return listener;
    }

    public void setListener(ParameterListener<T> listener) {
        this.listener = listener;
    }

    @Override public int getChildCount() {
        return calculator != null ? calculator.getChildCount() : 0;
    }

    @Override public Array<? extends DemoNode> getChildren() {
        if (calculator != null) return calculator.getChildren();
        else return EmptyArray.EMPTY_ARRAY;
    }

    @Override public int getTotalNumberOfDescendants() {
        if (calculator != null) return  calculator.getTotalNumberOfDescendants();
        else return 0;
    }
}
