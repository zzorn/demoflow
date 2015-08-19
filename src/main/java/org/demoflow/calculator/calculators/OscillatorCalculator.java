package org.demoflow.calculator.calculators;

import org.demoflow.calculator.TimeVaryingCalculatorBase;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.CosineInterpolator;
import org.demoflow.interpolator.interpolators.CubicInterpolator;
import org.demoflow.interpolator.interpolators.QuadraticInterpolator;
import org.demoflow.parameter.Parameter;
import org.demoflow.parameter.range.ranges.DoubleRange;

import static org.flowutils.MathUtils.Tau;

/**
 * Time based oscillator with a configurable waveform.
 */
public final class OscillatorCalculator extends TimeVaryingCalculatorBase {

    public Parameter<Interpolator> risingInterpolator;
    public Parameter<Interpolator> droppingInterpolator;
    public Parameter<Double> dutyCycle;
    public Parameter<Double> risingSlopeAmount;
    public Parameter<Double> droppingSlopeAmount;

    /**
     * Creates an oscillating waveform.
     */
    public OscillatorCalculator() {
        this(1);
    }

    /**
     * Creates an oscillating waveform.
     *
     * @param wavelength wavelength of the oscillations in seconds.
     */
    public OscillatorCalculator(double wavelength) {
        this(wavelength, -1, 1);
    }

    /**
     * Creates an oscillating waveform.
     *
     * @param wavelength wavelength of the oscillations in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     */
    public OscillatorCalculator(double wavelength, double minOutput, double maxOutput) {
        this(wavelength, minOutput, maxOutput, 0);
    }

    /**
     * Creates an oscillating waveform.
     *
     * @param wavelength wavelength of the oscillations in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     * @param phase can be used to phase shift the oscillator
     */
    public OscillatorCalculator(double wavelength, double minOutput, double maxOutput, double phase) {
        this(wavelength, minOutput, maxOutput, phase, QuadraticInterpolator.IN_OUT, QuadraticInterpolator.IN_OUT, 0.5, 0.5, 0.5);
    }

    /**
     * Creates an oscillating waveform with configurable rising and dropping shape, and configurable lengths at the top and bottom values.
     * __/¨¨\__/¨¨
     * @param wavelength wavelength of the oscillations in seconds.
     * @param minOutput lowest output value
     * @param maxOutput highest output value
     * @param phase can be used to phase shift the oscillator
     * @param risingInterpolator interpolator to use for the rising part of the wave
     * @param droppingInterpolator interpolator to use for the dropping part of the wave
     * @param dutyCycle how much time is spent at the top (1) or bottom (0) relatively.  0.5 would be 50%-50%, and 0.75 would be 75% at the top and 25% at the bottom.
     * @param risingSlopeAmount amount of rising slope on the waveform, 0 = no slope, instant rise, 1 = start sloping up from the middle of the bottom, stop the rise at the middle of the top.
     * @param droppingSlopeAmount  amount of dropping slope on the waveform, 0 = no slope, instant drop, 1 = start sloping down from the middle of the top, stop the slope at the middle of the bottom.
     */
    public OscillatorCalculator(double wavelength,
                                double minOutput,
                                double maxOutput,
                                double phase,
                                Interpolator risingInterpolator,
                                Interpolator droppingInterpolator,
                                double dutyCycle,
                                double risingSlopeAmount,
                                double droppingSlopeAmount) {
        super(wavelength, minOutput, maxOutput, phase);

        this.risingInterpolator = addParameter("risingInterpolator", risingInterpolator);
        this.droppingInterpolator = addParameter("droppingInterpolator", droppingInterpolator);
        this.dutyCycle = addParameter("dutyCycle", dutyCycle, DoubleRange.ZERO_TO_ONE);
        this.risingSlopeAmount = addParameter("risingSlopeAmount", risingSlopeAmount, DoubleRange.ZERO_TO_ONE);
        this.droppingSlopeAmount = addParameter("droppingSlopeAmount", droppingSlopeAmount, DoubleRange.ZERO_TO_ONE);
    }

    @Override protected double calculateValue(double phase) {
        // Determine what phase we are in (bottom, rising, top, dropping)  __/¨¨\
        // Bottom and top base length is determined by the duty cycle (1 = all top, 0 = all bottom).
        // Slopes eat space from the bottom and top proportionately.

        double risingLen = 0.5 * risingSlopeAmount.get();
        double droppingLen = 0.5 * droppingSlopeAmount.get();

        double totalTopBottomLength = 1.0 - (risingLen + droppingLen);
        final Double relativeTopTime = dutyCycle.get();
        double topLen = totalTopBottomLength * relativeTopTime;
        double bottomLen = totalTopBottomLength * (1.0 - relativeTopTime);

        // Return top or bottom value, or rising or dropping interpolated value, based on the current phase.
        if (phase < bottomLen) return -1;
        else if (phase < bottomLen + risingLen) return risingInterpolator.get().interpolate(phase, bottomLen, bottomLen + risingLen, -1, 1);
        else if (phase < bottomLen + risingLen + topLen) return 1;
        else return droppingInterpolator.get().interpolate(phase, bottomLen + risingLen + topLen, 1.0, 1, -1);
    }
}


