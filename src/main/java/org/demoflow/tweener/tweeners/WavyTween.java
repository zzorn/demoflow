package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.TweenerBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;

/**
 * Multi-period cosine interpolation.
 */
public final class WavyTween extends TweenerBase {

    public static final WavyTween DEFAULT = new WavyTween();

    private final int waveCount;

    public WavyTween() {
        this(3);
    }

    public WavyTween(int waveCount) {
        Check.positiveOrZero(waveCount, "waveCount");

        this.waveCount = waveCount;
    }

    @Override public double tween(double t) {
        final double baseLine = 0.5 - 0.5 * Math.cos(t * 0.5 * MathUtils.Tau);             // __/~~
        final double waveAmplitude = 0.5 - 0.5 * Math.cos(t * MathUtils.Tau);              // _/~\_
        final double waves = 0.5 - 0.5 * Math.cos((t * (waveCount+0.5) * MathUtils.Tau));  // /\/\/
        return baseLine + waveAmplitude * waves;
    }
}
