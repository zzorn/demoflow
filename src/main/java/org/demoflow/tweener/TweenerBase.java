package org.demoflow.tweener;

/**
 * Common functionality for Tweeners.
 */
public abstract class TweenerBase implements Tweener {

    @Override public final double tween(double t, TweeningDirection tweeningDirection) {
        return tweeningDirection.calculate(t, this);
    }

    @Override public final double tween(double t, boolean clampT) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return tween(t);
    }

    @Override public final double tween(double t, boolean clampT, TweeningDirection tweeningDirection) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return tweeningDirection.calculate(t, this);
    }

    @Override public final double tween(double t, double a, double b) {
        return a + tween(t) * (b - a);
    }

    @Override public final double tween(double t, double a, double b, boolean clampT) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return a + tween(t) * (b - a);
    }

    @Override public final double tween(double t, double a, double b, boolean clampT, TweeningDirection tweeningDirection) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return a + tween(t, tweeningDirection) * (b - a);
    }

    @Override
    public final double tween(double sourcePos, double sourceStart, double sourceEnd, double targetStart, double targetEnd) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = tween(relativePos);

        return targetStart + relativePos * (targetEnd - targetStart);
    }

    @Override
    public final double tween(double sourcePos,
                              double sourceStart,
                              double sourceEnd,
                              double targetStart,
                              double targetEnd,
                              boolean clampSourcePos) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = tween(relativePos, clampSourcePos);

        return targetStart + relativePos * (targetEnd - targetStart);
    }

    @Override
    public double tween(double sourcePos,
                        double sourceStart,
                        double sourceEnd,
                        double targetStart,
                        double targetEnd,
                        boolean clampSourcePos,
                        TweeningDirection tweeningDirection) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = tween(relativePos, clampSourcePos, tweeningDirection);

        return targetStart + relativePos * (targetEnd - targetStart);
    }
}
