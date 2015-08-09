package org.demoflow.functions;

/**
 * Applies the b function on the coordinates passed to the a function.
 */
public final class WarpFunction2 extends Function2TwoFunctionBase {

    private static final double DEFAULT_X_OFFSET = 34987.2341;
    private static final double DEFAULT_Y_OFFSET = 90714.8371;

    private double inputXOffset = DEFAULT_X_OFFSET;
    private double inputYOffset = DEFAULT_Y_OFFSET;
    private double inputXScale = 1;
    private double inputYScale = 1;

    public WarpFunction2() {
        this(null, null);
    }

    public WarpFunction2(Function2 a, Function2 b) {
        this(DEFAULT_X_OFFSET, DEFAULT_Y_OFFSET, a, b);
    }

    public WarpFunction2(double inputXOffset,
                         double inputYOffset,
                         Function2 a,
                         Function2 b) {
        this(inputXOffset, inputYOffset, 1, 1, a, b);
    }

    public WarpFunction2(double inputXOffset,
                         double inputYOffset,
                         double inputXScale,
                         double inputYScale,
                         Function2 a,
                         Function2 b) {
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

    @Override protected double combine(Function2 a, Function2 b, double x, double y) {
        final double bx = b.get(x * inputXScale + inputXOffset, y * inputXScale + inputXOffset);
        final double by = b.get(x * inputYScale + inputYOffset, y * inputYScale + inputYOffset);
        return a.get(bx, by);
    }
}
