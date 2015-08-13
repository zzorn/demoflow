package org.demoflow.particles.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import org.entityflow.component.ComponentBase;

/**
 * Appearance for particles without a model.
 */
public class ParticleAppearance extends ComponentBase {

    public final Color color = new Color(1f,1f,1f,1f);
    public float radius = 0.1f;

    // DEBUG: A model for now.  Needs to figure out how to do billboards with pure opengl
    public ModelInstance modelInstance;

}
