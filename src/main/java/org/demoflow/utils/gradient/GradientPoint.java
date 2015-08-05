package org.demoflow.utils.gradient;

import org.demoflow.tweener.Interpolator;

/**
 * A point in a Gradient.
 */
public interface GradientPoint<T> {

    /**
     * @return position of this point in the gradient.
     */
    double getPos();

    /**
     * @return value at this point.
     */
    T getValue();

    /**
     * @return interpolation method to use from the previous point to this point.
     */
    Interpolator getInterpolator();

}
