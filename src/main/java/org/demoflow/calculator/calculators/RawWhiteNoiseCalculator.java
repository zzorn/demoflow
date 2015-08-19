package org.demoflow.calculator.calculators;

import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;
import org.demoflow.parameter.Parameter;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 * Generates white noise, that is, a new random value in the specified bounds each time step.
 */
public final class RawWhiteNoiseCalculator extends CalculatorBase<Double> {

    public final Parameter<Double> minOutput;
    public final Parameter<Double> maxOutput;
    public final Parameter<Integer> seed;

    private final RandomSequence random = new XorShift();

    public RawWhiteNoiseCalculator() {
        this(-1, 1);
    }

    public RawWhiteNoiseCalculator(double minOutput, double maxOutput) {
        this(minOutput, maxOutput, (int) System.nanoTime());
    }

    public RawWhiteNoiseCalculator(double minOutput, double maxOutput, int seed) {
        this.minOutput = addParameter("minOutput", minOutput);
        this.maxOutput = addParameter("maxOutput", maxOutput);
        this.seed = addParameter("seed", seed);
    }

    @Override
    protected Double doCalculate(CalculationContext calculationContext,
                                 Double currentValue,
                                 Parameter<Double> parameter) {
        return random.nextDouble(minOutput.get(), maxOutput.get());
    }

    @Override protected void doResetState() {
        random.setSeed(seed.get());
    }

    @Override public Class<Double> getReturnType() {
        return Double.class;
    }
}
