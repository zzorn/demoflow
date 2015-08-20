package org.demoflow.effect;

import nu.xom.Element;
import org.demoflow.DemoComponentManager;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.Parametrized;
import org.demoflow.parameter.ParametrizedBase;
import org.flowutils.Check;
import org.flowutils.Symbol;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

import javax.swing.*;
import java.io.IOException;

import static org.flowutils.Check.notNull;

/**
 * Common functionality for effects.
 */
public abstract class EffectBase<P> extends ParametrizedBase implements Effect {

    private static final String ICON_PATH = "assets/icons/effects/";

    private String name;

    private RandomSequence randomSequence;
    private boolean active = false;
    private boolean initialized = false;
    private boolean shutdown = false;

    private P preCalculatedData = null;

    private double relativeStartTime = 0.0;
    private double relativeEndTime = 1.0;

    public EffectBase() {
        this.name = getClass().getSimpleName().replace("Effect", "");
    }

    @Override public final void setup() {
        if (initialized) throw new IllegalStateException("Setup can not be called if we are already initialized.");
        if (shutdown) throw new IllegalStateException("Setup can not be called after we have already called shutdown.");

        // Reset parameter values to initial values
        resetParametersToInitialValues();

        // Pre-calculate if needed
        if (preCalculatedData == null) {
            preCalculatedData = preCalculate();

            // Reset parameter values to initial values again in case pre calculation messed with them
            resetParametersToInitialValues();
        }

        // Allow subclass to do setup
        doSetup(preCalculatedData);

        initialized = true;
    }

    // Make setParent public for effects.
    @Override public void setParent(Parametrized parent) {
        super.setParent(parent);
    }

    @Override public String getName() {
        return name;
    }

    @Override public void setName(String name) {
        notNull(name, "name");
        this.name = name;
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
        if (active) throw new IllegalStateException(this + " is already started");

        active = true;

        if (initialized) {
            doActivate();
        }
    }

    @Override public final void update(CalculationContext calculationContext) {
        // Initialize the effect if it has not yet been initialized
        if (!initialized) {
            setup();

            if (active) {
                doActivate();
            }
        }

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

    @Override public final void reset() {
        if (shutdown) throw new IllegalStateException("Can not reset after a shutdown");

        if (initialized) {
            deactivate();
            doReset();
        }
    }

    @Override public final void shutdown() {
        if (shutdown) throw new IllegalStateException("Can not call shutdown twice, shutdown already called.");
        shutdown = true;

        if (initialized) {
            deactivate();
            doShutdown();
            initialized = false;
        }
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
    protected P preCalculate() {
        return null;
    }

    // Override as needed
    protected void doSetup(P preCalculatedData) {
    }

    /**
     * Rewinds this effect to the start.
     */
    protected abstract void doReset();

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

    @Override protected String getIconPath() {
        return ICON_PATH + getClass().getSimpleName() + ".png";
    }

    @Override public Element toXmlElement() {
        // Create effect element and add the common attributes
        Element effect = new Element("effect");
        addAttribute(effect, "name", getName());
        addAttribute(effect, "type", getClass().getSimpleName());
        addAttribute(effect, "relativeStartTime", relativeStartTime);
        addAttribute(effect, "relativeEndTime", relativeEndTime);

        // Create child elements for the parameters of the effect
        final Element parameters = new Element("parameters");
        effect.appendChild(parameters);
        for (Parameter parameter : getParameters()) {
            parameters.appendChild(parameter.toXmlElement());
        }

        return effect;
    }

    @Override public void fromXmlElement(Element element, DemoComponentManager typeManager) throws IOException {
        // Assign common attributes
        setName(element.getAttributeValue("name"));
        setEffectTimePeriod(
                getDoubleAttribute(element, "relativeStartTime", 0.0),
                getDoubleAttribute(element, "relativeEndTime", 1.0)
        );

        // Load effect parameters
        assignParameters(this, element, typeManager);
    }
}
