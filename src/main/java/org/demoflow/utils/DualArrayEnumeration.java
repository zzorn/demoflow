package org.demoflow.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Enumeration;

/**
 * Enumerates two consecutive arrays
 */
public final class DualArrayEnumeration<T> implements Enumeration<T> {

    private final Array<? extends T> values1;
    private final Array<? extends T> values2;
    private int index = 0;

    public DualArrayEnumeration(Array<? extends T> values1,
                                Array<? extends T> values2) {
        this.values1 = values1;
        this.values2 = values2;
    }

    @Override public boolean hasMoreElements() {
        final int numElements = (values1 != null ? values1.size : 0) +
                                (values2 != null ? values2.size : 0);

        return index < numElements;
    }

    @Override public T nextElement() {
        final int values1Size = values1 != null ? values1.size : 0;
        if (index < values1Size) return values1.get(index++);
        else return values2.get((index++) - values1Size);
    }
}
