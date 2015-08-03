package org.demoflow.effect.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.RenderContext;
import org.demoflow.View;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.animation.Parameter;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.ranges.ColorRange;
import org.demoflow.effect.ranges.Vector3Range;
import org.flowutils.random.RandomSequence;

/**
 * A simple rotating cube effect.
 */
public final class CubeEffect extends EffectBase<Object> {

    public final Parameter<Color> color;
    public final Parameter<Vector3> scale;
    public final Parameter<Vector3> position;

    private Model model;
    private ModelInstance modelInstance;
    private Material material;
    private ColorAttribute diffuseColor;

    public CubeEffect() {
        this(Color.GRAY, new Vector3(1,1,1), Vector3.Zero);
    }

    public CubeEffect(final Color color, final Vector3 scale, final Vector3 position) {
        this.color = addParameter("color", color, ColorRange.FULL);
        this.scale = addParameter("scale", scale, Vector3Range.POSITIVE);
        this.position = addParameter("position", position, Vector3Range.FULL);
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

    @Override protected void doUpdate(CalculationContext calculationContext) {
        // TODO: Spin it?
    }

    @Override protected void doRender(View view, RenderContext renderContext) {
        diffuseColor.color.set(color.get());
        modelInstance.transform.setToTranslationAndScaling(position.get(), scale.get());

        renderContext.getModelBatch().render(modelInstance);
    }

    @Override protected void doShutdown(View view) {
        model.dispose();
    }
}
