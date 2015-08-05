package org.demoflow.demo;

import com.badlogic.gdx.utils.Array;
import org.demoflow.effect.EffectContainer;
import org.demoflow.effect.RenderContext;
import org.demoflow.view.View;
import org.demoflow.parameter.Parametrized;
import org.demoflow.effect.Effect;

import java.io.File;

/**
 * Contains effects with start and stop times spread out over the duration of the Demo.
 * Has a duration, effects are forced to stop before the duration ends and start after time 0.
 */
public interface Demo extends Parametrized, EffectContainer {

    /**
     * Adds an effect to this demo.
     *
     * @param effect effect to add.
     * @return the added effect.
     */
    <E extends Effect> E addEffect(E effect);

    /**
     * @param effect effect to remove from the demo.
     */
    void removeEffect(Effect effect);

    /**
     * @return the effects in this demo.
     */
    Array<Effect> getEffects();

    /**
     * Pause or unpause the execution of the demo.
     * @param paused if true, the demo will not update over time, but will render with the current parameters.
     */
    void setPaused(boolean paused);

    /**
     * @return if true, the demo will not update over time, but will render with the current parameters.
     */
    boolean isPaused();

    /**
     * @return current speed of the demo.  1 = normal speed, 0 = no speed, 2 = double speed, etc.
     */
    double getSpeed();

    /**
     * @param speed the speed of the demo.  1 = normal speed, 0 = no speed, 2 = double speed, etc.
     */
    void setSpeed(double speed);


    /**
     * @return nominal duration of the demo in seconds.
     *         Time dilation effects may change this in practice.
     */
    double getDurationSeconds();

    /**
     * @param duration_seconds nominal duration of the demo in seconds.
     *                         Time dilation effects may change this in practice.
     */
    void setDurationSeconds(double duration_seconds);

    /**
     * @return random seed used to initialize the random number generators on startup.
     */
    long getRandomSeed();

    /**
     * @param randomSeed random seed used to initialize the random number generators on startup.
     */
    void setRandomSeed(long randomSeed);

    /**
     * @return number of update steps to do each second.
     */
    double getTimeStepsPerSecond();

    /**
     * @param timeStepsPerSecond number of update steps to do each second.
     */
    void setTimeStepsPerSecond(double timeStepsPerSecond);

    /**
     * @return current nominal time in the demo.
     *         Time dilation effects may change this in practice.
     */
    double getCurrentDemoTime();

    /**
     * @return relative progress of the demo, 0 = at start, 1 = at end.
     */
    double getCurrentDemoProgress();

    /**
     * @return the view to show the demo on.
     */
    View getView();

    /**
     * @param view the view to show the demo on.
     */
    void setView(View view);

    /**
     * Initializes the demo and the effects used.
     */
    void setup();

    /**
     * Shuts down the effects and the demo.
     * Maybe restarted later.
     */
    void shutdown();

    /**
     * Shuts down the demo and initializes it to the start state.
     */
    void reset();

    /**
     * Update the demo state based on time.
     * @param deltaTime_s number of seconds since the last call to update.
     */
    void update(double deltaTime_s);

    /**
     * Render the current frame of the demo.
     * @param renderContext context to render to.
     */
    void render(RenderContext renderContext);

    /**
     * Loads information on what effects to use and what parameters and parameter calculators to use for them.
     * @param demoFile file to load from.
     */
    void load(File demoFile);

    /**
     * Save information on what effects to use and what parameters and parameter calculators to use for them.
     * @param demoFile file to save to.
     */
    void save(File demoFile);

    /**
     * @param listener listener that gets notified about demo progress and state changes.
     */
    void addListener(DemoListener listener);

    /**
     * @param listener listener to remove.
     */
    void removeListener(DemoListener listener);

    /**
     * @return if true the demo will automatically restart when finished.
     */
    boolean isAutoRestart();

    /**
     * @param autoRestart if true the demo will automatically restart when finished.
     */
    void setAutoRestart(boolean autoRestart);
}
