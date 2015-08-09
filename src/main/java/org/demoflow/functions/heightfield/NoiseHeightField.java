package org.demoflow.functions.heightfield;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.flowutils.SimplexGradientNoise;

import static org.flowutils.Check.notNull;

/**
 * Heightfield that uses a simplex noise.  Provides gradient as well.
 * The default noise frequency is 1 and the default amplitude is between -1 and 1.
 */
public final class NoiseHeightField implements HeightField {

    private final double[] tempGradient = new double[2];

    private double xScale = 1;
    private double zScale = 1;
    private double yScale = 1;
    private double xOffset = 0;
    private double yOffset = 0;
    private double zOffset = 0;

    /**
     * Creates a noise based height field with a height variation between -1 and 1, and a noise frequency of 1.
     */
    public NoiseHeightField() {
        this(1, 1, 1);
    }

    /**
     * @param xScale Scaling of the noise in the x direction.
     * @param yScale Scaling of the noise in the y direction.
     * @param zScale Scaling of the noise in the z direction.
     */
    public NoiseHeightField(double xScale, double yScale, double zScale) {
        this(xScale, yScale, zScale, 0, 0, 0);
    }

    /**
     * @param xScale Scaling of the noise in the x direction.
     * @param yScale Scaling of the noise in the y direction.
     * @param zScale Scaling of the noise in the z direction.
     * @param xOffset Offset of the noise in the x direction.
     * @param yOffset Offset of the noise in the y direction.
     * @param zOffset Offset of the noise in the z direction.
     */
    public NoiseHeightField(double xScale,
                            double yScale,
                            double zScale,
                            double xOffset,
                            double yOffset,
                            double zOffset) {
        this.xScale = xScale;
        this.zScale = zScale;
        this.yScale = yScale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public double getXScale() {
        return xScale;
    }

    public void setXScale(double xScale) {
        this.xScale = xScale;
    }

    public double getZScale() {
        return zScale;
    }

    public void setZScale(double zScale) {
        this.zScale = zScale;
    }

    public double getYScale() {
        return yScale;
    }

    public void setYScale(double yScale) {
        this.yScale = yScale;
    }

    public double getXOffset() {
        return xOffset;
    }

    public void setXOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    public double getYOffset() {
        return yOffset;
    }

    public void setYOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    public double getZOffset() {
        return zOffset;
    }

    public void setZOffset(double zOffset) {
        this.zOffset = zOffset;
    }

    public void setScale(Vector3 scale) {
        notNull(scale, "scale");

        setXScale(scale.x);
        setYScale(scale.y);
        setZScale(scale.z);
    }

    public void setOffset(Vector3 offset) {
        notNull(offset, "offset");

        setXOffset(offset.x);
        setYOffset(offset.y);
        setZOffset(offset.z);
    }

    @Override public double  get(double  x, double  z) {
        return SimplexGradientNoise.sdnoise2(x, z);
    }

    @Override public Vector3 get(Vector3 positionInOut) {
        final double y = SimplexGradientNoise.sdnoise2(positionInOut.x, positionInOut.z);
        positionInOut.y = (float) y;
        return positionInOut;
    }

    @Override public Vector3 get(Vector3 positionInOut, Vector2 gradientOut) {
        final double y = SimplexGradientNoise.sdnoise2(positionInOut.x, positionInOut.z, tempGradient);
        positionInOut.y = (float) y;
        gradientOut.x = (float) tempGradient[0];
        gradientOut.y = (float) tempGradient[1];
        return positionInOut;
    }
}
