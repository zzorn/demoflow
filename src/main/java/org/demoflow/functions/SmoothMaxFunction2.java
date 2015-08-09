package org.demoflow.functions;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.CosineInterpolator;
import org.flowutils.Check;

import static org.flowutils.Check.notNull;

/**
 * Mixes between the return value of a and b, depending on which one is larger, using a specified interpolator.
 */
public final class SmoothMaxFunction2 extends Function2TwoFunctionBase {

    private Interpolator interpolator;
    private double threshold;

    public SmoothMaxFunction2() {
        this(null, null);
    }

    /**
     * Uses cosine interpolation and a threshold of 1.
     */
    public SmoothMaxFunction2(Function2 a, Function2 b) {
        this(CosineInterpolator.IN_OUT, a, b);
    }

    /**
     * Takes the max of a and b, mixing them together with the interpolator if they are less than 1 apart.
     * @param interpolator interpolator to mix a and b with if they are closer to each other than the threshold.
     */
    public SmoothMaxFunction2(Interpolator interpolator, Function2 a, Function2 b) {
        this(interpolator, 1, a, b);
    }

    /**
     * @param interpolator interpolator to mix a and b with if they are closer to each other than the threshold.
     * @param threshold how much the difference between a and b should be to use only one of them.
     */
    public SmoothMaxFunction2(Interpolator interpolator, double threshold, Function2 a, Function2 b) {
        super(a, b);

        Check.positiveOrZero(threshold, "threshold");

        this.threshold = threshold;
        setInterpolator(interpolator);
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        notNull(interpolator, "interpolator");
        this.interpolator = interpolator;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        final double aValue = a.get(x, y);
        final double bValue = b.get(x, y);

        final double distance = Math.abs(aValue - bValue);

        if (distance > threshold) {
            return aValue >= bValue ? aValue : bValue;
        }
        else {
            final double t = 1.0 - (aValue - bValue + distance) / (2*distance);
            return interpolator.interpolate(t, aValue, bValue);
        }
    }


}
