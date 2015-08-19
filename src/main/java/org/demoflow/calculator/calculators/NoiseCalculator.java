package org.demoflow.calculator.calculators;

import org.demoflow.calculator.TimeVaryingCalculatorBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.SimplexGradientNoise;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 * Generates noise that changes over time.
 */
public final class NoiseCalculator extends TimeVaryingCalculatorBase {

    private static final RandomSequence RANDOM_SEQUENCE = new XorShift();

    public final Parameter<Double> noiseY;

    /**
     * Generates random noise with a frequency of 1Hz and output values from -1 to 1.
     */
    public NoiseCalculator() {
        this(1.0);
    }

    /**
     * Generates random noise with the specified frequency and output values from -1 to 1.
     * @param wavelength wavelength of the noise in seconds.
     */
    public NoiseCalculator(double wavelength) {
        this(wavelength, -1, 1);
    }

    /**
     * Generates random noise with the specified frequency and min and max output values.
     * @param wavelength wavelength of the noise in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     */
    public NoiseCalculator(double wavelength, double minOutput, double maxOutput) {
        this(wavelength, minOutput, maxOutput, 0.0);
    }

    /**
     * Generates random noise with the specified frequency and min and max output values.
     * @param wavelength wavelength of the noise in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     * @param phase can be used to phase shift the noise
     */
    public NoiseCalculator(double wavelength, double minOutput, double maxOutput, double phase) {
        this(wavelength, minOutput, maxOutput, phase, RANDOM_SEQUENCE.nextDouble(10000));
    }

    /**
     * Generates random noise with the specified frequency and min and max output values.
     * @param wavelength wavelength of the noise in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     * @param phase can be used to phase shift the noise
     * @param noiseY another coordinate on the generated noise, allows changing the shape of the noise generated.
     *               Works a bit like a noise seed, except the change is continuous.
     */
    public NoiseCalculator(double wavelength, double minOutput, double maxOutput, double phase, double noiseY) {
        super(wavelength, minOutput, maxOutput, phase);

        this.noiseY = addParameter("noiseY", noiseY);
    }

    protected double calculateValue(final double phase) {
        return SimplexGradientNoise.sdnoise2(phase, noiseY.get());
    }

    @Override protected double wrapPhase(double phase) {
        // No wrapping for noise, as it is not repeating
        return phase;
    }
}
