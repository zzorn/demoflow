package org.demoflow;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

/**
 *
 */
public class DefaultRenderContext implements RenderContext {

    private ModelBatch modelBatch;

    public DefaultRenderContext(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }

    @Override public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public void setModelBatch(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }
}
