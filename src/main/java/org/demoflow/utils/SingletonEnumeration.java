package org.demoflow.utils;

import java.util.Enumeration;

/**
 *
 */
public final class SingletonEnumeration<T> implements Enumeration<T> {

    private final T value;
    private int index = 0;

    public SingletonEnumeration(T value) {
        this.value = value;
    }

    @Override public boolean hasMoreElements() {
        return index < 1;
    }

    @Override public T nextElement() {
        index++;
        return value;
    }
}
