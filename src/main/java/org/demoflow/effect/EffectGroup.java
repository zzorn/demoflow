package org.demoflow.effect;

import com.badlogic.gdx.utils.Array;
import org.demoflow.demo.Demo;
import org.demoflow.node.DemoNode;
import org.demoflow.parameter.calculator.CalculationContext;
import org.demoflow.utils.ArrayUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.Check.notNull;

/**
 * Several effects grouped together.
 * The effect group may have custom parameters that the contained effects may use in their calculations.
 *
 * The contained effects are not updated or rendered outside the time boundaries of the effect group duration.
 */
public final class EffectGroup extends EffectBase<Object> implements EffectContainer {

    private final Array<Effect> effects = new Array<>();

    @Override public <E extends Effect> E addEffect(E effect) {
        notNull(effect, "effect");
        if (effects.contains(effect, true)) throw new IllegalArgumentException("The effect "+effect+" has already been added to this effect group");
        if (effect.getParent() != null) throw new IllegalArgumentException("The effect is already added to another parent ("+effect.getParent()+")");

        effects.add(effect);
        effect.setParent(this);

        notifyChildNodeAdded(effect);

        return effect;
    }

    @Override public final void removeEffect(Effect effect) {
        if (effect != null)
        if (effects.removeValue(effect, true)) {
            effect.setParent(null);

            notifyChildNodeRemoved(effect);
        }
    }

    @Override public final Array<Effect> getEffects() {
        return effects;
    }

    @Override public int getChildCount() {
        return getEffects().size + getParameters().size;
    }

    @Override public Array<? extends DemoNode> getChildren() {
        return ArrayUtils.combineArrays(getParameters(), getEffects());
    }

    @Override protected void doSetup(Object preCalculatedData, RandomSequence randomSequence) {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).setup(randomSequence.nextLong());
        }
    }

    @Override protected void doActivate() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).activate();
        }
    }

    @Override protected void doDeactivate() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).deactivate();
        }
    }

    @Override protected void doReset(long randomSeed) {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).reset(randomSeed);
        }
    }

    @Override protected void doShutdown() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).shutdown();
        }
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).update(calculationContext);
        }
    }

    @Override protected void doRender(RenderContext renderContext) {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).render(renderContext);
        }
    }

    @Override public void resetParametersToInitialValues() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).resetParametersToInitialValues();
        }
    }

    @Override public void setPaused(boolean paused) {
        for (Effect effect : effects) {
            effect.setPaused(paused);
        }
    }

    @Override public int getDepth() {
        final DemoNode parent = getParent();

        // Kludge for the effect group that is directly contained in a demo, to get the depth right.
        if (parent != null && parent instanceof Demo) {
            return parent.getDepth();
        }
        else return super.getDepth();
    }

}
