package org.demoflow.utils;

import com.badlogic.gdx.utils.Array;
import org.demoflow.effect.Effect;
import org.demoflow.node.DemoNode;
import org.demoflow.parameter.Parameter;

/**
 *
 */
public final class ArrayUtils {


    public static final Array EMPTY_ARRAY = new EmptyArray();

    /**
     * @return as or bs if the other is empty, otherwise a new array with the combined contents.
     */
    public static <T> Array<? extends T> combineArrays(final Array<? extends T> as, final Array<? extends T> bs) {
        if (as == null || as.size <= 0) return bs;
        else if (bs == null || bs.size <= 0) return as;
        else {
            // A bit of a fast kludge.  This is inefficient if done in tight loops.
            final int capacity = as.size + bs.size;
            final Array effectsAndParameters = new Array<>(capacity);
            effectsAndParameters.addAll(as);
            effectsAndParameters.addAll(bs);
            return effectsAndParameters;
        }
    }

    private ArrayUtils() {
    }
}
