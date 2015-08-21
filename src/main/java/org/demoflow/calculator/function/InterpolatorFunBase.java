package org.demoflow.calculator.function;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.InterpolationRemapRange;
import org.flowutils.MathUtils;
import org.flowutils.interpolator.InterpolationRemap;
import org.flowutils.interpolator.Interpolator;

import static org.flowutils.Check.notNull;

/**
 * Base class for different interpolation functions.
 */
public abstract class InterpolatorFunBase<I extends Interpolator> extends CalculatorBase<InterpolatorFun> implements InterpolatorFun {

    public Parameter<InterpolationRemap> direction;

    private final I interpolator;

    protected InterpolatorFunBase(I interpolator) {
        this(interpolator, InterpolationRemap.IN_OUT);
    }

    protected InterpolatorFunBase(I interpolator, InterpolationRemap remap) {
        notNull(interpolator, "interpolator");
        this.interpolator = interpolator;

        direction = addParameter("direction", remap, InterpolationRemapRange.FULL);
    }

    @Override public final double get(double x) {
        updateInterpolator(interpolator);
        return interpolator.interpolate(x, direction.get());
    }

    public I getInterpolator() {
        return interpolator;
    }

    /**
     * Allows the derived class assign any parameter values to the interpolator before the interpolator is used.
     */
    protected abstract void updateInterpolator(I interpolator);

    @Override public final double interpolate(double t, double a, double b) {
        return MathUtils.mix(get(t), a, b);
    }

    @Override
    public double interpolate(double sourcePos, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        return MathUtils.mix(get(
                                     MathUtils.relPos(
                                             sourcePos,
                                             sourceStart,
                                             sourceEnd)),
                             targetStart,
                             targetEnd);
    }

    @Override public Class<InterpolatorFun> getReturnType() {
        return InterpolatorFun.class;
    }

    @Override
    protected InterpolatorFun doCalculate(CalculationContext calculationContext, InterpolatorFun currentValue, Parameter<InterpolatorFun> parameter) {
        return this;
    }

    @Override protected void doResetState() {
    }

}
