package org.demoflow.utils;

import com.badlogic.gdx.utils.Array;

import java.util.Enumeration;

/**
 *
 */
public final class ArrayEnumeration<T> implements Enumeration<T> {

    private final Array<? extends T> values;
    private int index = 0;

    public ArrayEnumeration(Array<? extends T> values) {
        this.values = values;
    }

    @Override public boolean hasMoreElements() {
        return values != null && index < values.size;
    }

    @Override public T nextElement() {
        return values.get(index++);
    }
}
