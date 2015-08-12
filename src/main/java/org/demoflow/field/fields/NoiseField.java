package org.demoflow.field.fields;

import org.demoflow.field.FieldBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.SimplexGradientNoise;
import org.flowutils.random.XorShift;

/**
 * Smooth two dimensional noise field.
 * The default output is in the -1 to 1 range.
 */
public final class NoiseField extends FieldBase {

    public Parameter<Integer> seed;
    public Parameter<Double> frequencyX;
    public Parameter<Double> frequencyY;
    public Parameter<Double> inputOffsetX;
    public Parameter<Double> inputOffsetY;
    public Parameter<Double> outputAmplitude;
    public Parameter<Double> outputOffset;

    private static final int UNINITIALIZED_SEED = Integer.MIN_VALUE;
    private static final double RANDOM_OFFSET_RANGE = 10000000.0;

    private final XorShift random = new XorShift();

    private int previousSeed = UNINITIALIZED_SEED;
    private double cachedRandomXOffset;
    private double cachedRandomYOffset;

    /**
     * Uses a seed that is randomized when the noise is created.
     * The frequency will be 1.
     * The output will be in the -1 to 1 range.
     */
    public NoiseField() {
        this(1.0);
    }

    /**
     * Uses a seed that is randomized when the noise is created.
     * The output will be in the -1 to 1 range.
     */
    public NoiseField(double frequency) {
        this(frequency, 1.0, 0.0);
    }

    /**
     * Uses a seed that is randomized when the noise is created.
     *
     * @param frequency frequency of the noise along the x and y axis.
     * @param outputAmplitude scaling for the output value.
     * @param outputOffset offset to add to the output value.
     */
    public NoiseField(double frequency,
                      double outputAmplitude,
                      double outputOffset) {
        this(UNINITIALIZED_SEED, frequency, frequency, 0, 0, outputAmplitude, outputOffset);
    }

    /**
     * @param seed seed used to calculate the sampling position of the noise.
     * @param frequencyX frequency of the noise along the x axis.
     * @param frequencyY frequency of the noise along the y axis.
     * @param inputOffsetX x offset for sampling position of the noise.
     * @param inputOffsetY y offset for sampling position of the noise.
     * @param outputAmplitude scaling for the output value.
     * @param outputOffset offset to add to the output value.
     */
    public NoiseField(int seed,
                      double frequencyX,
                      double frequencyY,
                      double inputOffsetX,
                      double inputOffsetY,
                      double outputAmplitude,
                      double outputOffset) {

        // Randomize seed if it is Integer.MIN_VALUE.  (Kludge to get around that we can't initialize fields before calling constructors)
        if (seed == UNINITIALIZED_SEED) {
            seed = random.nextInt();
            if (seed == UNINITIALIZED_SEED) seed = 0;
        }

        this.seed = addParameter("seed", seed);
        this.frequencyX = addParameter("frequencyX", frequencyX);
        this.frequencyY = addParameter("frequencyY", frequencyY);
        this.inputOffsetX = addParameter("inputOffsetX", inputOffsetX);
        this.inputOffsetY = addParameter("inputOffsetY", inputOffsetY);
        this.outputAmplitude = addParameter("outputAmplitude", outputAmplitude);
        this.outputOffset = addParameter("outputOffset", outputOffset);
    }

    @Override public double get(double x, double y) {
        // Recalculate random offsets into noise if needed
        final Integer seed = this.seed.get();
        if (seed != previousSeed) {
            // Recalculate the cached offsets
            random.setSeed(seed);
            cachedRandomXOffset = random.nextDouble() * RANDOM_OFFSET_RANGE;
            cachedRandomYOffset = random.nextDouble() * RANDOM_OFFSET_RANGE;
            previousSeed = seed;
        }

        // Calculate noise value at the location
        final double noiseX = cachedRandomXOffset + inputOffsetX.get() + x * frequencyX.get();
        final double noiseY = cachedRandomYOffset + inputOffsetY.get() + y * frequencyY.get();
        return SimplexGradientNoise.sdnoise2(noiseX, noiseY) * outputAmplitude.get() + outputOffset.get();
    }
}
