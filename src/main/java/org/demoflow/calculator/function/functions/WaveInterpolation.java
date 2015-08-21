package org.demoflow.calculator.function.functions;

import org.demoflow.calculator.function.InterpolatorFunBase;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.interpolator.interpolators.NoiseInterpolator;
import org.flowutils.interpolator.interpolators.WavyInterpolator;

/**
 * Wave interpolation.
 */
public final class WaveInterpolation extends InterpolatorFunBase<WavyInterpolator> {

    public final Parameter<Integer> waveCount;
    public final Parameter<Double> amplitude;

    public WaveInterpolation() {
        super(new WavyInterpolator());

        waveCount = addParameter("waveCount", getInterpolator().getWaveCount(), IntRange.ONE_OR_LARGER);
        amplitude = addParameter("amplitude", getInterpolator().getWaveAmplitude());
    }

    @Override protected void updateInterpolator(WavyInterpolator interpolator) {
        interpolator.setWaveCount(waveCount.get());
        interpolator.setWaveAmplitude(amplitude.get());
    }
}
