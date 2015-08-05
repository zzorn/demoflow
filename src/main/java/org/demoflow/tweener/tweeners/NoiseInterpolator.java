package org.demoflow.tweener.tweeners;

import org.demoflow.tweener.InterpolatorBase;
import org.flowutils.Check;
import org.flowutils.MathUtils;
import org.flowutils.SimplexGradientNoise;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 * Noise based interpolation.
 */
public final class NoiseInterpolator extends InterpolatorBase {

    private static final RandomSequence RANDOM_SEQUENCE = new XorShift();

    public static final NoiseInterpolator DEFAULT = new NoiseInterpolator();

    private final double frequency;
    private final double offset;

    /**
     * Creates a new NoiseTween with random offset and a frequency of about four noise waves in the interpolate.
     */
    public NoiseInterpolator() {
        this(4);
    }

    /**
     * Creates a new NoiseTween with random offset and the specified frequency.
     * @param frequency frequency of the noise.  Determines number of noise waves visible in the interpolate.
     */
    public NoiseInterpolator(double frequency) {
        this(frequency, RANDOM_SEQUENCE.nextDouble()*1000);
    }

    /**
     * @param frequency frequency of the noise.  Determines number of noise waves visible in the interpolate.
     * @param offset shifts the noise sideways.  Use e.g. to create different noise patterns for different tweens.
     *               Randomized by default.
     */
    public NoiseInterpolator(double frequency, double offset) {
        Check.positive(frequency, "frequency");
        Check.normalNumber(offset, "offset");

        this.offset = offset;
        this.frequency = frequency;
    }

    @Override public double interpolate(double t) {
        final double baseLine = 0.5 - 0.5 * Math.cos(t * 0.5 * MathUtils.Tau);                 // __/~~
        final double waveAmplitude = 0.5 - 0.5 * Math.cos(t * MathUtils.Tau);                  // _/~\_
        final double noise = 0.5 - 0.5 * SimplexGradientNoise.sdnoise1((t+offset)*frequency);  // /\|\_/
        return baseLine + waveAmplitude * noise;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getOffset() {
        return offset;
    }
}
