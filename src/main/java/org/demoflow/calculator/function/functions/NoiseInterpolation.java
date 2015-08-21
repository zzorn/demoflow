package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.DoubleRange;
import org.flowutils.interpolator.interpolators.NoiseInterpolator;
import org.flowutils.interpolator.interpolators.PowInterpolator;

/**
 * Noise interpolation.
 */
public final class NoiseInterpolation extends InterpolatorFunBase<NoiseInterpolator> {

    public final Parameter<Double> frequency;
    public final Parameter<Double> noiseAmplitude;
    public final Parameter<Double> noiseShape;

    public NoiseInterpolation() {
        super(new NoiseInterpolator());

        frequency = addParameter("frequency", getInterpolator().getFrequency(), DoubleRange.POSITIVE);
        noiseAmplitude = addParameter("noiseAmplitude", getInterpolator().getNoiseAmplitude());
        noiseShape = addParameter("noiseShape", getInterpolator().getNoiseShape());
    }

    @Override protected void updateInterpolator(NoiseInterpolator interpolator) {
        interpolator.setNoiseAmplitude(noiseAmplitude.get());
        interpolator.setFrequency(frequency.get());
        interpolator.setNoiseShape(noiseShape.get());
    }
}
