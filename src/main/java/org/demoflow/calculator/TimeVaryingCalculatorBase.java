package org.demoflow.calculator;

import org.demoflow.parameter.Parameter;
import org.flowutils.MathUtils;
import org.flowutils.SimplexGradientNoise;

/**
 * A base class for calculators that calculate time varying oscillating functions.
 */
public abstract class TimeVaryingCalculatorBase extends CalculatorBase<Double> {

    public final Parameter<Double> wavelength;
    public final Parameter<Double> minOutput;
    public final Parameter<Double> maxOutput;
    public final Parameter<Double> phase;

    private double currentPhase;

    /**
     */
    protected TimeVaryingCalculatorBase() {
        this(1);
    }

    /**
     * @param wavelength wavelength of the function in seconds.
     */
    protected TimeVaryingCalculatorBase(double wavelength) {
        this(wavelength, -1, 1);
    }

    /**
     * @param wavelength wavelength of the function in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     */
    protected TimeVaryingCalculatorBase(double wavelength,
                                     double minOutput,
                                     double maxOutput) {
        this(wavelength, minOutput, maxOutput, 0);
    }

    /**
     * @param wavelength wavelength of the function in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     * @param phase can be used to phase shift the function
     */
    protected TimeVaryingCalculatorBase(double wavelength,
                                     double minOutput,
                                     double maxOutput,
                                     double phase) {
        this.wavelength = addParameter("wavelength", wavelength);
        this.minOutput = addParameter("minOutput", minOutput);
        this.maxOutput = addParameter("maxOutput", maxOutput);
        this.phase     = addParameter("phase", phase);
    }

    @Override
    protected final Double doCalculate(CalculationContext calculationContext, Double currentValue, Parameter<Double> parameter) {

        // Update phase
        final double wavelength = this.wavelength.get();
        if (wavelength != 0) {
            currentPhase = wrapPhase(currentPhase + calculationContext.deltaTimeSeconds() / wavelength);
        }

        // Calculate value
        double value = calculateValue(wrapPhase(currentPhase + phase.get()));

        // Apply output range
        return MathUtils.map(value, -1, 1, minOutput.get(), maxOutput.get());
    }

    /**
     * Calculate the value of the time varying function based on the current phase.
     * @param phase phase of the noise, in the 0..1 range.
     * @return calculated value for the specified phase.  Should be in the -1 to 1 range.
     */
    protected abstract double calculateValue(double phase);

    @Override protected void doResetState() {
        currentPhase = 0;
    }

    @Override public final Class<Double> getReturnType() {
        return Double.class;
    }

    protected double wrapPhase(double phase) {
        return MathUtils.wrap0To1(phase);
    }
}
