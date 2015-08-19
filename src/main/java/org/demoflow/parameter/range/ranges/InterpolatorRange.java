package org.demoflow.parameter.range.ranges;

import org.demoflow.interpolator.Interpolator;
import org.demoflow.utils.ClassUtils;
import org.flowutils.LogUtils;

import java.util.ArrayList;
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

        // Create instances of each interpolator using default no-args constructor, if available.
        List<Interpolator> interpolators = new ArrayList<>();
        for (Class<? extends Interpolator> interpolatorClass : interpolatorClasses) {
            try {
                final Interpolator interpolator = interpolatorClass.newInstance();
                interpolators.add(interpolator);
            }
            catch (Exception e) {
                // If not suitable no args constructor is available, assume the interpolator isn't stand alone (e.g. a combine interpolator), and skip it.
                LogUtils.getLogger().warn("Could not create instance of interpolator " + interpolatorClass + " (Check that it has a public zero parameter constructor if it's a simple interpolator that should be selectable in a combo-box).");
            }
        }

        return interpolators.toArray(new Interpolator[interpolators.size()]);
    }

}
