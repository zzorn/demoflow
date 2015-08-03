package org.demoflow.effect.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.effect.RenderContext;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.effect.EffectBase;
import org.demoflow.parameter.range.ranges.ColorRange;
import org.demoflow.parameter.range.ranges.Vector3Range;
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
        // IDEA: Add quaternion for current direction?
    }

    @Override protected void doSetup(Object preCalculatedData, RandomSequence randomSequence) {
        // Create cube model and material, then create an instance of the cube
        ModelBuilder modelBuilder = new ModelBuilder();
        material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        model = modelBuilder.createBox(1f, 1f, 1f, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);

        // Materials are copied for model instances, get reference to the color attribute so we can change it easily
        diffuseColor = (ColorAttribute) modelInstance.materials.get(0).get(ColorAttribute.Diffuse);
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        // Nothing to update
    }

    @Override protected void doRender(RenderContext renderContext) {
        // Update cube appearance
        diffuseColor.color.set(color.get());
        modelInstance.transform.setToTranslationAndScaling(position.get(), scale.get());

        // Render cube
        renderContext.getModelBatch().render(modelInstance);
    }

    @Override protected void doShutdown() {
        model.dispose();
    }
}
