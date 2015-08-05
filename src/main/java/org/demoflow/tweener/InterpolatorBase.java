package org.demoflow.tweener;

/**
 * Common functionality for Tweeners.
 */
public abstract class InterpolatorBase implements Interpolator {

    @Override public final double interpolate(double t, InterpolationDirection interpolationDirection) {
        return interpolationDirection.calculate(t, this);
    }

    @Override public final double interpolate(double t, boolean clampT) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return interpolate(t);
    }

    @Override public final double interpolate(double t, boolean clampT, InterpolationDirection interpolationDirection) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return interpolationDirection.calculate(t, this);
    }

    @Override public final double interpolate(double t, double a, double b) {
        return a + interpolate(t) * (b - a);
    }

    @Override public final double interpolate(double t, double a, double b, boolean clampT) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return a + interpolate(t) * (b - a);
    }

    @Override public final double interpolate(double t,
                                              double a,
                                              double b,
                                              boolean clampT,
                                              InterpolationDirection interpolationDirection) {
        if (clampT) {
            if (t < 0) t = 0;
            if (t > 1) t = 1;
        }

        return a + interpolate(t, interpolationDirection) * (b - a);
    }

    @Override
    public final double interpolate(double sourcePos,
                                    double sourceStart,
                                    double sourceEnd,
                                    double targetStart,
                                    double targetEnd) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = interpolate(relativePos);

        return targetStart + relativePos * (targetEnd - targetStart);
    }

    @Override
    public final double interpolate(double sourcePos,
                                    double sourceStart,
                                    double sourceEnd,
                                    double targetStart,
                                    double targetEnd,
                                    boolean clampSourcePos) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = interpolate(relativePos, clampSourcePos);

        return targetStart + relativePos * (targetEnd - targetStart);
    }

    @Override
    public double interpolate(double sourcePos,
                              double sourceStart,
                              double sourceEnd,
                              double targetStart,
                              double targetEnd,
                              boolean clampSourcePos,
                              InterpolationDirection interpolationDirection) {
        double relativePos = sourceEnd == sourceStart ? 0.5 : (sourcePos - sourceStart) / (sourceEnd - sourceStart);

        relativePos = interpolate(relativePos, clampSourcePos, interpolationDirection);

        return targetStart + relativePos * (targetEnd - targetStart);
    }
}
