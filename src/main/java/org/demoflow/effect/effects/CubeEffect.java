package org.demoflow.effect.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.View;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.ranges.ColorRange;
import org.demoflow.effect.ranges.Vector3Range;
import org.flowutils.Symbol;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 * A simple rotating cube effect.
 */
public class CubeEffect extends EffectBase<Object> {

    public static final Symbol COLOR = Symbol.get("color");
    public static final Symbol SCALE = Symbol.get("scale");
    public static final Symbol POSITION = Symbol.get("position");

    private Model model;
    private ModelInstance modelInstance;
    private Material material;
    private ColorAttribute diffuseColor;

    public CubeEffect() {
        addParameter(COLOR, new Color(0.5f, 0.5f, 0.5f, 1f), ColorRange.FULL);
        addParameter(SCALE, new Vector3(1, 1, 1), Vector3Range.POSITIVE);
        addParameter(POSITION, new Vector3(0, 0, 0), Vector3Range.FULL);
    }

    @Override protected void doSetup(View view, Object preCalculatedData, RandomSequence randomSequence) {
        ModelBuilder modelBuilder = new ModelBuilder();
        material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        model = modelBuilder.createBox(1f, 1f, 1f, material,
                                       VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);

        // Materials are copied for model instances, get reference to the color attribute so we can change it easily
        diffuseColor = (ColorAttribute) modelInstance.materials.get(0).get(ColorAttribute.Diffuse);
    }

    @Override protected void onParameterUpdated(Symbol id, Object oldValue, Object newValue) {
        if (id == COLOR) {
            diffuseColor.color.set((Color) newValue);
        }
        else if (id == POSITION || id == SCALE) {
            modelInstance.transform.setToTranslationAndScaling(getVector(POSITION), getVector(SCALE));
        }
    }

    @Override protected void doUpdate(double timeSinceLastCall_seconds) {
    }

    @Override protected void doRender(View view, ModelBatch modelBatch) {
        modelBatch.render(modelInstance);
    }

    @Override protected void doShutdown(View view) {
        model.dispose();
    }
}
