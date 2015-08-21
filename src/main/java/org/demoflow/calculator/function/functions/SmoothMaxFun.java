package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithTwoBaseFields;
import org.demoflow.calculator.function.InterpolatorFun;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.flowutils.MathUtils;

/**
 * Mixes between the return value of a and b, depending on which one is larger, using a specified interpolator.
 */
public final class SmoothMaxFun extends FieldWithTwoBaseFields {

    public final Parameter<InterpolatorFun> interpolator;
    public final Parameter<Double> threshold;

    public SmoothMaxFun() {
        this(null, null);
    }

    public SmoothMaxFun(Field a, Field b) {
        this(new LinearInterpolation(), 1.0, a, b);
    }

    /**
     * @param interpolator interpolator to mix a and b with if they are closer to each other than the threshold.
     * @param threshold how much the difference between a and b should be to use only one of them.
     */
    public SmoothMaxFun(InterpolatorFun interpolator, double threshold, Field a, Field b) {
        super(a, b);
        this.interpolator = addParameter("interpolator", interpolator);
        this.threshold = addParameter("threshold", threshold, DoubleRange.POSITIVE);
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        final double aValue = a.get(x, y);
        final double bValue = b.get(x, y);

        final double distance = Math.abs(aValue - bValue);

        if (distance > threshold.get()) {
            return aValue >= bValue ? aValue : bValue;
        }
        else {
            final InterpolatorFun interpolator = this.interpolator.get();
            final double t = MathUtils.relPos(distance, 0, threshold.get());
            if (aValue >= bValue) return interpolator.interpolate(t, bValue, aValue);
            else return bValue;
        }
    }
}
