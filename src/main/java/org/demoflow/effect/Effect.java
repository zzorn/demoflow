package org.demoflow.effect;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.demoflow.View;
import org.demoflow.effect.ranges.ParameterRange;
import org.flowutils.Symbol;
import org.flowutils.random.RandomSequence;

import java.util.Map;

/**
 * A demo effect.
 */
public interface Effect<P> {

    /**
     * Does long running pre-calculations for the effect, that can be serialized and saved to disk.
     * E.g. texture generation, genetic algorithm running, etc.
     * @return precalculated data to save and pass to setup, or null if no data is precalculated.
     */
    P preCalculate(RandomSequence preCalculationRandomness);

    /**
     * Load needed resources and do other setup.
     *
     * @param view view with opgl context etc.
     * @param preCalculatedData data calculated by the preCalculate method.
     */
    // IDEA: Later listener system for progress?
    void setup(View view, P preCalculatedData, RandomSequence randomSequence);

    /**
     * Initializes and sets up the effect for display.
     */
    void start();

    /**
     * Hides / removes the effect from display.
     * Frees temporary resources.
     * Start can be called again after stop.
     */
    void stop();

    /**
     * Releases any resources held by this effect.  The effect can not be started again after shutdown.
     */
    void shutdown();

    /**
     * Render this effect for a frame.
     */
    void render(double timeSinceLastCall_seconds, ModelBatch modelBatch);

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

    <T> ParameterRange<T> getParameterRange(Symbol id);
}
