package org.demoflow.tweener;

/**
 * IN_OUT = normal interpolator range,                                                     __/^^
 * IN = use first half of normal interpolator range and scale it to full range,            ____/
 * OUT = use second half of normal interpolator range and scale it to full range,          /^^^^
 * OUT_IN = first use second half of interpolator and then first half of the interpolator.      ,---'
 */
public enum InterpolationDirection {

    /**
     * Use the first half of the tweening curve.
     */
    IN,

    /**
     * Use the second half of the tweening curve.
     */
    OUT,

    /**
     * Use the normal tweening curve.
     */
    IN_OUT,

    /**
     * Use the second half of the tweening curve as the first half, and vice versa.
     */
    OUT_IN,
    ;

    public double calculate(double t, Interpolator interpolator) {
        switch (this) {
            case IN: return interpolator.interpolate(t * 0.5) * 2.0;
            case OUT: return interpolator.interpolate(t * 0.5 + 0.5) * 2.0 - 1.0;
            case IN_OUT: return interpolator.interpolate(t);
            case OUT_IN: return t < 0.5 ?
                                interpolator.interpolate(t * 0.5 + 0.5) * 2.0 - 1.0 :
                                interpolator.interpolate(t * 0.5) * 2.0;
            default:
                throw new IllegalStateException("Unknown interpolate direction " + this);
        }
    }
}
