package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.interpolator.interpolators.SteppingInterpolator;

/**
 * Stepping interpolation.
 */
public final class SteppingInterpolation extends InterpolatorFunBase<SteppingInterpolator> {

    public final Parameter<Integer> stepCount;

    public SteppingInterpolation() {
        super(new SteppingInterpolator());

        stepCount = addParameter("stepCount", getInterpolator().getSteps(), IntRange.ONE_OR_LARGER);
    }

    @Override protected void updateInterpolator(SteppingInterpolator interpolator) {
        interpolator.setSteps(stepCount.get());
    }
}
