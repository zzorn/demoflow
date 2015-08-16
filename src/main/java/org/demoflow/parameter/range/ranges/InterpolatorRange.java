package org.demoflow.parameter.range.ranges;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.utils.ClassUtils;

import java.util.List;

/**
 * Range with interpolators to choose from.
 */
public class InterpolatorRange extends SelectRange<Interpolator> {

    public static final Interpolator[] INTERPOLATORS = createInstances();

    public static final InterpolatorRange FULL = new InterpolatorRange();


    public InterpolatorRange() {
        super(Interpolator.class, INTERPOLATORS);
    }


    private static Interpolator[] createInstances() {

        // Get interpolator classes
        List<Class<? extends Interpolator>> interpolatorClasses = ClassUtils.getClassesImplementing("org.demoflow",
                                                                                                    Interpolator.class,
                                                                                                    "org.demoflow.interpolator.interpolators");

        // Create instances of each interpolator using default no-args constructor.
        Interpolator[] interpolators = new Interpolator[interpolatorClasses.size()];
        int index = 0;
        for (Class<? extends Interpolator> interpolatorClass : interpolatorClasses) {
            try {
                interpolators[index++] = interpolatorClass.newInstance();
            }
            catch (Exception e) {
                throw new IllegalStateException("Could not create instance of interpolator " + interpolatorClass + ": " + e.getMessage(), e);
            }
        }

        return interpolators;
    }
}
