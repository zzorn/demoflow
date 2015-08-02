package org.demoflow.effect.ranges;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.Check.notNull;

/**
 * A range for 3D vectors.
 */
public final class Vector3Range implements ParameterRange<Vector3> {

    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;
    private final float minZ;
    private final float maxZ;
    private final float standardDeviation;

    public static final Vector3Range FULL = new Vector3Range();
    public static final Vector3Range MINUS_ONE_TO_ONE = new Vector3Range(-1, 1, 0.5f);
    public static final Vector3Range ZERO_TO_ONE = new Vector3Range(0, 1, 0.25f);

    public static final Vector3Range POSITIVE_X = new Vector3Range(0,                       Float.POSITIVE_INFINITY,
                                                                   Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                   Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector3Range POSITIVE_Y = new Vector3Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                   0,                       Float.POSITIVE_INFINITY,
                                                                   Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector3Range POSITIVE_Z = new Vector3Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                   Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                   0,                       Float.POSITIVE_INFINITY);
    public static final Vector3Range POSITIVE = new Vector3Range(0, Float.POSITIVE_INFINITY,
                                                                 0, Float.POSITIVE_INFINITY,
                                                                 0, Float.POSITIVE_INFINITY);
    public static final Vector3Range ZERO_X = new Vector3Range(0, 0,
                                                               Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                               Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector3Range ZERO_Y = new Vector3Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                               0, 0,
                                                               Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector3Range ZERO_Z = new Vector3Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                               Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                               0, 0);
    public static final Vector3Range ZERO_XY = new Vector3Range(0, 0,
                                                                0, 0,
                                                                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector3Range ZERO_YZ = new Vector3Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                0, 0,
                                                                0, 0);
    public static final Vector3Range ZERO_ZX = new Vector3Range(0, 0,
                                                                Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                0, 0);


    public Vector3Range() {
        this(1f);
    }

    public Vector3Range(float standardDeviation) {
        this(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, standardDeviation);
    }

    public Vector3Range(float minValue, float maxValue) {
        this(minValue, maxValue, 1f);
    }

    public Vector3Range(float minValue, float maxValue, float standardDeviation) {
        this(minValue, maxValue, minValue, maxValue, minValue, maxValue, standardDeviation);
    }

    public Vector3Range(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this(minX, maxX, minY, maxY, minZ, maxZ, 1f);
    }

    public Vector3Range(float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float standardDeviation) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.standardDeviation = standardDeviation;
    }

    @Override public Vector3 clampToRange(Vector3 originalValue) {
        originalValue.x = MathUtils.clampToRange(originalValue.x, minX, maxX);
        originalValue.y = MathUtils.clampToRange(originalValue.y, minY, maxY);
        originalValue.z = MathUtils.clampToRange(originalValue.z, minZ, maxZ);

        return originalValue;
    }

    @Override public Vector3 randomValue(RandomSequence randomSequence) {
        final Vector3 vector = new Vector3(randomCoordinate(randomSequence, minX, maxX),
                                           randomCoordinate(randomSequence, minY, maxY),
                                           randomCoordinate(randomSequence, minZ, maxZ));
        return clampToRange(vector);
    }

    @Override public Vector3 mutateValue(Vector3 vector, float mutationAmount, RandomSequence randomSequence) {
        vector.x += mutationAmount * standardDeviation * randomSequence.nextGaussianFloat();
        vector.y += mutationAmount * standardDeviation * randomSequence.nextGaussianFloat();
        vector.z += mutationAmount * standardDeviation * randomSequence.nextGaussianFloat();
        return clampToRange(vector);
    }

    @Override public Vector3 copy(Vector3 source) {
        return new Vector3(source);
    }

    @Override public Vector3 getDefaultValue() {
        return clampToRange(new Vector3());
    }

    private float randomCoordinate(RandomSequence randomSequence, float min, float max) {
        if (Float.isInfinite(min) || Float.isInfinite(max)) {
            return randomSequence.nextGaussianFloat() * standardDeviation;
        }
        else {
            return randomSequence.nextFloat(min, max);
        }
    }
}
