package org.demoflow.tweener;

/**
 * IN_OUT = normal tweener range,                                                     __/^^
 * IN = use first half of normal tweener range and scale it to full range,            ____/
 * OUT = use second half of normal tweener range and scale it to full range,          /^^^^
 * OUT_IN = first use second half of tweener and then first half of the tweener.      ,---'
 */
public enum TweeningDirection {

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

    public double calculate(double t, Tweener tweener) {
        switch (this) {
            case IN: return tweener.tween(t * 0.5) * 2.0;
            case OUT: return tweener.tween(t * 0.5 + 0.5) * 2.0 - 1.0;
            case IN_OUT: return tweener.tween(t);
            case OUT_IN: return t < 0.5 ?
                                tweener.tween(t * 0.5 + 0.5) * 2.0 - 1.0 :
                                tweener.tween(t * 0.5) * 2.0;
            default:
                throw new IllegalStateException("Unknown tween direction " + this);
        }
    }
}
