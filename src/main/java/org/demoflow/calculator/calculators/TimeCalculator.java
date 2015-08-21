package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.calculator.TimeType;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.SelectRange;
import org.demoflow.parameter.range.ranges.TimeTypeRange;

/**
 * Returns relative position within the demo as a value from 0 to 1.
 */
// TODO: Add enum parameter for selecting the type of time (relative effect pos or demo pos, or absolute time, etc).
public final class TimeCalculator extends CalculatorBase<Double> {

    public final Parameter<TimeType> timeType;
    public final Parameter<Double> scale;
    public final Parameter<Double> offset;

    public TimeCalculator() {
        this(TimeType.RELATIVE_EFFECT_POSITION);
    }

    public TimeCalculator(TimeType timeType) {
        this(timeType, 1);
    }

    public TimeCalculator(TimeType timeType, double scale) {
        this(timeType, scale, 0);
    }

    public TimeCalculator(TimeType timeType, double scale, double offset) {
        this.timeType = addParameter("timeType", timeType, TimeTypeRange.FULL);
        this.scale = addParameter("scale", scale);
        this.offset = addParameter("offset", offset);
    }

    @Override
    protected Double doCalculate(CalculationContext calculationContext,
                                 Double currentValue,
                                 Parameter<Double> parameter) {
        return offset.get() + scale.get() * timeType.get().getTime(calculationContext);
    }

    @Override protected void doResetState() {
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }

}
