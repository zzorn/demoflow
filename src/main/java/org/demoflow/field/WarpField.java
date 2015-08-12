package org.demoflow.field;

/**
 * Applies the b function on the coordinates passed to the a function.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class WarpField extends FieldWithTwoBaseFields {

    private static final double DEFAULT_X_OFFSET = 34987.2341;
    private static final double DEFAULT_Y_OFFSET = 90714.8371;

    private double inputXOffset = DEFAULT_X_OFFSET;
    private double inputYOffset = DEFAULT_Y_OFFSET;
    private double inputXScale = 1;
    private double inputYScale = 1;

    public WarpField() {
        this(null, null);
    }

    public WarpField(Field a, Field b) {
        this(DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET, a, b);
    }

    public WarpField(double inputXOffset,
                     double inputYOffset,
                     Field a,
                     Field b) {
        this(inputXOffset, inputYOffset, 1, 1, a, b);
    }

    public WarpField(double inputXOffset,
                     double inputYOffset,
                     double inputXScale,
                     double inputYScale,
                     Field a,
                     Field b) {
        super(a, b);
        this.inputXOffset = inputXOffset;
        this.inputYOffset = inputYOffset;
        this.inputXScale = inputXScale;
        this.inputYScale = inputYScale;
    }

    public double getInputXScale() {
        return inputXScale;
    }

    public void setInputXScale(double inputXScale) {
        this.inputXScale = inputXScale;
    }

    public double getInputYScale() {
        return inputYScale;
    }

    public void setInputYScale(double inputYScale) {
        this.inputYScale = inputYScale;
    }

    public double getInputXOffset() {
        return inputXOffset;
    }

    public void setInputXOffset(double inputXOffset) {
        this.inputXOffset = inputXOffset;
    }

    public double getInputYOffset() {
        return inputYOffset;
    }

    public void setInputYOffset(double inputYOffset) {
        this.inputYOffset = inputYOffset;
    }

    @Override protected double calculate(Field a, Field b, double x, double y) {
        final double bx = b.get(x * inputXScale + inputXOffset, y * inputXScale + inputXOffset);
        final double by = b.get(x * inputYScale + inputYOffset, y * inputYScale + inputYOffset);
        return a.get(bx, by);
    }
}
