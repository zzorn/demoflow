package org.demoflow.effect;

import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.Parametrized;
import org.demoflow.parameter.ParametrizedBase;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 * Common functionality for effects.
 */
public abstract class EffectBase<P> extends ParametrizedBase implements Effect {

    private RandomSequence randomSequence;
    private boolean active = false;
    private boolean initialized = false;

    private P preCalculatedData = null;

    private double relativeStartTime = 0.0;
    private double relativeEndTime = 1.0;


    @Override public final void setup(long randomSeed) {
        if (initialized) throw new IllegalStateException("Setup can not be called if we are already initialized.  Call shutdown first.");

        this.randomSequence = new XorShift(randomSeed);

        // Reset parameter values to initial values
        resetParametersToInitialValues();

        // Pre-calculate if needed
        if (preCalculatedData == null) {
            preCalculatedData = preCalculate(new XorShift(randomSeed % 21983)); // (Avoid passing same random sequence to pre-calculation)

            // Reset parameter values to initial values again in case pre calculation messed with them
            resetParametersToInitialValues();
        }

        // Allow subclass to do setup
        doSetup(preCalculatedData, randomSequence);

        initialized = true;
    }

    // Make setParent public for effects.
    @Override public void setParent(Parametrized parent) {
        super.setParent(parent);
    }

    @Override public String getName() {
        return getClass().getSimpleName().replace("Effect", "");
    }

    @Override public final double getRelativeStartTime() {
        return relativeStartTime;
    }

    @Override public final double getRelativeEndTime() {
        return relativeEndTime;
    }

    @Override public final double getEffectStartTime_s(double demoDuration_s) {
        return relativeStartTime * demoDuration_s;
    }

    @Override public final double getEffectEndTime_s(double demoDuration_s) {
        return relativeEndTime * demoDuration_s;
    }

    @Override public final void setEffectTimePeriod(double relativeStartTime, double relativeEndTime) {
        Check.greaterOrEqual(relativeEndTime, "relativeEndTime", relativeStartTime, "relativeStartTime");

        this.relativeStartTime = relativeStartTime;
        this.relativeEndTime = relativeEndTime;
    }

    @Override public final void setEffectTimePeriod(double startTime_s,
                                                    double endTime_s,
                                                    double demoDuration_s) {
        Check.positive(demoDuration_s, "demoDuration_s");

        setEffectTimePeriod(startTime_s / demoDuration_s, endTime_s / demoDuration_s);
    }



    protected final RandomSequence getRandomSequence() {
        return randomSequence;
    }


    @Override public final boolean isActive() {
        return active;
    }

    @Override public final boolean isInitialized() {
        return initialized;
    }

    @Override public final void activate() {
        if (!initialized) throw new IllegalStateException("Setup should be called before activate");
        if (active) throw new IllegalStateException(this + " is already started");

        active = true;

        doActivate();
    }

    @Override public final void update(CalculationContext calculationContext) {
        // Activate effect when effect start time passed, deactivate effect when stop time passed
        updateEffectActivationState(calculationContext);

        if (active) {
            calculationContext.setEffectPeriod(relativeStartTime, relativeEndTime);

            // Calculate parameter values for the parameters with calculators
            recalculateParameters(calculationContext);

            // Update effect
            doUpdate(calculationContext);
        }
    }

    @Override public final void render(RenderContext renderContext) {
        if (active) {
            doRender(renderContext);
        }
    }

    @Override public final void deactivate() {
        if (active) {
            active = false;
            doDeactivate();
        }
    }

    @Override public final void shutdown() {
        if (initialized) {
            deactivate();
            doShutdown();
            initialized = false;
        }
    }

    /**
     * Called when a parameter is updated, can be used to listen to changes.
     * Automatically called for all parameters after startup has been called, to allow initialization of the effect.
     * Override if needed.
     */
    @Override public void onParameterChanged(Parameter parameter, Symbol id, Object value) {
    }

    /**
     * Do any logic updates here.
     */
    protected abstract void doUpdate(CalculationContext calculationContext);

    /**
     * Render the effect here.
     */
    protected abstract void doRender(RenderContext renderContext);

    /**
     * Does long running pre-calculations for the effect, that may be serialized and saved to disk.
     * E.g. texture generation, genetic algorithm running, etc.
     * Override if needed.
     * @return precalculated data to pass to setup, or null if no data is precalculated.
     */
    protected P preCalculate(RandomSequence randomSequence) {
        return null;
    }

    // Override as needed
    protected void doSetup(P preCalculatedData, RandomSequence randomSequence) {
    }

    // Override as needed
    protected void doActivate() {
    }

    // Override as needed
    protected void doDeactivate() {
    }

    // Override as needed
    protected void doShutdown() {
    }

    private void updateEffectActivationState(CalculationContext calculationContext) {
        final double relativeDemoTime = calculationContext.getRelativeDemoTime();
        if (relativeDemoTime >= relativeStartTime &&
            relativeDemoTime < relativeEndTime) {
            // The effect time is now, we should activate the effect
            if (!active) activate();
        }
        else {
            // The effect time is not now, we should deactivate the effect
            if (active) deactivate();
        }
    }


    // Override if the effect needs to pause some ongoing thread or similar (e.g. music)
    @Override public void setPaused(boolean paused) {
        // Ignored for most effects
    }
}
