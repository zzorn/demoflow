package org.demoflow.effect;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.View;
import org.demoflow.effect.ranges.ParameterRange;
import org.flowutils.Symbol;
import org.flowutils.random.RandomSequence;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for effects.
 */
public abstract class EffectBase<P> implements Effect<P> {

    private final Map<Symbol, ParameterRange> parameterRanges = new LinkedHashMap<>();
    private final ConcurrentMap<Symbol, Object> parameters = new ConcurrentHashMap<>();
    private RandomSequence randomSequence;
    private View view;
    private boolean started = false;
    private boolean setupCalled = false;
    private boolean shutdownCalled = false;

    protected final <T> void addParameter(Symbol id, T initialValue, ParameterRange<T> range) {
        parameterRanges.put(id, range);
        parameters.put(id, initialValue);
    }

    @Override public final Map<Symbol, ParameterRange> getParameterRanges() {
        return parameterRanges;
    }

    @Override public final Map<Symbol, Object> getParameterValues() {
        return parameters;
    }

    @Override public final void updateParameters(Map<Symbol, Object> parametersToUpdate) {
        for (Map.Entry<Symbol, Object> entry : parametersToUpdate.entrySet()) {
            setParameter(entry.getKey(), entry.getValue());
        }
    }

    @Override public final P preCalculate(RandomSequence preCalculationRandomness) {
        return doPreCalculate(preCalculationRandomness);
    }

    @Override public final void setup(View view, P preCalculatedData, RandomSequence randomSequence) {
        notNull(view, "view");
        notNull(randomSequence, "randomSequence");

        this.view = view;
        this.randomSequence = randomSequence;

        doSetup(view, preCalculatedData, randomSequence);

        setupCalled = true;

        // Notify effect about parameter values
        for (Map.Entry<Symbol, Object> entry : parameters.entrySet()) {
            onParameterUpdated(entry.getKey(), null, entry.getValue());
        }
    }

    protected final View getView() {
        return view;
    }

    protected final RandomSequence getRandomSequence() {
        return randomSequence;
    }

    @Override public final void start() {
        if (started) throw new IllegalStateException(this + " is already started");
        if (!setupCalled) throw new IllegalStateException("Setup should be called before start");
        if (shutdownCalled) throw new IllegalStateException("Start can not be called after shutdown has been called");

        started = true;

        doStart(view);
    }

    @Override public final void render(double timeSinceLastCall_seconds, ModelBatch modelBatch) {
        if (!started) throw new IllegalStateException("Effect should be started to render");

        doUpdate(timeSinceLastCall_seconds);

        doRender(view, modelBatch);
    }

    @Override public final void stop() {
        if (started) {
            started = false;
            doStop(view);
        }
    }

    @Override public final void shutdown() {
        if (!shutdownCalled) {
            stop();
            doShutdown(view);
            shutdownCalled = true;
        }
    }

    @Override public final void setParameter(Symbol id, Object value) {
        // Ensure the parameter exists:
        getParameterRange(id);

        // Update it
        final Object oldValue = parameters.put(id, value);

        // Notify about change
        if (setupCalled && !shutdownCalled) {
            onParameterUpdated(id, oldValue, value);
        }
    }

    @Override public final <T> T getParameter(Symbol id) {
        return (T) parameters.get(id);
    }

    /**
     * @return a boolean parameter with the specified id.
     */
    protected final boolean getBoolean(Symbol id) {
        return (Boolean) parameters.get(id);
    }

    /**
     * @return an integer parameter with the specified id.
     */
    protected final int getInt(Symbol id) {
        return (Integer) parameters.get(id);
    }

    /**
     * @return a float parameter with the specified id.
     */
    protected final float getFloat(Symbol id) {
        return (Float) parameters.get(id);
    }

    /**
     * @return a 3d vector parameter with the specified id.
     */
    protected final Vector3 getVector(Symbol id) {
        return (Vector3) parameters.get(id);
    }

    /**
     * @return a quaternion parameter with the specified id.
     */
    protected final Quaternion getQuaternion(Symbol id) {
        return (Quaternion) parameters.get(id);
    }

    /**
     * @return a color parameter with the specified id.
     */
    protected final Color getColor(Symbol id) {
        return (Color) parameters.get(id);
    }

    /**
     * Writes the 3d vector parameter with the specified id to the output vector.
     */
    protected final void getVector(Symbol id, Vector3 out) {
        out.set((Vector3) parameters.get(id));
    }

    /**
     * Writes the quaternion parameter with the specified id to the output quaternion.
     */
    protected final void getQuaternion(Symbol id, Quaternion out) {
        out.set((Quaternion) parameters.get(id));
    }

    /**
     * Writes the color parameter with the specified id to the output color.
     */
    protected final void getColor(Symbol id, Color out) {
        out.set((Color) parameters.get(id));
    }

    /**
     * @return the range of the specified parameter.
     */
    @Override public final <T> ParameterRange<T> getParameterRange(Symbol id) {
        ParameterRange parameterRange = parameterRanges.get(id);
        if (parameterRange == null) {
            throw new IllegalArgumentException("No parameter with the id '"+id+"'");
        }
        return parameterRange;
    }

    // Override as needed
    protected P doPreCalculate(RandomSequence randomSequence) {
        return null;
    }

    // Override as needed
    protected void doSetup(View view, P preCalculatedData, RandomSequence randomSequence) {
    }

    /**
     * Called when a parameter is updated, can be used to listen to changes.
     * Only called for parameter changes after setup has been called.
     * Automatically called for all parameters after startup has been called, to allow initialization of the effect.
     */
    protected void onParameterUpdated(Symbol id, Object oldValue, Object newValue) {
    }

    // Override as needed
    protected void doStart(View view) {
    }

    /**
     * Do any logic updates here.
     * @param timeSinceLastCall_seconds seconds since the last call to doUpdate.
     */
    protected abstract void doUpdate(double timeSinceLastCall_seconds);

    protected abstract void doRender(View view, ModelBatch modelBatch);

    // Override as needed
    protected void doStop(View view) {
    }

    // Override as needed
    protected void doShutdown(View view) {
    }


}
