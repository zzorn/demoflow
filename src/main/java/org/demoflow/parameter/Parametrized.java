package org.demoflow.parameter;

import com.badlogic.gdx.utils.Array;
import org.demoflow.node.DemoNode;
import org.demoflow.calculator.CalculationContext;
import org.flowutils.Symbol;

/**
 * Something that has parameters that may be calculated over time.
 */
public interface Parametrized extends DemoNode {

    /**
     * @return the effect or calculator or parameter that this parametrized object is in.
     *         Can be used to get parameters visible in the current context.
     */
    DemoNode getParent();

    /**
     * Update the parent of this node.
     */
    void setParent(DemoNode parent);

    /**
     * @return the parameters available in this Parametrized class.
     */
    Array<Parameter> getParameters();

    boolean hasParameter(Symbol id);

    /**
     * @return the parameter with the specified id.
     * @throws IllegalArgumentException if there is no parameter with that id.
     */
    Parameter getParameter(Symbol id);

    /**
     * @return the parameter with the specified id, or null if no such parameter available.
     */
    Parameter getParameterOrNull(Symbol id);

    /**
     * @return the parameter with the specified id from this Parametrized class,
     *         or a parameter with the specified id from the parent(s) of this Parametrized class.
     *         Returns null if there is no parameter with that id.
     */
    Parameter getParameterOrNullRecursively(Symbol id);

    /**
     * Uses the available calculators to update the parameter values.
     * @param calculationContext context to pass to the calculators.
     */
    void recalculateParameters(CalculationContext calculationContext);

    /**
     * Resets current parameter values to the initial values.
     */
    void resetParametersToInitialValues();
}
