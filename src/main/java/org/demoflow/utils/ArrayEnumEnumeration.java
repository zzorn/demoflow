package org.demoflow.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Enumeration;

import static org.flowutils.Check.notNull;

/**
 * Enumerates an array followed by an enumeration.
 */
public final class ArrayEnumEnumeration<T> implements Enumeration<T> {

    private final Array<? extends T> values1;
    private final Enumeration<? extends T> values2;
    private int index = 0;

    public ArrayEnumEnumeration(Array<? extends T> values1, Enumeration<? extends T> values2) {
        notNull(values1, "values1");
        notNull(values2, "values2");

        this.values1 = values1;
        this.values2 = values2;
    }

    @Override public boolean hasMoreElements() {
        return index < values1.size || values2.hasMoreElements();
    }

    @Override public T nextElement() {
        if (index < values1.size) return values1.get(index++);
        else return values2.nextElement();
    }
}
