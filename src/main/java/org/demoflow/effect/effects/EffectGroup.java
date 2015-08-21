package org.demoflow.effect.effects;

import com.badlogic.gdx.utils.Array;
import nu.xom.Element;
import org.demoflow.DemoComponentManager;
import org.demoflow.demo.Demo;
import org.demoflow.effect.Effect;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.EffectContainer;
import org.demoflow.effect.RenderContext;
import org.demoflow.node.DemoNode;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.utils.ArrayUtils;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import java.io.IOException;
import java.util.List;

import static org.flowutils.Check.notNull;

/**
 * Several effects grouped together.
 * The effect group may have custom parameters that the contained effects may use in their calculations.
 *
 * The contained effects are not updated or rendered outside the time boundaries of the effect group duration.
 */
public final class EffectGroup extends EffectBase<Object> implements EffectContainer {

    private final Array<Effect> effects = new Array<>();
    private final Array<Effect> effectsToRemove = new Array<>();

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
        if (effect != null && !effectsToRemove.contains(effect, true)) {
            effectsToRemove.add(effect);
        }
    }

    @Override public final Array<Effect> getEffects() {
        return effects;
    }

    @Override public void moveEffect(Effect effect, int delta) {
        final int currentIndex = effects.indexOf(effect, true);
        if (currentIndex < 0) throw new IllegalArgumentException("No such effect found");

        effects.removeIndex(currentIndex);
        final int newIndex = MathUtils.clamp(currentIndex + delta, 0, effects.size);
        effects.insert(newIndex, effect);

        // TODO: Implement
        // notifyNodeMoved();
    }

    @Override public int indexOf(Effect effect) {
        return effects.indexOf(effect, true);
    }

    @Override public int getChildCount() {
        return getEffects().size + getParameters().size;
    }

    @Override public Array<? extends DemoNode> getChildren() {
        return ArrayUtils.combineArrays(getParameters(), getEffects());
    }

    @Override protected void doSetup(Object preCalculatedData) {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).setup();
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

    @Override protected void doReset() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).reset();
        }
    }

    @Override protected void doShutdown() {
        for (int i = 0; i < effects.size; i++) {
            effects.get(i).shutdown();
        }
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        if (effectsToRemove.size > 0) {
            for (Effect effectToRemove : effectsToRemove) {
                effectToRemove.deactivate();
                effectToRemove.shutdown();
                effectToRemove.setParent(null);
                notifyChildNodeRemoved(effectToRemove);
                effects.removeValue(effectToRemove, true);
            }
            effectsToRemove.clear();
        }

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
        super.resetParametersToInitialValues();

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

    @Override public Element toXmlElement() {
        final Element element = super.toXmlElement();

        // Create child elements for the effects of the demo
        final Element effects = new Element("effects");
        element.appendChild(effects);
        for (Effect effect : getEffects()) {
            effects.appendChild(effect.toXmlElement());
        }

        return element;
    }

    @Override public void fromXmlElement(Element element, DemoComponentManager typeManager) throws IOException {
        // Read parameters
        assignParameters(this, element, typeManager);

        // Read effects
        final List<Effect> effects = readEffects(element, typeManager);
        for (Effect effect : effects) {
            addEffect(effect);
        }
    }
}
