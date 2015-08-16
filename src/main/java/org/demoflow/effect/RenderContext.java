package org.demoflow.effect;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.demoflow.view.View;

/**
 * Rendering context passed to Effects.
 */
public interface RenderContext {

    /**
     * @return used to batch render 3D models (takes care of depth-sorting, minimizing number of texture switches, etc).
     */
    ModelBatch getModelBatch();

    /**
     * @return allows access to the camera and other basic view settings.
     */
    View getView();

}
