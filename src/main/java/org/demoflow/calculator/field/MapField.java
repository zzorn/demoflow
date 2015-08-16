package org.demoflow.calculator.field;

import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.FieldWithOneBaseField;

/**
 * Maps the inputs and outputs of the base function to ranges.
 @deprecated Replace with Function1, 2, 3, etc and FunctionField & FieldFunction etc
 */
public final class MapField extends FieldWithOneBaseField {

    private double startX;
    private double endX;
    private double startY;
    private double endY;
    private double startResult;
    private double endResult;
    private boolean clampX;
    private boolean clampY;
    private boolean clampResult;

    /**
     * Does not clamp input or output ranges.
     * Does not do any remapping of the inputs or the output.
     * Does not have any base function.
     */
    public MapField() {
        this(null);
    }

    /**
     * Does not clamp input or output ranges.
     * Does not do any remapping of the inputs or the output.
     *
     * @param base base function to map.
     */
    public MapField(Field base) {
        this(1, base);
    }

    /**
     * Does not clamp input or output ranges.
     * Does not do any remapping of the inputs, only the result.
     *
     * @param resultScale value to scale the result of the base function with.
     * @param base base function to map.
     */
    public MapField(double resultScale,
                    Field base) {
        this(-resultScale, resultScale, -1, 1, base);
    }

    /**
     * Does not clamp input or output ranges.
     * Does not do any remapping of the inputs, only the result.
     *
     * @param startResult value to map 0 result to.
     * @param endResult value to map 1 result to.
     * @param base base function to map.
     */
    public MapField(double startResult,
                    double endResult,
                    Field base) {
        this(startResult, endResult, -1, 1, base);
    }

    /**
     * Does not clamp input or output ranges.
     *
     * @param resultScale value to scale the result of the base function with.
     * @param xScale value to scale the first input with.
     * @param yScale value to scale the second input with.
     * @param base base function to map.
     */
    public MapField(double resultScale,
                    double xScale,
                    double yScale,
                    Field base) {
        this(0, resultScale, 0, xScale, 0, yScale, false, false, false, base);
    }

    /**
     * Does not clamp input or output ranges.
     *  @param startResult value to map 0 result to.
     * @param endResult value to map 1 result to.
     * @param startInput value to map 0 x or y to.
     * @param endInput value to map 1 x or y to.
     * @param base base function to map.
     */
    public MapField(double startResult,
                    double endResult,
                    double startInput,
                    double endInput,
                    Field base) {
        this(startResult, endResult, startInput, endInput, startInput, endInput, false, false, false, base);
    }

    /**
     * Does not clamp input or output ranges.
     *
     * @param startResult value to map 0 result to.
     * @param endResult value to map 1 result to.
     * @param startX value to map 0 x to.
     * @param endX value to map 1 x to.
     * @param startY value to map 0 y to.
     * @param endY value to map 1 y to.
     * @param base base function to map.
     */
    public MapField(double startResult,
                    double endResult,
                    double startX,
                    double endX,
                    double startY,
                    double endY,
                    Field base) {
        this(startResult, endResult, startX, endX, startY, endY, false, false, false, base);
    }

    /**
     * @param startResult value to map 0 result to.
     * @param endResult value to map 1 result to.
     * @param startX value to map 0 x to.
     * @param endX value to map 1 x to.
     * @param startY value to map 0 y to.
     * @param endY value to map 1 y to.
     * @param clampX whether to clamp the input x value to the startX..endX range.
     * @param clampY whether to clamp the input y value to the startY..endY range.
     * @param clampResult  whether to clamp the result value to the startResult..endResult range.
     * @param base base function to map.
     */
    public MapField(double startResult,
                    double endResult,
                    double startX,
                    double endX,
                    double startY,
                    double endY,
                    boolean clampX,
                    boolean clampY,
                    boolean clampResult,
                    Field base) {
        super(base);
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.startResult = startResult;
        this.endResult = endResult;
        this.clampX = clampX;
        this.clampY = clampY;
        this.clampResult = clampResult;
    }

    @Override protected double calculate(Field base, double x, double y) {
        final double mappedX = map(x, startX, endX, clampX);
        final double mappedY = map(y, startY, endY, clampY);
        return map(base.get(mappedX, mappedY), startResult, endResult, clampResult);
    }

    private double map(double value, double start, double end, boolean clamp) {
        final double result = start + value * (end - start);
        if (clamp) {
            if (result < start) return start;
            else if (result > end) return end;
        }
        return result;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public double getStartResult() {
        return startResult;
    }

    public void setStartResult(double startResult) {
        this.startResult = startResult;
    }

    public double getEndResult() {
        return endResult;
    }

    public void setEndResult(double endResult) {
        this.endResult = endResult;
    }

    public boolean isClampX() {
        return clampX;
    }

    public void setClampX(boolean clampX) {
        this.clampX = clampX;
    }

    public boolean isClampY() {
        return clampY;
    }

    public void setClampY(boolean clampY) {
        this.clampY = clampY;
    }

    public boolean isClampResult() {
        return clampResult;
    }

    public void setClampResult(boolean clampResult) {
        this.clampResult = clampResult;
    }

}
