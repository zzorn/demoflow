package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.LinearInterpolator;
import org.demoflow.parameter.Parameter;

/**
 * Uses an interpolator to interpolate the source value.
 */
public final class InterpolatingCalculator extends CalculatorBase<Double> {

    public final Parameter<Double> baseValue;
    public final Parameter<Interpolator> interpolator;
    public final Parameter<Boolean> clampToRange;
    public final Parameter<Double> sourceStart;
    public final Parameter<Double> sourceEnd;
    public final Parameter<Double> targetStart;
    public final Parameter<Double> targetEnd;

    public InterpolatingCalculator() {
        this(LinearInterpolator.DEFAULT);
    }

    public InterpolatingCalculator(Interpolator interpolator) {
        this(interpolator, true);
    }

    // TODO: Add direction
    public InterpolatingCalculator(Interpolator interpolator,
                                   boolean clampToRange) {
        this(interpolator, clampToRange, 0, 1, 0, 1);
    }

    // TODO: Add direction
    public InterpolatingCalculator(Interpolator interpolator,
                                   boolean clampToRange,
                                   double sourceStart, double sourceEnd,
                                   double targetStart, double targetEnd) {

        this.baseValue = addParameter("baseValue", 0.0);

        this.interpolator = addParameter("interpolator", interpolator);
        this.clampToRange = addParameter("clampToRange", clampToRange);
        this.sourceStart = addParameter("sourceStart", sourceStart);
        this.sourceEnd = addParameter("sourceEnd", sourceEnd);
        this.targetStart = addParameter("targetStart", targetStart);
        this.targetEnd = addParameter("targetEnd", targetEnd);

    }

    @Override
    protected Double doCalculate(CalculationContext calculationContext,
                                 Double currentValue,
                                 Parameter<Double> parameter) {

        final Double baseValue = this.baseValue.get();
        if (baseValue == null) return null;

        return interpolator.get().interpolate(baseValue, sourceStart.get(),
                                              sourceEnd.get(), targetStart.get(),
                                              targetEnd.get(), clampToRange.get());
    }

    @Override protected void doResetState() {
        // No state to reset
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }
}
