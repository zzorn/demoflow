package org.demoflow.particles.components;

import com.badlogic.gdx.math.Vector3;
import org.entityflow.component.ComponentBase;

/**
 *
 */
public class Positioned extends ComponentBase {

    /**
     * Position of the entity.
     */
    public final Vector3 pos = new Vector3();

    public Positioned(Vector3 pos) {
        this.pos.set(pos);
    }
}
