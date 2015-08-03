package org.demoflow.effect;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.demoflow.view.View;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class DefaultRenderContext implements RenderContext {

    private final View view;
    private ModelBatch modelBatch;

    public DefaultRenderContext(View view, ModelBatch modelBatch) {
        notNull(view, "view");

        this.view = view;
        this.modelBatch = modelBatch;
    }

    @Override public View getView() {
        return view;
    }

    @Override public ModelBatch getModelBatch() {
        return modelBatch;
    }

    public void setModelBatch(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }
}
