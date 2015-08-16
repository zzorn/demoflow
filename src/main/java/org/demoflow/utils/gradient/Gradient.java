package org.demoflow.utils.gradient;

import org.demoflow.interpolator.Interpolator;

import java.util.Iterator;

/**
 * Interpolates between objects placed at specified points, using specified interpolators between each point.
 * Uses a ValueInterpolator to do the actual mixing between two values.
 */
public interface Gradient<T> extends DoubleFun<T> {

    /**
     * Adds a point with a linear interpolation to the previous point.
     * @param pos position to add the point at.
     * @param value value at the point.
     */
    void addPoint(double pos, T value);

    /**
     * @param pos position to add the point at.
     * @param value value at the point.
     * @param interpolator interpolator to use between the previous point and this point.
     */
    void addPoint(double pos, T value, Interpolator interpolator);

    /**
     * @return number of points in this gradient.
     */
    int size();

    /**
     * @return information for the point closest to the specified position, or null if no points in the gradient.
     */
    GradientPoint<T> getClosestPoint(double pos);

    /**
     * @return the index of the point closest to the specified pos, or -1 if there are no points in the gradient.
     */
    int getClosestIndex(double pos);

    /**
     * @return information for the point at the specified index, or null if no point at the specified index.
     */
    GradientPoint<T> getPointAtIndex(int index);

    /**
     * @return iterator over the points in this gradient, ordered from smallest to largest position.
     */
    Iterator<? extends GradientPoint<T>> getPoints();

    /**
     * @param pos position at which the closest point should be removed.
     * @return true if a point was removed.
     */
    boolean removeClosestPoint(double pos);

    /**
     * Removes the n:th point from this gradient, if there is one.
     * @return true if a point was removed.
     */
    boolean removePointAtIndex(int index);

    /**
     * Removes all points.
     */
    void clear();

    /**
     * Updates the properties of the point at the specified index.
     * Note that the index may change as the result of this operation!
     * @param pointIndex index of the point to change.
     * @param position new position for the point.
     * @param value new value at the point.
     * @param interpolator new interpolator to use for values between the point and the previous one.
     */
    void updatePoint(int pointIndex, double position, T value, Interpolator interpolator);

    /**
     * Update the position of the point at the specified index.
     * Note that the index may change as the result of this operation!
     */
    void setPointPosition(int pointIndex, double position);

    /**
     * Update the value at the specified index.
     */
    void setPointValue(int pointIndex, T newValue);

    /**
     * Update the interpolator at the specified index.
     */
    void setPointInterpolation(int pointIndex, Interpolator newInterpolation);

    /**
     * @return used for interpolating between the values in the gradient.
     */
    ValueInterpolator<T> getValueInterpolator();

    /**
     * @param valueInterpolator used for interpolating between the values in the gradient.
     *                          Must be specified before values can be calculated.
     */
    void setValueInterpolator(ValueInterpolator<T> valueInterpolator);

    /**
     * @return value at the specified position, using the specified interpolator to interpolate between values of type T.
     */
    T getValueAt(double pos, ValueInterpolator<T> valueInterpolator);
}
