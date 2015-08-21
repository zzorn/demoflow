package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.calculator.function.InterpolatorFun;
import org.demoflow.calculator.function.functions.LinearInterpolation;
import org.demoflow.parameter.Parameter;

/**
 * Uses an interpolator to interpolate the source value.
 */
public final class InterpolatingCalculator extends CalculatorBase<Double> {

    public final Parameter<Double> baseValue;
    public final Parameter<InterpolatorFun> interpolator;

    public InterpolatingCalculator() {
        this(new LinearInterpolation());
    }

    public InterpolatingCalculator(InterpolatorFun interpolator) {

        this.baseValue = addParameter("baseValue", 0.0);
        this.interpolator = addParameter("interpolator", interpolator);
    }

    @Override
    protected Double doCalculate(CalculationContext calculationContext,
                                 Double currentValue,
                                 Parameter<Double> parameter) {

        final Double baseValue = this.baseValue.get();
        if (baseValue == null) return null;

        final InterpolatorFun interpolatorFun = interpolator.get();

        if (interpolatorFun == null) return baseValue;
        else return interpolatorFun.get(baseValue);
    }

    @Override protected void doResetState() {
        // No state to reset
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }
}
