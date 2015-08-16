package org.demoflow.utils;

import com.badlogic.gdx.utils.Array;

/**
 * Hardcoded empty array.
 */
public final class EmptyArray<T> extends Array<T> {

    public static final EmptyArray EMPTY_ARRAY = new EmptyArray();

    private static final String MUTATION_ERROR_MESSAGE = "This is a read only empty array.";

    public EmptyArray() {
        super(0);
    }

    @Override public void add(T value) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public void addAll(Array<? extends T> array) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public void addAll(Array<? extends T> array, int start, int count) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public void addAll(T... array) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public void addAll(T[] array, int start, int count) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public void insert(int index, T value) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override public T[] ensureCapacity(int additionalCapacity) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }

    @Override protected T[] resize(int newSize) {
        throw new UnsupportedOperationException(MUTATION_ERROR_MESSAGE);
    }
}
