package org.demoflow.effect;

import com.badlogic.gdx.utils.Array;

/**
 * Something that contains effects.
 */
public interface EffectContainer {

    /**
     * @param effect effect to add.
     * @return the added effect.
     */
    <E extends Effect> E addEffect(E effect);

    /**
     * @param effect the effect to remove.
     */
    void removeEffect(Effect effect);

    /**
     * @return the effects in this effect container.
     */
    Array<Effect> getEffects();
}
