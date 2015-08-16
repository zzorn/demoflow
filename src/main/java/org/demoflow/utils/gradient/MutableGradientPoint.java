package org.demoflow.utils.gradient;

import org.demoflow.interpolator.Interpolator;
import org.flowutils.Check;

import static org.flowutils.Check.notNull;

/**
 * Mutable implementation of GradientPoint.
 */
public final class MutableGradientPoint<T> implements GradientPoint<T> {

    private double pos;
    private T value;
    private Interpolator interpolator;

    public MutableGradientPoint(double pos, T value, Interpolator interpolator) {
        setPos(pos);
        setValue(value);
        setInterpolator(interpolator);
    }

    @Override public double getPos() {
        return pos;
    }

    /**
     * @return true if the position changed.
     */
    public boolean setPos(double pos) {
        Check.normalNumber(pos, "pos");

        if (pos != this.pos) {
            this.pos = pos;
            return true;
        }
        else {
            return false;
        }
    }

    @Override public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        notNull(interpolator, "interpolator");

        this.interpolator = interpolator;
    }
}
