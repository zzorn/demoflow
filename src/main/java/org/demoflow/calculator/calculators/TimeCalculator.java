package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.calculator.TimeType;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.TimeTypeRange;

/**
 * Returns selected type of time, with the selected scaling and offset.
 * Scaling is applied to future values, not past ones, so works well when the scale is changed by a calculator as well.
 */
public final class TimeCalculator extends CalculatorBase<Double> {

    public final Parameter<TimeType> timeType;
    public final Parameter<Double> scale;
    public final Parameter<Double> offset;

    private double previousTime;
    private double value;
    private boolean initialized = false;

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

        // Get duration since last calculation (or 0 if this is the first one)
        final double time = timeType.get().getTime(calculationContext);
        final double delta;
        if (initialized) {
            delta = time - previousTime;
        } else {
            initialized = true;
            delta = 0;
        }
        previousTime = time;

        // Update current value, scaling the delta as needed
        value += delta * scale.get();

        // Return with added offset
        return value + offset.get();
    }

    @Override protected void doResetState() {
        previousTime = 0;
        value = 0;
        initialized = false;
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }

}
