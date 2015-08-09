package org.demoflow.functions;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.CosineInterpolator;

import static org.flowutils.Check.notNull;

/**
 * Scales the output of the function using the specified interpolator.
 */
public final class InterpolateFunction2 extends Function2OneFunctionBase {

    private double inputStart;
    private double inputEnd;
    private double resultStart;
    private double resultEnd;
    private boolean clampInput;
    private Interpolator interpolator;

    public InterpolateFunction2() {
        this(null);
    }

    /**
     * Interpolator that scales the result to the 0..1 range.  Assumes the input to interpolate is in the 0..1 range.
     *
     * @param base base function to interpolate.
     */
    public InterpolateFunction2(Function2 base) {
        this(CosineInterpolator.IN_OUT, base);
    }

    /**
     * Interpolator that scales the result to the 0..1 range.  Assumes the input to interpolate is in the 0..1 range.
     *
     * @param interpolator interpolator to apply to the result of the base function.
     * @param base base function to interpolate.
     */
    public InterpolateFunction2(Interpolator interpolator,
                                Function2 base) {
        this(interpolator, 0, 1, 0, 1, false, base);
    }

    /**
     * Interpolator that scales the result to the 0..1 range.
     *
     * @param interpolator interpolator to apply to the result of the base function.
     * @param inputStart base function return value to map to the start of the interpolation range.
     * @param inputEnd base function return value to map to the end of the interpolation range.
     * @param base base function to interpolate.
     */
    public InterpolateFunction2(Interpolator interpolator,
                                double inputStart,
                                double inputEnd,
                                Function2 base) {
        this(interpolator, inputStart, inputEnd, 0, 1, false, base);
    }

    /**
     * @param interpolator interpolator to apply to the result of the base function.
     * @param inputStart base function return value to map to the start of the interpolation range.
     * @param inputEnd base function return value to map to the end of the interpolation range.
     * @param resultStart result value to map the start of the interpolation output to.
     * @param resultEnd result value to map the end of the interpolation output to.
     * @param base base function to interpolate.
     */
    public InterpolateFunction2(Interpolator interpolator,
                                double inputStart,
                                double inputEnd,
                                double resultStart,
                                double resultEnd,
                                Function2 base) {
        this(interpolator, inputStart, inputEnd, resultStart, resultEnd, false, base);
    }

    /**
     * @param interpolator interpolator to apply to the result of the base function.
     * @param inputStart base function return value to map to the start of the interpolation range.
     * @param inputEnd base function return value to map to the end of the interpolation range.
     * @param resultStart result value to map the start of the interpolation output to.
     * @param resultEnd result value to map the end of the interpolation output to.
     * @param clampInput true if input values should be clamped to the inputStart..inputEnd range before interpolation.
     * @param base base function to interpolate.
     */
    public InterpolateFunction2(Interpolator interpolator,
                                double inputStart,
                                double inputEnd,
                                double resultStart,
                                double resultEnd,
                                boolean clampInput,
                                Function2 base) {
        super(base);

        notNull(interpolator, "interpolator");

        this.inputStart = inputStart;
        this.inputEnd = inputEnd;
        this.resultStart = resultStart;
        this.resultEnd = resultEnd;
        this.clampInput = clampInput;
        this.interpolator = interpolator;
    }

    @Override protected double calculate(Function2 base, double x, double y) {
        final double inputValue = inputEnd != inputStart ?
                                  (base.get(x, y) - inputStart) / (inputEnd - inputStart) :
                                  0.5;
        final double interpolatedValue = interpolator.interpolate(inputValue, clampInput);
        return resultStart + interpolatedValue * (resultEnd - resultStart);
    }

    public double getInputStart() {
        return inputStart;
    }

    public void setInputStart(double inputStart) {
        this.inputStart = inputStart;
    }

    public double getInputEnd() {
        return inputEnd;
    }

    public void setInputEnd(double inputEnd) {
        this.inputEnd = inputEnd;
    }

    public double getResultStart() {
        return resultStart;
    }

    public void setResultStart(double resultStart) {
        this.resultStart = resultStart;
    }

    public double getResultEnd() {
        return resultEnd;
    }

    public void setResultEnd(double resultEnd) {
        this.resultEnd = resultEnd;
    }

    public boolean isClampInput() {
        return clampInput;
    }

    public void setClampInput(boolean clampInput) {
        this.clampInput = clampInput;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        notNull(interpolator, "interpolator");

        this.interpolator = interpolator;
    }
}
