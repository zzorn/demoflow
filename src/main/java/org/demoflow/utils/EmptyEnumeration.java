package org.demoflow.utils;

import java.util.Enumeration;

/**
 *
 */
public final class EmptyEnumeration<T> implements Enumeration<T> {

    public static final EmptyEnumeration EMPTY_ENUMERATION = new EmptyEnumeration();

    @Override public boolean hasMoreElements() {
        return false;
    }

    @Override public T nextElement() {
        return null;
    }
}
