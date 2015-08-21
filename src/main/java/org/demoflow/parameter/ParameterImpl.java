package org.demoflow.parameter;

import com.badlogic.gdx.utils.Array;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;
import org.demoflow.DemoComponentManager;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeBase;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.Calculator;
import org.demoflow.parameter.range.Range;
import org.demoflow.utils.EmptyArray;
import org.flowutils.Symbol;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.flowutils.Check.notNull;

/**
 * Implementation of Parameter.
 */
public final class ParameterImpl<T> extends DemoNodeBase implements Parameter<T> {

    private final Symbol id;
    private final boolean constant;
    private Range<T> range;
    private T value;
    private T defaultValue;
    private Calculator<T> calculator;
    private ArrayList<ParameterListener<T>> listeners = null;


    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     */
    public ParameterImpl(Symbol id, Parametrized host, Range<T> range) {
        this(id, host, range, range.getDefaultValue());
    }

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     * @param value initial value of the parameter.
     */
    public ParameterImpl(Symbol id, Parametrized host, Range<T> range, T value) {
        this(id, host, range, value, false);
    }

    /**
     * @param id unique id for the parameter within the Parametrized class where it is.
     * @param host the Parametrized class that this parameter belongs to.
     * @param range allowed range for the parameter.
     * @param value initial value of the parameter.
     * @param constant if true, the parameter can not be changed over time.
     */
    public ParameterImpl(Symbol id, Parametrized host, Range<T> range, T value, boolean constant) {
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
                         Range<T> range,
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

        set(range.copy(value), true);
        setCalculator(calculator);

        if (listener != null) {
            addParameterListener(listener);
        }
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
        if (hasParametrizedValue()) return (T) calculator;
        else return value;
    }

    @Override public void set(T newValue) {
        set(newValue, true);
    }

    @Override public void set(T newValue, boolean alsoSetInitialValue) {
        if (hasParametrizedValue()) {
            // Store the value in the calculator field
            setCalculator((Calculator<T>) newValue);
        }
        else {
            value = range.clampToRange(newValue);

            // Update initial value as well if requested
            if (alsoSetInitialValue) {
                defaultValue = range.copy(value);
            }
        }

        // Notify listeners
        notifyValueChanged(newValue);
        if (alsoSetInitialValue) notifyDefaultValueChanged(newValue);
        // notifyNodeUpdated();
    }

    @Override public Range<T> getRange() {
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
                if (!hasParametrizedValue()) set(defaultValue, false);
            }

            notifyNodeUpdated();
            notifyCalculatorChanged(this.calculator);

            if (hasParametrizedValue()) {
                notifyValueChanged((T) calculator);
            }
        }

        return calculator;
    }

    @Override public boolean isConstant() {
        return constant;
    }

    @Override public Class<T> getType() {
        return range.getType();
    }

    @Override public Icon getIcon() {
        return range.getIcon();
    }

    @Override public void recalculateParameter(CalculationContext calculationContext) {
        if (calculator != null) {
            if (hasParametrizedValue()) {
                // Recalculate
                calculator.calculate(calculationContext, (T) calculator, this);
            }
            else {
                // Set value without changing the initial value
                set(calculator.calculate(calculationContext, value, this), false);
            }
        }
    }

    @Override public void resetToInitialValue() {

        if (!hasParametrizedValue()) {
            // Revert to initial value (or manually specified value)
            set(range.copy(defaultValue), false);
        }

        // Reset calculator state as well
        if (calculator != null) {
            calculator.resetState();
        }
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

    @Override public T getDefaultValue() {
        return defaultValue;
    }

    @Override public final void addParameterListener(ParameterListener<T> listener) {
        notNull(listener, "listener");
        if (listeners != null && listeners.contains(listener))
            throw new IllegalArgumentException(
                    "The ParameterListener<T> has already been added as a listener, can't add it twice");

        if (listeners == null) {
            listeners = new ArrayList<ParameterListener<T>>(4);
        }

        listeners.add(listener);
    }

    @Override public final void removeParameterListener(ParameterListener<T> listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Notifies about value change.
     */
    private void notifyValueChanged(T newValue) {
        if (listeners != null) {
            for (ParameterListener<T> listener : listeners) {
                listener.onChanged(this, newValue);
            }
        }
    }

    /**
     * Notifies about default value change.
     */
    private void notifyDefaultValueChanged(T newValue) {
        if (listeners != null) {
            for (ParameterListener<T> listener : listeners) {
                listener.onDefaultValueChanged(this, newValue);
            }
        }
    }

    private void notifyCalculatorChanged(Calculator<T> newValue) {
        if (listeners != null) {
            for (ParameterListener<T> listener : listeners) {
                listener.onCalculatorChanged(this, newValue);
            }
        }
    }

    @Override public Element toXmlElement() {
        // Element
        final Element parameterElement = new Element("p"); // Use short name for parameter elements, to save space and visual clutter.  Comes at the cost of a little bit of clarity.
        addAttribute(parameterElement, "id", getId().toString());

        // Value
        if (!hasParametrizedValue()) {
            final Node valueNode = getRange().valueToXml(getDefaultValue());
            if (valueNode instanceof Text) {
                // If it's a text string, make it an attribute to produce more compact and easy to read xml (although harder to parse...)
                addAttribute(parameterElement, "value", valueNode.getValue());
            }
            else {
                // Add the value as a separate contained element
                final Element valueElement = new Element("value");
                valueElement.appendChild(valueNode);
                parameterElement.appendChild(valueElement);
            }
        }

        // Calculator
        if (calculator != null) {
            parameterElement.appendChild(calculator.toXmlElement());
        }

        return parameterElement;
    }

    @Override public void fromXmlElement(Element element, DemoComponentManager typeManager) throws IOException {
        // Parse value of the element using the range of the parameter and set the value if this is a non-functional parameter
        if (!hasParametrizedValue()) {
            final Node valueElement;
            final Attribute valueAttribute = element.getAttribute("value");
            if (valueAttribute != null) {
                // First try to read the value from a value attribute
                valueElement = new Text(valueAttribute.getValue());
            }
            else {
                // Get value from contained child element if found
                valueElement = element.getFirstChildElement("value");
            }

            // If we found a value, parse and set it
            if (valueElement != null) {
                set(getRange().valueFromXml(valueElement, typeManager), true);
            }
        }

        // Parse the calculator for the element
        final Element calculatorElement = element.getFirstChildElement("calculator");
        if (calculatorElement != null) {
            setCalculator(typeManager.loadCalculator(calculatorElement));
        }
    }


    private boolean hasParametrizedValue() {
        return getRange().isParametrizedValue();
    }

}
