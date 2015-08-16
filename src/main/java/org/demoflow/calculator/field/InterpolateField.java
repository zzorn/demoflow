package org.demoflow.calculator.field;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithOneBaseField;
import org.demoflow.interpolator.Interpolator;
import org.demoflow.interpolator.interpolators.CosineInterpolator;

import static org.flowutils.Check.notNull;

/**
 * Scales the output of the function using the specified interpolator.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class InterpolateField extends FieldWithOneBaseField {

    private double inputStart;
    private double inputEnd;
    private double resultStart;
    private double resultEnd;
    private boolean clampInput;
    private Interpolator interpolator;

    public InterpolateField() {
        this(null);
    }

    /**
     * Interpolator that scales the result to the 0..1 range.  Assumes the input to interpolate is in the 0..1 range.
     *
     * @param base base function to interpolate.
     */
    public InterpolateField(Field base) {
        this(CosineInterpolator.IN_OUT, base);
    }

    /**
     * Interpolator that scales the result to the 0..1 range.  Assumes the input to interpolate is in the 0..1 range.
     *
     * @param interpolator interpolator to apply to the result of the base function.
     * @param base base function to interpolate.
     */
    public InterpolateField(Interpolator interpolator,
                            Field base) {
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
    public InterpolateField(Interpolator interpolator,
                            double inputStart,
                            double inputEnd,
                            Field base) {
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
    public InterpolateField(Interpolator interpolator,
                            double inputStart,
                            double inputEnd,
                            double resultStart,
                            double resultEnd,
                            Field base) {
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
    public InterpolateField(Interpolator interpolator,
                            double inputStart,
                            double inputEnd,
                            double resultStart,
                            double resultEnd,
                            boolean clampInput,
                            Field base) {
        super(base);

        notNull(interpolator, "interpolator");

        this.inputStart = inputStart;
        this.inputEnd = inputEnd;
        this.resultStart = resultStart;
        this.resultEnd = resultEnd;
        this.clampInput = clampInput;
        this.interpolator = interpolator;
    }

    @Override protected double calculate(Field base, double x, double y) {
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
