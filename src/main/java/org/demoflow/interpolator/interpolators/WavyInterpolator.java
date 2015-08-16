package org.demoflow.interpolator.interpolators;

import org.demoflow.interpolator.InterpolatorBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;

/**
 * Multi-period cosine interpolation.
 */
public final class WavyInterpolator extends InterpolatorBase {

    public static final WavyInterpolator IN_OUT = new WavyInterpolator();

    private final int waveCount;

    public WavyInterpolator() {
        this(3);
    }

    public WavyInterpolator(int waveCount) {
        Check.positiveOrZero(waveCount, "waveCount");

        this.waveCount = waveCount;
    }

    @Override public double interpolate(double t) {
        final double baseLine = 0.5 - 0.5 * Math.cos(t * 0.5 * MathUtils.Tau);             // __/~~
        final double waveAmplitude = 0.5 - 0.5 * Math.cos(t * MathUtils.Tau);              // _/~\_
        final double waves = 0.5 - 0.5 * Math.cos((t * (waveCount+0.5) * MathUtils.Tau));  // /\/\/
        return baseLine + waveAmplitude * waves;
    }
}
