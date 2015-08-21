package org.demoflow.calculator.calculators;

import org.demoflow.calculator.TimeVaryingCalculatorBase;
import org.demoflow.calculator.function.InterpolatorFun;
import org.demoflow.calculator.function.functions.CosineInterpolation;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.IntRange;
import org.flowutils.interpolator.interpolators.LinearInterpolator;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;


/**
 * Creates random noise by selecting a random target value each time period and interpolating to it.
 */
public final class RandomNoiseCalculator extends TimeVaryingCalculatorBase {

    public Parameter<InterpolatorFun> interpolator;
    public Parameter<Integer> seed;

    private final RandomSequence randomSequence = new XorShift();
    private double previousPhase = 1.0;
    private double start;
    private double target;

    public RandomNoiseCalculator() {
        this(1);
    }

    public RandomNoiseCalculator(double wavelength) {
        this(wavelength, -1, 1);
    }

    public RandomNoiseCalculator(double wavelength, double minOutput, double maxOutput) {
        this(wavelength, minOutput, maxOutput, 0);
    }

    public RandomNoiseCalculator(double wavelength, double minOutput, double maxOutput, double phase) {
        this(wavelength, minOutput, maxOutput, phase, (int) System.nanoTime(), new CosineInterpolation());
    }

    public RandomNoiseCalculator(double wavelength,
                                 double minOutput,
                                 double maxOutput,
                                 double phase,
                                 int seed,
                                 InterpolatorFun interpolator) {
        super(wavelength, minOutput, maxOutput, phase);

        this.seed = addParameter("seed", seed, IntRange.FULL, true);
        this.interpolator = addParameter("interpolator", interpolator);
    }

    @Override protected double calculateValue(double phase) {
        if (phase < previousPhase) {
            // We wrapped around, select a new target
            start = target;
            target = randomSequence.nextDouble(-1, 1);
        }
        previousPhase = phase;

        // Interpolate between start and the target
        final InterpolatorFun interpolatorFun = interpolator.get();
        if (interpolatorFun != null) {
            return interpolatorFun.interpolate(phase, start, target);
        } else {
            return LinearInterpolator.IN_OUT.interpolate(phase, start, target);
        }
    }

    @Override protected void doResetState() {
        super.doResetState();
        randomSequence.setSeed(seed.get());
        previousPhase = 1.0;
        start = randomSequence.nextDouble(-1, 1);
        target = randomSequence.nextDouble(-1, 1);
    }
}
