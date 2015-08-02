package org.demoflow;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;
import org.demoflow.effect.Effect;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;
import org.flowutils.service.ServiceBase;
import org.flowutils.service.ServiceProvider;

import java.util.HashMap;
import java.util.Map;

import static org.flowutils.Check.notNull;

/**
 * Keeps track of effects.
 */
public final class EffectService extends ServiceBase {

    private static final long DEFAULT_SEED = 42L;

    private final Array<Effect> effects = new Array<Effect>();
    private final View view;
    private final RandomSequence randomSequence;
    private final long preProcessSeed;
    private final long demoSeed;

    public EffectService(View view) {
        this(view, DEFAULT_SEED);
    }

    public EffectService(View view, long demoSeed) {
        this(view, new XorShift(), demoSeed);
    }

    public EffectService(View view, RandomSequence randomSequence, long demoSeed) {
        this(view, randomSequence, demoSeed, demoSeed);
    }

    public EffectService(View view, RandomSequence randomSequence, long demoSeed, long preProcessSeed) {
        notNull(view, "view");
        notNull(randomSequence, "randomSequence");

        this.view = view;
        this.randomSequence = randomSequence;
        this.demoSeed = demoSeed;
        this.preProcessSeed = preProcessSeed;
    }

    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    public void removeEffect(Effect effect) {
        effects.removeValue(effect, true);
    }


    @Override protected void doInit(ServiceProvider serviceProvider) {
        // Load or calculate pre-calculated values
        Map<Effect, Object> preCalculatedValues = getPreProcessValues();

        // Setup effects
        randomSequence.setSeed(demoSeed);
        for (Effect effect : effects) {
            effect.setup(view, preCalculatedValues.get(effect), randomSequence);

            // TODO: Later only start effect when it is scheduled to
            effect.start();
        }

    }

    private Map<Effect, Object> getPreProcessValues() {
        // TODO: Try to load from disk first, only preprocess if a preprocessing parameter changed or a preprocess was requested
        // TODO: Some unique IDs for effects, so that we can associate the correct preprocessed values with the correct effect instances.
        Map<Effect, Object> preCalculatedValues = preProcess();
        // TODO: Save preprocessed to disk
        return preCalculatedValues;
    }

    private Map<Effect, Object> preProcess() {
        // Preprocess
        Map<Effect, Object> preCalculatedValues = new HashMap<>();
        randomSequence.setSeed(preProcessSeed);
        for (Effect effect : effects) {
            final Object precalculated = effect.preCalculate(randomSequence);
            preCalculatedValues.put(effect, precalculated);
        }
        return preCalculatedValues;
    }

    @Override protected void doShutdown() {
        for (Effect effect : effects) {
            effect.stop();
            effect.shutdown();
        }
    }

    public void render(double timeSinceLastCall_seconds, ModelBatch modelBatch) {
        for (Effect effect : effects) {
            effect.render(timeSinceLastCall_seconds, modelBatch);
        }
    }
}
