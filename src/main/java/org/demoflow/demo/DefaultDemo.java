package org.demoflow.demo;

import com.badlogic.gdx.utils.Array;
import org.demoflow.effect.RenderContext;
import org.demoflow.view.View;
import org.demoflow.effect.EffectGroup;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.ParametrizedBase;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.parameter.calculator.DefaultCalculationContext;
import org.demoflow.effect.Effect;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.flowutils.Check;
import org.flowutils.Symbol;

import java.io.File;

import static org.flowutils.Check.notNull;

/**
 * Default implementation of Demo.
 * Add and configure effects to create the demo.
 */
public class DefaultDemo extends ParametrizedBase implements Demo {

    public static final double DEFAULT_DURATION_SECONDS = 60.0;
    public final Parameter<Double> timeDilation;

    private Array<DemoListener> listeners = new Array<>();

    private final EffectGroup effects = new EffectGroup();

    private View view;

    private boolean paused = false;
    private double speed = 1.0;
    private double durationSeconds = DEFAULT_DURATION_SECONDS;
    private double timeStepsPerSecond = 120;

    private long randomSeed;

    private double surplusTimeFromLastUpdate = 0;

    private final CalculationContext calculationContext = new DefaultCalculationContext();

    private boolean effectSetupRequested = false;
    private boolean effectShutdownRequested = false;
    private boolean initialized = false;

    public DefaultDemo() {
        this(DEFAULT_DURATION_SECONDS);
    }

    public DefaultDemo(double durationSeconds) {
        setDurationSeconds(durationSeconds);
        timeDilation = addParameter("timeDilation", 1.0, DoubleRange.POSITIVE);
    }

    @Override public final Array<Effect> getEffects() {
        return effects.getEffects();
    }

    @Override public final void addEffect(Effect effect) {
        notNull(effect, "effect");

        effects.addEffect(effect);
    }

    @Override public final void removeEffect(Effect effect) {
        effects.removeEffect(effect);
    }

    @Override public final void setPaused(boolean paused) {
        this.paused = paused;

        // Notify listeners
        for (DemoListener listener : listeners) {
            listener.onPauseChanged(this, this.paused);
        }
    }

    @Override public final boolean isPaused() {
        return paused;
    }

    @Override public final double getSpeed() {
        return speed;
    }

    @Override public final void setSpeed(double speed) {
        Check.positiveOrZero(speed, "speed");

        this.speed = speed;
    }

    @Override public final double getDurationSeconds() {
        return durationSeconds;
    }

    @Override public final void setDurationSeconds(double durationSeconds) {
        Check.positive(durationSeconds, "durationSeconds");

        this.durationSeconds = durationSeconds;
    }

    @Override public final long getRandomSeed() {
        return randomSeed;
    }

    @Override public final void setRandomSeed(long randomSeed) {
        this.randomSeed = randomSeed;
    }


    @Override public double getTimeStepsPerSecond() {
        return timeStepsPerSecond;
    }

    @Override public void setTimeStepsPerSecond(double timeStepsPerSecond) {
        Check.positive(timeStepsPerSecond, "timeStepsPerSecond");
        this.timeStepsPerSecond = timeStepsPerSecond;
    }

    @Override public final double getCurrentDemoTime() {
        return calculationContext.getSecondsFromDemoStart();
    }

    @Override public final double getCurrentDemoProgress() {
        return calculationContext.getRelativeDemoTime();
    }

    @Override public final View getView() {
        return view;
    }

    @Override public final void setView(View view) {
        this.view = view;
    }

    @Override public void setup() {
        if (view == null) throw new IllegalStateException("setView must be called before we can setup the demo");

        effectSetupRequested = true;
    }

    @Override public void shutdown() {
        effectShutdownRequested = true;
    }

    @Override public final void reset() {
        shutdown();
        setup();
    }

    @Override public void update(double deltaTime_s) {
        // Shut down if requested
        if (effectShutdownRequested) {
            effectShutdownRequested = false;
            doShutdown();
        }

        // Restart effects if necessary
        if (effectSetupRequested) {
            effectSetupRequested = false;
            doSetup();
        }

        // Update if we are initialized and the demo is ongoing
        if (initialized && calculationContext.getSecondsFromDemoEnd() >= 0) {
            double timeToAdd = deltaTime_s;

            if (!paused) {
                timeToAdd *= speed * timeDilation.get();

                double remainingUpdateTime = surplusTimeFromLastUpdate + timeToAdd;

                // Update the demo in fixed sized time steps
                double timeStepSizeSeconds = 1.0 / timeStepsPerSecond;
                while (remainingUpdateTime >= timeStepSizeSeconds) {
                    remainingUpdateTime -= timeStepSizeSeconds;

                    // Update effects
                    calculationContext.update(timeStepSizeSeconds);
                    effects.update(calculationContext);
                }

                surplusTimeFromLastUpdate = remainingUpdateTime;
            }

            // Notify listeners about progress
            for (int i = 0; i < listeners.size; i++) {
                listeners.get(i).onProgress(this,
                                            getCurrentDemoTime(),
                                            getDurationSeconds(),
                                            getCurrentDemoProgress());
            }

            // Check if demo finished
            if (calculationContext.getSecondsFromDemoEnd() < 0) {
                for (int i = 0; i < listeners.size; i++) {
                    listeners.get(i).onCompleted();
                }
            }
        }
    }

    private void doShutdown() {
        // Shut down effects
        effects.shutdown();

        initialized = false;

        // Notify listeners about shutdown
        for (DemoListener listener : listeners) {
            listener.onShutdown(this);
        }
    }

    private void doSetup() {
        surplusTimeFromLastUpdate = 0;
        calculationContext.init(durationSeconds);

        effects.setup(randomSeed);
        initialized = true;

        // Notify listeners about setup
        for (DemoListener listener : listeners) {
            listener.onSetup(this);
        }
    }

    @Override public void render(RenderContext renderContext) {
        if (initialized) {
            // Render effects
            effects.render(renderContext);
        }
    }

    @Override public void load(File demoFile) {
        // IMPLEMENT: Implement load
        // TODO: Maybe custom xml format with generic demo parameters, and then effects with their type and their parameter values
    }

    @Override public void save(File demoFile) {
        // IMPLEMENT: Implement save
    }

    @Override public void onParameterChanged(Parameter parameter, Symbol id, Object value) {
        // Nothing to do.
    }

    @Override public final void addListener(DemoListener listener) {
        notNull(listener, "listener");

        listeners.add(listener);
    }

    @Override public final void removeListener(DemoListener listener) {
        listeners.removeValue(listener, true);
    }
}
