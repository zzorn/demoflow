package org.demoflow.parameter.range.ranges;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.parameter.range.RangeBase;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.Check.notNull;


/**
 * A range for 2D vectors.
 */
public final class Vector2Range extends RangeBase<Vector2> {

    private final float minX;
    private final float maxX;
    private final float minY;
    private final float maxY;
    private final float standardDeviation;

    public static final Vector2Range FULL = new Vector2Range();
    public static final Vector2Range MINUS_ONE_TO_ONE = new Vector2Range(-1, 1, 0.5f);
    public static final Vector2Range ZERO_TO_ONE = new Vector2Range(0, 1, 0.25f);

    public static final Vector2Range POSITIVE_X = new Vector2Range(0,                       Float.POSITIVE_INFINITY,
                                                                   Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector2Range POSITIVE_Y = new Vector2Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                                   0,                       Float.POSITIVE_INFINITY);
    public static final Vector2Range POSITIVE = new Vector2Range(0, Float.POSITIVE_INFINITY,
                                                                 0, Float.POSITIVE_INFINITY);
    public static final Vector2Range ZERO_X = new Vector2Range(0, 0,
                                                               Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final Vector2Range ZERO_Y = new Vector2Range(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
                                                               0, 0);


    public Vector2Range() {
        this(1f);
    }

    public Vector2Range(float standardDeviation) {
        this(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, standardDeviation);
    }

    public Vector2Range(float minValue, float maxValue) {
        this(minValue, maxValue, 1f);
    }

    public Vector2Range(float minValue, float maxValue, float standardDeviation) {
        this(minValue, maxValue,
             minValue, maxValue,
             standardDeviation);
    }

    public Vector2Range(float minX, float maxX,
                        float minY, float maxY) {
        this(minX, maxX, minY, maxY, 1f);
    }

    public Vector2Range(float minX, float maxX,
                        float minY, float maxY,
                        float standardDeviation) {
        super(Vector2.class);

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.standardDeviation = standardDeviation;
    }

    @Override public Vector2 clampToRange(Vector2 originalValue) {
        notNull(originalValue, "originalValue");
        originalValue.x = MathUtils.clampToRange(originalValue.x, minX, maxX);
        originalValue.y = MathUtils.clampToRange(originalValue.y, minY, maxY);

        return originalValue;
    }

    @Override protected Vector2 createRandomValue(RandomSequence randomSequence) {
        return new Vector2(randomCoordinate(randomSequence, minX, maxX),
                           randomCoordinate(randomSequence, minY, maxY));
    }

    @Override protected Vector2 doMutateValue(Vector2 vector, float mutationAmount, RandomSequence randomSequence) {
        vector.x += mutationAmount * standardDeviation * randomSequence.nextGaussianFloat();
        vector.y += mutationAmount * standardDeviation * randomSequence.nextGaussianFloat();
        return vector;
    }

    @Override protected Vector2 createCopy(Vector2 source) {
        return new Vector2(source);
    }

    @Override protected Vector2 createDefaultValue() {
        return new Vector2();
    }

    private float randomCoordinate(RandomSequence randomSequence, float min, float max) {
        if (Float.isInfinite(min) || Float.isInfinite(max)) {
            return randomSequence.nextGaussianFloat() * standardDeviation;
        }
        else {
            return randomSequence.nextFloat(min, max);
        }
    }

    @Override public Vector2 interpolate(double t, Vector2 a, Vector2 b, Vector2 out) {
        out.set(a);
        out.lerp(b, (float) t);
        return out;
    }
}
