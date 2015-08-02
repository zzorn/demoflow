package org.demoflow.effects;

import org.demoflow.Viewer;
import org.flowutils.Symbol;

import java.util.List;
import java.util.Map;

/**
 * A demo effect.
 */
public interface Effect {

    /**
     * Load needed resources and do other setup.
     */
    // IDEA: Later listener system for progress?
    void setup(Viewer viewer);

    /**
     * Initializes and sets up the effect for display.
     */
    void start(Viewer viewer);

    /**
     * Hides / removes the effect from display.
     */
    void stop(Viewer viewer);

    /**
     * Render this effect for a frame.
     */
    void render(Viewer viewer);

    /**
     * @return information for the parameters provided by this effect.
     */
    Map<Symbol, ParameterRange> getParameterRanges();

    /**
     * @return current values of the parameters used by this effect.
     */
    Map<Symbol, Object> getParameterValues();

    /**
     * Update some of the parameters for this effect.
     * Throws IllegalArgumentException if this effect does not have one of the specified parameters.
     */
    void updateParameters(Map<Symbol, Object> parametersToUpdate);

    /**
     * Sets a parameter of this effect.
     * Throws IllegalArgumentException if this effect does not have that parameter.
     */
    void setParameter(Symbol id, Object value);

    /**
     * @return the specified parameter of this effect.
     * Throws IllegalArgumentException if this effect does not have that parameter.
     */
    <T> T getParameter(Symbol id);

    ParameterRange getParameterRange(Symbol id);
}
