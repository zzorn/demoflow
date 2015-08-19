package org.demoflow.calculator.function.functions;

import com.badlogic.gdx.math.Vector2;
import org.demoflow.calculator.function.FieldBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.SimplexGradientNoise;
import org.flowutils.random.XorShift;

/**
 * Smooth two dimensional noise field.
 * The default output is in the -1 to 1 range.
 */
public final class NoiseField extends FieldBase {

    public Parameter<Integer> seed;
    public Parameter<Double> waveLength;
    public Parameter<Vector2> inputOffset;
    public Parameter<Double> minOutput;
    public Parameter<Double> maxOutput;

    private static final int UNINITIALIZED_SEED = Integer.MIN_VALUE;
    private static final double RANDOM_OFFSET_RANGE = 10000000.0;

    private final XorShift random = new XorShift();

    private int previousSeed = UNINITIALIZED_SEED;
    private double cachedRandomXOffset;
    private double cachedRandomYOffset;
    public Parameter<Double> noiseZ;

    /**
     * Uses a seed that is randomized when the noise is created.
     * The wave length will be 1.
     * The output will be in the -1 to 1 range.
     */
    public NoiseField() {
        this(1.0);
    }

    /**
     * Uses a seed that is randomized when the noise is created.
     * The output will be in the -1 to 1 range.
     *
     * @param waveLength length of the noise features.
     */
    public NoiseField(double waveLength) {
        this(waveLength, -1.0, 1.0);
    }

    /**
     * Uses a seed that is randomized when the noise is created.
     *
     * @param waveLength length of the noise features.
     * @param minOutput minimum output value.
     * @param maxOutput maximum output value.
     */
    public NoiseField(double waveLength,
                      double minOutput,
                      double maxOutput) {
        this(UNINITIALIZED_SEED, waveLength, new Vector2(), minOutput, maxOutput, 0);
    }

    /**
     * @param seed seed used to calculate the sampling position of the noise.
     * @param waveLength length of the noise features.
     * @param inputOffset offset for sampling position of the noise.
     * @param minOutput minimum output value.
     * @param maxOutput maximum output value.
     * @param noiseZ another coordinate on the generated noise, allows changing the shape of the noise generated.
     *               Works a bit like a noise seed, except the change is continuous.
     */
    public NoiseField(int seed,
                      double waveLength,
                      Vector2 inputOffset,
                      double minOutput,
                      double maxOutput,
                      double noiseZ) {

        // Randomize seed if it is Integer.MIN_VALUE.  (Kludge to get around that we can't initialize fields before calling constructors)
        if (seed == UNINITIALIZED_SEED) {
            seed = random.nextInt();
            if (seed == UNINITIALIZED_SEED) seed = 0;
        }

        this.seed = addParameter("seed", seed);
        this.waveLength = addParameter("waveLength", waveLength);
        this.inputOffset = addParameter("inputOffset", inputOffset != null ? inputOffset : new Vector2());
        this.minOutput = addParameter("minOutput", minOutput);
        this.maxOutput = addParameter("maxOutput", maxOutput);
        this.noiseZ = addParameter("noiseZ", noiseZ);
    }

    @Override public double get(double x, double y) {
        final double min = minOutput.get();
        final double max = maxOutput.get();
        if (min == max) return min;

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
        Vector2 inputOffset = this.inputOffset.get();

        final double noiseX = x * waveLength.get() + cachedRandomXOffset + inputOffset.x;
        final double noiseY = y * waveLength.get() + cachedRandomYOffset + inputOffset.y;
        final double noiseValue = SimplexGradientNoise.sdnoise3(noiseX, noiseY, noiseZ.get());
        return noiseValue * (max - min) + min;
    }
}
