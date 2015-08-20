package org.demoflow.demo;

import com.badlogic.gdx.utils.Array;
import nu.xom.*;
import org.demoflow.DemoComponentManager;
import org.demoflow.effect.EffectContainer;
import org.demoflow.effect.RenderContext;
import org.demoflow.node.DemoNode;
import org.demoflow.node.DemoNodeListenerAdapter;
import org.demoflow.utils.ArrayUtils;
import org.demoflow.utils.uiutils.timebar.TimeBarModel;
import org.demoflow.view.View;
import org.demoflow.effect.effects.EffectGroup;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.ParametrizedBase;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.DefaultCalculationContext;
import org.demoflow.effect.Effect;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.flowutils.Check;
import org.flowutils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Default implementation of Demo.
 * Add and configure effects to create the demo.
 */
public class DefaultDemo extends ParametrizedBase implements Demo, EffectContainer {

    public static final double DEFAULT_DURATION_SECONDS = 60.0;
    private static final int DEFAULT_TIMESTEPS_PER_SECOND = 120;
    public final Parameter<Double> timeDilation;


    private Array<DemoListener> listeners = new Array<>();

    private final EffectGroup effects;

    private View view;

    private String name;

    private boolean paused = false;
    private double speed = 1.0;
    private double durationSeconds = DEFAULT_DURATION_SECONDS;
    private double timeStepsPerSecond = DEFAULT_TIMESTEPS_PER_SECOND;

    private boolean autoRestart = false;

    private long randomSeed;

    private double surplusTimeFromLastUpdate = 0;

    private final CalculationContext calculationContext = new DefaultCalculationContext();

    private final DefaultCalculationContext undilatedTime = new DefaultCalculationContext();

    private boolean effectSetupRequested = false;
    private boolean effectShutdownRequested = false;
    private boolean effectRestartRequested = false;
    private boolean initialized = false;
    private boolean shutdown = false;

    private TimeBarModel timeBarModel = new TimeBarModel(1);

    public DefaultDemo() {
        this("Demo");
    }

    public DefaultDemo(String name) {
        this(name, DEFAULT_DURATION_SECONDS);
    }

    public DefaultDemo(String name, double durationSeconds) {
        Check.nonEmptyString(name, "name");

        this.name = name;

        effects = new EffectGroup();
        effects.setParent(this);
        effects.addNodeListener(new DemoNodeListenerAdapter() {
            @Override public void onChildAdded(DemoNode parent, DemoNode child) {
                notifyChildNodeAdded(child);
            }

            @Override public void onChildRemoved(DemoNode parent, DemoNode child) {
                notifyChildNodeRemoved(child);
            }

            @Override public void onNodeUpdated(DemoNode node) {
                notifyNodeUpdated();
            }
        });

        setDurationSeconds(durationSeconds);
        timeDilation = addParameter("timeDilation", 1.0, DoubleRange.POSITIVE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name, "name");
        this.name = name;
    }

    @Override public final Array<Effect> getEffects() {
        return effects.getEffects();
    }

    @Override public <E extends Effect> E addEffect(E effect) {
        notNull(effect, "effect");

        effects.addEffect(effect);

        return effect;
    }

    @Override public final void removeEffect(Effect effect) {
        effects.removeEffect(effect);
    }

    @Override public void moveEffect(Effect effect, int delta) {
        effects.moveEffect(effect, delta);
    }

    @Override public int indexOf(Effect effect) {
        return effects.indexOf(effect);
    }

    @Override public final void setPaused(boolean paused) {
        if (this.paused != paused) {
            this.paused = paused;

            // Notify effects
            effects.setPaused(paused);

            // Notify listeners
            for (DemoListener listener : listeners) {
                listener.onPauseChanged(this, this.paused);
            }
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
        timeBarModel.setDuration(durationSeconds);
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
        if (shutdown) throw new IllegalStateException("We can't setup a demo after it has been shut down.");

        effectSetupRequested = true;
    }

    @Override public TimeBarModel getTimeBarModel() {
        return timeBarModel;
    }

    @Override public final void reset() {
        if (view == null) throw new IllegalStateException("setView must be called before we can reset the demo");
        if (shutdown) throw new IllegalStateException("We can't restart a demo after it has been shut down.");

        effectRestartRequested = true;
    }

    @Override public void shutdown() {
        if (!shutdown) {
            effectShutdownRequested = true;
        }
    }

    @Override public void update(double deltaTime_s) {
        // Setup effects if necessary
        if (effectSetupRequested) {
            effectSetupRequested = false;
            doSetup();
        }

        // Restart effects if necessary
        if (effectRestartRequested) {
            effectRestartRequested = false;
            doRestart();
        }

        // Shut down if requested
        if (effectShutdownRequested) {
            effectShutdownRequested = false;
            doShutdown();
        }

        // Update if we are initialized and the demo is ongoing
        if (initialized && calculationContext.getSecondsFromDemoEnd() >= 0) {
            double timeToAdd = deltaTime_s;

            if (!paused) {
                // Update undilated time, to allow calculation of time dilation value
                undilatedTime.update(deltaTime_s);
                timeDilation.recalculateParameter(undilatedTime);

                timeToAdd *= speed * timeDilation.get();

                double remainingUpdateTime = surplusTimeFromLastUpdate + timeToAdd;

                // Update the demo in fixed sized time steps
                double timeStepSizeSeconds = 1.0 / timeStepsPerSecond;
                while (remainingUpdateTime >= timeStepSizeSeconds) {
                    remainingUpdateTime -= timeStepSizeSeconds;

                    // Update time
                    calculationContext.update(timeStepSizeSeconds);
                    timeBarModel.setCurrentTime(calculationContext.getRelativeDemoTime());

                    // Update parameters in the demo itself
                    recalculateParameters(calculationContext);

                    // Update effects
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

                // Restart if auto-restart is on
                if (autoRestart) {
                    reset();
                }
            }
        }
    }

    private void doSetup() {
        effects.setup();
        initialized = true;

        // Notify listeners about setup
        for (DemoListener listener : listeners) {
            listener.onSetup(this);
        }

        // Reset demo state to beginning
        doRestart();
    }

    private void doRestart() {
        surplusTimeFromLastUpdate = 0;
        calculationContext.init(durationSeconds);
        undilatedTime.init(durationSeconds);
        timeBarModel.setCurrentTime(calculationContext.getSecondsFromDemoStart());

        effects.reset();

        // Notify listeners about restart
        for (DemoListener listener : listeners) {
            listener.onRestart(this);
        }
    }

    private void doShutdown() {
        // Shut down effects
        effects.shutdown();

        initialized = false;
        shutdown = true;

        // Notify listeners about shutdown
        for (DemoListener listener : listeners) {
            listener.onShutdown(this);
        }
    }

    @Override public void render(RenderContext renderContext) {
        if (initialized) {
            // Render effects
            effects.render(renderContext);
        }
    }

    @Override public final void addListener(DemoListener listener) {
        notNull(listener, "listener");

        listeners.add(listener);
    }

    @Override public final void removeListener(DemoListener listener) {
        listeners.removeValue(listener, true);
    }

    @Override public boolean isAutoRestart() {
        return autoRestart;
    }

    @Override public void setAutoRestart(boolean autoRestart) {
        this.autoRestart = autoRestart;
    }

    @Override public int getChildCount() {
        return getParameters().size + effects.getChildCount();
    }

    @Override public Array<? extends DemoNode> getChildren() {
        return ArrayUtils.combineArrays(getParameters(), getEffects());
    }

    @Override public void load(File demoFile, DemoComponentManager typeManager) throws IOException {
        loadFromXml(FileUtils.readFile(demoFile), typeManager);
    }

    @Override public void save(File demoFile) throws IOException {
        FileUtils.saveAndCheck(generateXml(), demoFile);
    }

    @Override public void loadFromXml(String xmlText, DemoComponentManager typeManager) throws IOException {
        try {
            Builder parser = new Builder();
            Document document = parser.build(new StringReader(xmlText));
            fromXmlElement(document.getRootElement(), typeManager);
        } catch (ParsingException e) {
            throw new IOException("Could not parse the demo: " + e.getMessage(), e);
        }
    }

    @Override public String generateXml() throws IOException {
        // Generate xml document
        final Document document = new Document(toXmlElement());

        // Pretty print the document with indent
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Serializer serializer = new Serializer(outputStream);
        serializer.setIndent(4);
        serializer.write(document);

        return outputStream.toString();
    }

    @Override public void fromXmlElement(Element element, DemoComponentManager typeManager) throws IOException {
        // Get attributes
        setName(element.getAttributeValue("name"));
        setRandomSeed(getLongAttribute(element, "randomSeed", 0));
        setDurationSeconds(getDoubleAttribute(element, "duration", DEFAULT_DURATION_SECONDS));
        setSpeed(getDoubleAttribute(element, "speed", 1.0));
        setTimeStepsPerSecond(getDoubleAttribute(element, "timeStepsPerSecond", DEFAULT_TIMESTEPS_PER_SECOND));

        // Assign parameter values
        assignParameters(this, element, typeManager);

        // Assign effects
        final List<Effect> effects = readEffects(element, typeManager);
        for (Effect effect : effects) {
            addEffect(effect);
        }
    }

    @Override public Element toXmlElement() {
        // Create demo element and the attributes
        Element demo = new Element("demo");
        addAttribute(demo, "name", getName());
        addAttribute(demo, "randomSeed", getRandomSeed());
        addAttribute(demo, "duration", getDurationSeconds());
        addAttribute(demo, "speed", getSpeed());
        addAttribute(demo, "timeStepsPerSecond", getTimeStepsPerSecond());

        // Create child elements for the parameters of the demo
        final Element parameters = new Element("parameters");
        demo.appendChild(parameters);
        for (Parameter parameter : getParameters()) {
            parameters.appendChild(parameter.toXmlElement());
        }

        // Create child elements for the effects of the demo
        final Element effects = new Element("effects");
        demo.appendChild(effects);
        for (Effect effect : getEffects()) {
            effects.appendChild(effect.toXmlElement());
        }

        return demo;
    }



}
