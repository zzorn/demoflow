package org.demoflow.parameter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.demoflow.field.Field;
import org.demoflow.field.colorfield.ColorField;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeBase;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.Calculator;
import org.demoflow.parameter.range.*;
import org.demoflow.parameter.range.ranges.*;
import org.flowutils.Symbol;

import static org.flowutils.Check.nonEmptyString;
import static org.flowutils.Check.notNull;

/**
 * Common functionality for Parametrized classes.
 */
public abstract class ParametrizedBase extends DemoNodeBase implements Parametrized {

    private final Array<Parameter> parameters = new Array<>(8);

    protected ParametrizedBase() {
        this(null);
    }

    /**
     * @param parent parent Parametrized object, or null if no parent.
     */
    protected ParametrizedBase(Parametrized parent) {
        super(parent);
    }

    @Override public final Array<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Convenience method to create a non-constant boolean parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Boolean> addParameter(String id, boolean initialValue) {
        return addParameter(id, initialValue, BooleanRange.FULL);
    }

    /**
     * Convenience method to create a non-constant int parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Integer> addParameter(String id, int initialValue) {
        return addParameter(id, initialValue, IntRange.FULL);
    }

    /**
     * Convenience method to create a non-constant float parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Float> addParameter(String id, float initialValue) {
        return addParameter(id, initialValue, FloatRange.FULL);
    }

    /**
     * Convenience method to create a non-constant double parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Double> addParameter(String id, double initialValue) {
        return addParameter(id, initialValue, DoubleRange.FULL);
    }

    /**
     * Convenience method to create a non-constant color parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Color> addParameter(String id, Color initialValue) {
        return addParameter(id, initialValue, ColorRange.FULL);
    }

    /**
     * Convenience method to create a non-constant 3D vector parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Vector3> addParameter(String id, Vector3 initialValue) {
        return addParameter(id, initialValue, Vector3Range.FULL);
    }

    /**
     * Convenience method to create a non-constant 2D vector parameter with the full range and specified initial value.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Vector2> addParameter(String id, Vector2 initialValue) {
        return addParameter(id, initialValue, Vector2Range.FULL);
    }

    /**
     * Convenience method to create a constant file handle parameter.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<FileHandle> addParameter(String id, FileHandle initialValue) {
        return addParameter(id, initialValue, FileHandleRange.FULL, true);
    }

    /**
     * Convenience method to create a constant field parameter.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<Field> addParameter(String id, Field initialValue) {
        final Parameter<Field> fieldParameter = addParameter(id, initialValue, FieldRange.FULL, true);
        // Field parameters are a special case as their calculators are also their values.
        fieldParameter.setCalculator(initialValue);
        return fieldParameter;
    }

    /**
     * Convenience method to create a constant color field parameter.
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final Parameter<ColorField> addParameter(String id, ColorField initialValue) {
        final Parameter<ColorField> fieldParameter = addParameter(id, initialValue, ColorFieldRange.FULL, true);
        // Field parameters are a special case as their calculators are also their values.
        fieldParameter.setCalculator(initialValue);
        return fieldParameter;
    }

    /**
     * Creates and adds a non-constant parameter to this Parametrized object.
     *
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @param range range of the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final <T> Parameter<T> addParameter(String id, T initialValue, Range<T> range) {
        return addParameter(id, initialValue, range, false);
    }

    /**
     * Creates and adds a parameter to this Parametrized object.
     *
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @param range range of the parameter.
     * @param constant if true, the parameter will be constant and not changeable over time.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final <T> Parameter<T> addParameter(String id, T initialValue, Range<T> range, boolean constant) {
        return addParameter(id, initialValue, range, constant, null);
    }

    /**
     * Creates and adds a parameter to this Parametrized object.
     *
     * @param id unique id of the parameter.  If a parameter with the same id already exists, an exception will be thrown.
     * @param initialValue initial value for the parameter.
     * @param range range of the parameter.
     * @param constant if true, the parameter will be constant and not changeable over time.
     * @param calculator calculator to use to calculate the value of the parameter.
     * @return the created parameter.  Can be cached by the Parametrized object to allow slightly faster access to getting the parameter value.
     */
    protected final <T> Parameter<T> addParameter(String id, T initialValue, Range<T> range, boolean constant, Calculator<T> calculator) {
        nonEmptyString(id, "id");
        notNull(range, "range");

        final Symbol idSymbol = Symbol.get(id);

        // Check that we don't already have a parameter by that name
        for (int i = 0; i < parameters.size; i++) {
            if (parameters.get(i).getId() == idSymbol) throw new IllegalArgumentException("There is already a parameter with the id " +
                                                                                          idSymbol + " in " + getClass().getSimpleName());
        }

        // Create parameter
        final ParameterImpl<T> parameter = new ParameterImpl<T>(idSymbol, this, range, initialValue, constant, calculator, null);

        // Add parameter
        parameters.add(parameter);

        // Notify listeners in UI
        notifyChildNodeAdded(parameter);

        // Return parameter, so that the implementing class can cache it locally if needed.
        return parameter;
    }

    /**
     * Removes the specified parameter from this Parametrized class.
     * @param parameter parameter to remove.
     */
    protected final void removeParameter(Parameter parameter) {
        if (parameters.removeValue(parameter, true)) {

            // Notify listeners in UI
            notifyChildNodeRemoved(parameter);
        }
    }


    @Override public final Parameter getParameterOrNull(Symbol id) {
        for (int i = 0; i < parameters.size; i++) {
            final Parameter parameter = parameters.get(i);
            if (parameter.getId() == id) {
                return parameter;
            }
        }

        return null;
    }

    @Override public final Parameter getParameter(Symbol id) {
        final Parameter parameter = getParameterOrNull(id);

        if (parameter != null) {
            return parameter;
        } else {
            throw new IllegalArgumentException("No parameter with id '"+id+"' available in " + getClass().getSimpleName());
        }
    }

    @Override public final Parameter getParameterOrNullRecursively(Symbol id) {
        // Try to get from this class
        final Parameter parameter = getParameterOrNull(id);
        if (parameter != null) return parameter;

        // Try to get from parent if available
        final DemoNode parent = getParent();
        if (parent != null && parent instanceof  Parametrized) {
            return ((Parametrized)parent).getParameterOrNullRecursively(id);
        } else {
            return null;
        }
    }

    @Override public final void recalculateParameters(CalculationContext calculationContext) {
        for (int i = 0; i < parameters.size; i++) {
            final Parameter parameter = parameters.get(i);
            parameter.recalculateParameter(calculationContext);
        }
    }

    @Override public void resetParametersToInitialValues() {
        for (Parameter parameter : parameters) {
            parameter.resetToInitialValue();
        }
    }

    @Override public int getChildCount() {
        return getParameters().size;
    }


    @Override public Array<? extends DemoNode> getChildren() {
        return parameters;
    }


}
