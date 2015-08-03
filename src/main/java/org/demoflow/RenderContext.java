package org.demoflow;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 * Rendering context passed to Effects.
 */
public interface RenderContext {

    ModelBatch getModelBatch();

}
