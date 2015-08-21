package org.demoflow.utils.gradient;

import com.badlogic.gdx.utils.Array;
import org.flowutils.interpolator.Interpolator;
import org.flowutils.interpolator.interpolators.LinearInterpolator;

import java.util.Comparator;
import java.util.Iterator;

import static org.flowutils.Check.notNull;

/**
 * Default implementation of a gradient.
 */
public final class GradientImpl<T> implements Gradient<T> {

    private static final Comparator<MutableGradientPoint> POINT_COMPARATOR = new Comparator<MutableGradientPoint>() {
        @Override public int compare(MutableGradientPoint o1, MutableGradientPoint o2) {
            return (int) Math.signum(o1.getPos() - o2.getPos());
        }
    };

    private final Array<MutableGradientPoint<T>> points = new Array<>();

    private ValueInterpolator<T> valueInterpolator;

    /**
     * Creates a new empty gradient.
     *
     * NOTE: A value interpolator must be provided before values can be calculated using setValueInterpolator().
     */
    public GradientImpl() {
        this(null);
    }

    /**
     * Creates a new empty gradient.
     *
     * @param valueInterpolator used for interpolating between the values in the gradient.
     */
    public GradientImpl(ValueInterpolator<T> valueInterpolator) {
        this.valueInterpolator = valueInterpolator;
    }

    @Override public void addPoint(double pos, T value) {
        addPoint(pos, value, LinearInterpolator.IN_OUT);
    }

    @Override public void addPoint(double pos, T value, Interpolator interpolator) {
        points.add(new MutableGradientPoint<T>(pos, value, interpolator));

        // Sort points if the new point is not after the previous ones
        if (points.size > 1 && pos < points.get(points.size-2).getPos()) {
            sortPoints();
        }
    }

    @Override public int size() {
        return points.size;
    }

    @Override public GradientPoint<T> getClosestPoint(double pos) {
        return getClosestMutablePoint(pos);
    }

    @Override public int getClosestIndex(double pos) {
        return findClosestIndex(pos);
    }

    @Override public GradientPoint<T> getPointAtIndex(int index) {
        if (index < 0 || index >= points.size) return null;
        else return points.get(index);
    }

    @Override public Iterator<? extends GradientPoint<T>> getPoints() {
        return points.iterator();
    }

    @Override public boolean removeClosestPoint(double pos) {
        return removePointAtIndex(findClosestIndex(pos));
    }

    @Override public boolean removePointAtIndex(int index) {
        if (index < 0 || index >= points.size) return false;
        points.removeIndex(index);
        return true;
    }

    @Override public void clear() {
        points.clear();
    }

    @Override public void updatePoint(int pointIndex, double position, T value, Interpolator interpolator) {
        final MutableGradientPoint<T> mutablePoint = getClosestMutablePoint(pointIndex);
        if (mutablePoint != null) {
            boolean positionChanged = mutablePoint.setPos(position);
            mutablePoint.setValue(value);
            mutablePoint.setInterpolator(interpolator);

            // The order of the points may have changed as position was updated, sort them
            if (positionChanged) sortPoints();
        }
    }

    @Override public void setPointPosition(int pointIndex, double position) {
        final MutableGradientPoint<T> mutablePoint = getClosestMutablePoint(pointIndex);
        if (mutablePoint != null) {
            boolean positionChanged = mutablePoint.setPos(position);

            // The order of the points may have changed as position was updated, sort them
            if (positionChanged) sortPoints();
        }
    }

    @Override public void setPointValue(int pointIndex, T newValue) {
        final MutableGradientPoint<T> mutablePoint = getClosestMutablePoint(pointIndex);
        if (mutablePoint != null) {
            mutablePoint.setValue(newValue);
        }
    }

    @Override public void setPointInterpolation(int pointIndex, Interpolator newInterpolation) {
        final MutableGradientPoint<T> mutablePoint = getClosestMutablePoint(pointIndex);
        if (mutablePoint != null) {
            mutablePoint.setInterpolator(newInterpolation);
        }
    }

    @Override public ValueInterpolator<T> getValueInterpolator() {
        return valueInterpolator;
    }

    @Override public void setValueInterpolator(ValueInterpolator<T> valueInterpolator) {
        this.valueInterpolator = valueInterpolator;
    }

    @Override public T getValueAt(final double pos) {
        if (valueInterpolator == null) throw new IllegalStateException("No ValueInterpolator has been specified for the Gradient, can not interpolate values.  Use setValueInterpolator, or the correct constructor to set one.");

        return getValueAt(pos, valueInterpolator);
    }

    @Override public T getValueAt(final double pos, ValueInterpolator<T> valueInterpolator) {
        notNull(valueInterpolator, "valueInterpolator");

        // If we are empty return null, if we only have one point return the value at it
        if (points.size <= 0) return null;
        else if (points.size == 1) return points.get(0).getValue();

        // Find closest point to the requested position
        final int index = findClosestIndex(pos);
        double closestPos = points.get(index).getPos();
        T closestValue = points.get(index).getValue();

        // Requested position is an exact match for the closest point, return value at it
        if (pos == closestPos) return closestValue;

        final double prevPos;
        final double nextPos;
        final T prevValue;
        final T nextValue;
        final Interpolator interpolator;
        if (pos < closestPos) {
            // Requested position is before first point, return value at first point
            if (index == 0) return closestValue;

            prevPos = points.get(index - 1).getPos();
            prevValue = points.get(index - 1).getValue();
            nextPos = closestPos;
            nextValue = closestValue;
            interpolator = points.get(index).getInterpolator();
        }
        else {
            // Requested position is after last point, return value at last point
            if (index >= points.size - 1) return closestValue;

            prevPos = closestPos;
            prevValue = closestValue;
            nextPos = points.get(index + 1).getPos();
            nextValue = points.get(index + 1).getValue();
            interpolator = points.get(index + 1).getInterpolator();
        }

        // Use interpolation function to interpolate between the two closest values
        double t = interpolator.interpolate(pos, prevPos, nextPos, 0, 1);

        // Interpolate between the two closest values using the calculated interpolation value
        final T result = valueInterpolator.interpolate(t, prevValue, nextValue);

        return result;
    }

    private void sortPoints() {
        points.sort(POINT_COMPARATOR);
    }

    private MutableGradientPoint<T> getClosestMutablePoint(double pos) {
        final int index = findClosestIndex(pos);
        if (index < 0) return null;
        else return points.get(index);
    }

    /**
     * Uses binary search to find the closest point to the requested position.
     * @return -1 if not found (== no points in gradient), otherwise index of closest point.
     */
    private int findClosestIndex(double pos) {
        if (points.size <= 0) return -1;
        else if (points.size == 1) return 0;

        int start = 0;
        int end = points.size - 1;

        // Continue until we have the two closest entries to the position
        while (start + 1 < end) {
            // Calculate midpoint for roughly equal partition
            int mid = (start + end) / 2;
            final double midPos = points.get(mid).getPos();

            if (pos > midPos) {
                start = mid + 1;
            }
            else if (pos < midPos) {
                end = mid - 1;
            } else {
                // Found exact match
                return mid;
            }
        }

        // Get closest
        final double startPos = points.get(start).getPos();
        final double endPos = points.get(end).getPos();
        if (distance(pos, startPos) < distance(pos, endPos)) return start;
        else return end;
    }


    private double distance(double a, double b) {
        return Math.abs(b - a);
    }

}
