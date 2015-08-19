package org.demoflow.effect;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.Parametrized;

/**
 * A demo effect.
 */
public interface Effect extends Parametrized {

    /**
     * @param name user readable name for this effect.  Does not need to be unique.
     */
    void setName(String name);

    /**
     * Load needed resources and do other setup.
     * This will only be called once.
     */
    // IDEA: Later listener system for progress?
    void setup();

    /**
     * Resets the position of this effect to the start.
     */
    void reset();

    /**
     * Releases any resources created by this effect in setup.
     * This will only be called once, the effect can not be restarted after this has been called.
     */
    void shutdown();

    /**
     * Activates the effect for display.
     */
    void activate();

    /**
     * Deactivates the effect and hides / removes the effect from display.
     * Frees temporary resources.
     * activate can be called again after deactivate.
     */
    void deactivate();

    /**
     * @return true if the effect is currently visible.
     */
    boolean isActive();

    /**
     * @return true if the effect is initialized and can be displayed.
     */
    boolean isInitialized();

    /**
     * Updates the effect state based on time.
     */
    void update(CalculationContext calculationContext);

    /**
     * Render this effect for a frame.
     */
    void render(RenderContext renderContext);


    /**
     * @return relative start time of the effect over the duration of the demo.  0 = start of demo, 1 = end of demo.
     */
    double getRelativeStartTime();

    /**
     * @return relative end time of the effect over the duration of the demo.  0 = start of demo, 1 = end of demo.
     */
    double getRelativeEndTime();

    /**
     * @return start time of the effect in seconds since the start of the demo.
     */
    double getEffectStartTime_s(double demoDuration_s);

    /**
     * @return end time of the effect in seconds since the start of the demo.
     */
    double getEffectEndTime_s(double demoDuration_s);

    /**
     * Sets the time period of the effect within the demo with relative times.
     *
     * @param relativeStartTime effect start time relative to the demo duration, 0 = start of demo, 1 = end of demo.
     * @param relativeEndTime effect end time relative to the demo duration, 0 = start of demo, 1 = end of demo.
     */
    void setEffectTimePeriod(double relativeStartTime, double relativeEndTime);

    /**
     * Sets the time period of the effect within the demo with absolute times.
     *
     * @param startTime_s start time of the effect in seconds after demo start.
     * @param endTime_s end time of the effect in seconds after demo start.
     * @param demoDuration_s duration of the demo in seconds.
     */
    void setEffectTimePeriod(double startTime_s, double endTime_s, double demoDuration_s);

    /**
     * @param parent the parametrized object (Effect or Demo) that this effect is contained in.
     */
    void setParent(Parametrized parent);


    /**
     * @param paused if true the demo is paused, and any background threads (e.g. music) should be paused,
     *               until it is resumed by calleding setPause(false).
     */
    void setPaused(boolean paused);
}
