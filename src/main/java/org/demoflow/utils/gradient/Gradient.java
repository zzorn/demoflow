package org.demoflow.utils.gradient;

import org.demoflow.tweener.Tweener;

import java.util.Collection;

/**
 * Interpolates between objects placed at specified points, using specified interpolators between each point.
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
     * @param interpolatorFromPreviousPoint interpolator to use between the previous point and this point.
     */
    void addPoint(double pos, T value, Tweener interpolatorFromPreviousPoint);

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
     * @return read only collection with the points in this gradient, ordered from smallest to largest position.
     */
    Collection<GradientPoint<T>> getPoints();

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
     * Update the value at the specified index.
     * @param pointIndex
     * @param newValue
     */
    void setPointValue(int pointIndex, T newValue);

    /**
     * Update the interpolator at the specified index.
     * @param pointIndex
     * @param newInterpolation
     */
    void setPointInterpolation(int pointIndex, Tweener newInterpolation);
}
