package org.demoflow.effect.ranges;

import com.badlogic.gdx.graphics.Color;
import org.flowutils.MathUtils;
import org.flowutils.random.RandomSequence;

import static org.flowutils.Check.notNull;

/**
 * A range for colors.
 */
public final class ColorRange implements ParameterRange<Color> {

    private final float minR;
    private final float maxR;
    private final float minG;
    private final float maxG;
    private final float minB;
    private final float maxB;
    private final float minA;
    private final float maxA;

    public static final ColorRange UNRESTRICTED = new ColorRange(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
    public static final ColorRange FULL = new ColorRange();
    public static final ColorRange ALL_OPAQUE_COLORS = new ColorRange(0f, 1f, 1f, 1f);
    public static final ColorRange BRIGHT_COLORS = new ColorRange(0.5f, 1f, 1f, 1f);
    public static final ColorRange DARK_COLORS = new ColorRange(0f, 0.5f, 1f, 1f);
    public static final ColorRange TRANSLUCENT_COLORS = new ColorRange(0f, 1f, 0f, 0.5f);
    public static final ColorRange RED_COLORS         = new ColorRange(0f, 1f, 0f, 0f, 0f, 0f, 0f, 1f);
    public static final ColorRange RED_SOLID_COLORS   = new ColorRange(0f, 1f, 0f, 0f, 0f, 0f, 1f, 1f);
    public static final ColorRange GREEN_COLORS       = new ColorRange(0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
    public static final ColorRange GREEN_SOLID_COLORS = new ColorRange(0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f);
    public static final ColorRange BLUE_COLORS        = new ColorRange(0f, 0f, 0f, 0f, 0f, 1f, 0f, 1f);
    public static final ColorRange BLUE_SOLID_COLORS  = new ColorRange(0f, 0f, 0f, 0f, 0f, 1f, 1f, 1f);
    public static final ColorRange PURPLE_COLORS      = new ColorRange(0f, 1f, 0f, 0f, 0f, 1f, 0f, 1f);
    public static final ColorRange PURPLE_SOLID_COLORS= new ColorRange(0f, 1f, 0f, 0f, 0f, 1f, 1f, 1f);

    public ColorRange() {
        this(0f, 1f);
    }

    public ColorRange(float minValue, float maxValue) {
        this(minValue, maxValue, minValue, maxValue, minValue, maxValue);
    }

    public ColorRange(float minValue, float maxValue, float minAlpha, float maxAlpha) {
        this(minValue, maxValue, minValue, maxValue, minValue, maxValue, minAlpha, maxAlpha);
    }

    public ColorRange(float minR, float maxR, float minG, float maxG, float minB, float maxB) {
        this(minR, maxR, minG, maxG, minB, maxB, 0f, 1f);
    }

    public ColorRange(float minR, float maxR, float minG, float maxG, float minB, float maxB, float minA, float maxA) {
        this.minR = minR;
        this.maxR = maxR;
        this.minG = minG;
        this.maxG = maxG;
        this.minB = minB;
        this.maxB = maxB;
        this.minA = minA;
        this.maxA = maxA;
    }

    @Override public Color clampToRange(Color originalValue) {
        originalValue.r = MathUtils.clampToRange(originalValue.r, minR, maxR);
        originalValue.g = MathUtils.clampToRange(originalValue.g, minG, maxG);
        originalValue.b = MathUtils.clampToRange(originalValue.b, minB, maxB);
        originalValue.a = MathUtils.clampToRange(originalValue.a, minA, maxA);

        return originalValue;
    }

    @Override public Color randomValue(RandomSequence randomSequence) {
        final Color color = new Color(randomSequence.nextFloat(minR, maxR),
                                      randomSequence.nextFloat(minG, maxG),
                                      randomSequence.nextFloat(minB, maxB),
                                      randomSequence.nextFloat(minA, maxA));
        return clampToRange(color);
    }

    @Override public Color mutateValue(Color color, float mutationAmount, RandomSequence randomSequence) {
        color.r += mutationAmount * randomSequence.nextFloat(-1f, 1f);
        color.g += mutationAmount * randomSequence.nextFloat(-1f, 1f);
        color.b += mutationAmount * randomSequence.nextFloat(-1f, 1f);
        color.a += mutationAmount * randomSequence.nextFloat(-1f, 1f);
        return clampToRange(color);
    }

    @Override public Color copy(Color source) {
        return new Color(source);
    }

    @Override public Color getDefaultValue() {
        return clampToRange(new Color(0.5f, 0.5f, 0.5f, 1f));
    }
}
