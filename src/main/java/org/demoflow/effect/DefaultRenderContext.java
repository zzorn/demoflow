package org.demoflow.effect;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import org.demoflow.DemoServices;
import org.demoflow.view.View;

import static org.flowutils.Check.notNull;

/**
 *
 */
public class DefaultRenderContext implements RenderContext {

    private final View view;
    private ModelBatch modelBatch;
    private DemoServices demoServices;

    public DefaultRenderContext(View view, ModelBatch modelBatch, DemoServices demoServices) {
        notNull(view, "view");
        notNull(demoServices, "demoServices");


        this.view = view;
        this.modelBatch = modelBatch;
        this.demoServices = demoServices;
    }

    @Override public View getView() {
        return view;
    }

    @Override public ModelBatch getModelBatch() {
        return modelBatch;
    }

    @Override public DemoServices getDemoServices() {
        return demoServices;
    }

    public void setModelBatch(ModelBatch modelBatch) {
        this.modelBatch = modelBatch;
    }
}
