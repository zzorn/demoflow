package org.demoflow.parameter.range.ranges;

import org.demoflow.parameter.range.SelectRange;
import org.flowutils.interpolator.Interpolator;
import org.flowutils.interpolator.interpolators.*;

/**
 * Range with simple Interpolators to choose from.
 * @deprecated Not sure if this will be used..
 */
public class InterpolatorRange extends SelectRange<Interpolator> {

    public static final Interpolator[] INTERPOLATORS = createInstances();

    public static final InterpolatorRange FULL = new InterpolatorRange();


    public InterpolatorRange() {
        super(Interpolator.class, INTERPOLATORS);
    }


    private static Interpolator[] createInstances() {

        // Create selection of common simple interpolators
        return new Interpolator[] {
                LinearInterpolator.IN_OUT,
                SineInterpolator.IN_OUT,
                new PowInterpolator(0.5),
                Pow2Interpolator.IN_OUT,
                Pow3Interpolator.IN_OUT,
                Pow4Interpolator.IN_OUT,
                Pow5Interpolator.IN_OUT,
                new SigmoidInterpolator(),
        };
    }

}
