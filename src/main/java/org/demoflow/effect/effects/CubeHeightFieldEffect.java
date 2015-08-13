package org.demoflow.effect.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.ColorField;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.range.ranges.ColorRange;
import org.demoflow.parameter.range.ranges.IntRange;
import org.demoflow.parameter.range.ranges.Vector3Range;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * A heightfield of cubes, to test out heightfields.
 */
public final class CubeHeightFieldEffect extends EffectBase<Object> {

    public final Parameter<Color> color;
    public final Parameter<Vector3> cubeScale;
    public final Parameter<Integer> cubeCountX;
    public final Parameter<Integer> cubeCountZ;
    public final Parameter<Vector3> fieldCenter;
    public final Parameter<Vector3> fieldScale;
    public final Parameter<Vector2> samplingCenter;
    public final Parameter<Vector2> samplingScale;
    public final Parameter<Field> heightfield;
    public final Parameter<Field> sizefield;

    private Model model;
    private Material material;

    private final List<ModelInstance> cubes = new ArrayList<>();
    public Parameter<ColorField> colorField;


    public CubeHeightFieldEffect() {
        this(Color.GRAY,
             new Vector3(1,1,1),
             16,
             16,
             Vector3.Zero,
             new Vector3(10,10,10),
             new Vector2(0,0),
             new Vector2(1,1),
             null,
             null,
             null);
    }

    /**
     * @param color default color of a single cube.
     * @param cubeScale default scale of a single cube.
     * @param cubeCountX number of cubes in the field along the x axis.
     * @param cubeCountZ number of cubes in the field along the z axis.
     * @param fieldCenter center of the cube fields in the world.
     * @param fieldScale scale of the cube field in the world.
     * @param samplingCenter offset the coordinates used to sample the fields with.
     * @param samplingScale scale the coordinates used to sample the fields with.
     * @param heightfield field with cube y positions.
     * @param sizeField field with cube scalings.
     */
    public CubeHeightFieldEffect(final Color color,
                                 final Vector3 cubeScale,
                                 final int cubeCountX,
                                 final int cubeCountZ,
                                 final Vector3 fieldCenter,
                                 final Vector3 fieldScale,
                                 final Vector2 samplingCenter,
                                 final Vector2 samplingScale,
                                 final Field heightfield,
                                 final Field sizeField,
                                 final ColorField colorField) {
        this.color = addParameter("color", color, ColorRange.FULL);
        this.cubeScale = addParameter("cubeScale", cubeScale, Vector3Range.POSITIVE);
        this.cubeCountX = addParameter("cubeCountX", cubeCountX, IntRange.SMALL_NON_NEGATIVE);
        this.cubeCountZ = addParameter("cubeCountZ", cubeCountZ, IntRange.SMALL_NON_NEGATIVE);
        this.fieldCenter = addParameter("fieldCenter", fieldCenter, Vector3Range.FULL);
        this.fieldScale = addParameter("fieldScale", fieldScale, Vector3Range.FULL);
        this.samplingCenter = addParameter("samplingCenter", samplingCenter);
        this.samplingScale = addParameter("samplingScale", samplingScale);
        this.heightfield = addParameter("heightfield", heightfield);
        this.sizefield = addParameter("sizeField", sizeField);
        this.colorField = addParameter("colorField", colorField);
    }

    @Override protected void doSetup(Object preCalculatedData, RandomSequence randomSequence) {
        // Create cube model and material
        ModelBuilder modelBuilder = new ModelBuilder();
        material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        model = modelBuilder.createBox(1f, 1f, 1f, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    @Override protected void doReset(long randomSeed) {
        // No state to reset
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        // Nothing to update
    }

    @Override protected void doRender(RenderContext renderContext) {
        // Recreate cubes if needed
        final int cubeSizeX = cubeCountX.get();
        final int cubeSizeZ = cubeCountZ.get();
        final int cubeCount = cubeSizeX * cubeSizeZ;
        if (cubes.size() != cubeCount) {
            cubes.clear();
            for (int i = 0; i < cubeCount; i++) {
                cubes.add(new ModelInstance(model));
            }
        }

        // Render cubes
        final Vector3 defaultCubeScale = this.cubeScale.get();
        final Vector3 fieldScale = this.fieldScale.get();
        final Field heightField = heightfield.get();
        final Field sizeField = this.sizefield.get();
        final ColorField colorField = this.colorField.get();

        final Vector3 pos = new Vector3();
        final Vector3 cubeScale = new Vector3();
        final Color color = new Color();

        for (int z = 0; z < cubeSizeZ; z++) {
            for (int x = 0; x < cubeSizeX; x++) {

                // Get cube instance to update
                final ModelInstance cube = cubes.get(x + z * cubeSizeX);

                // Relative position of x and z in this loop
                final float relativeX = MathUtils.map(x, 0, cubeSizeX - 1, -1, 1);
                final float relativeZ = MathUtils.map(z, 0, cubeSizeZ - 1, -1, 1);

                // Calculate source position in fields
                final double fieldSourceX = samplingCenter.get().x + samplingScale.get().x * relativeX;
                final double fieldSourceY = samplingCenter.get().y + samplingScale.get().y * relativeZ;

                // Calculate position on screen
                final float heightFieldValue = heightField == null ? 0f : (float) heightField.get(fieldSourceX, fieldSourceY);
                pos.set(fieldCenter.get());
                pos.x += relativeX * fieldScale.x;
                pos.z += relativeZ * fieldScale.z;
                pos.y += heightFieldValue * fieldScale.y;

                // Calculate color
                if (colorField != null) {
                    colorField.get(fieldSourceX, fieldSourceY, color);
                }
                else {
                    color.set(this.color.get());
                }

                // Calculate cube scale
                cubeScale.set(defaultCubeScale);
                cubeScale.scl(sizeField == null ? 1f : (float) sizeField.get(fieldSourceX, fieldSourceY));

                // Update cube location and appearance
                cube.transform.setToTranslationAndScaling(pos, cubeScale);
                ((ColorAttribute)cube.materials.get(0).get(ColorAttribute.Diffuse)).color.set(color);

                // Queue cube for rendering
                renderContext.getModelBatch().render(cube);
            }
        }
    }

    @Override protected void doShutdown() {
        model.dispose();
    }
}
