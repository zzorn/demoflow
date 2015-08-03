package org.demoflow.effect;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.demoflow.view.View;

/**
 * Rendering context passed to Effects.
 */
public interface RenderContext {

    ModelBatch getModelBatch();

    View getView();

}
