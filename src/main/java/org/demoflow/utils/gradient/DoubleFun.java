package org.demoflow.utils.gradient;

/**
 * Returns a specified type of value for a double number.
 * Typically defined for the 0..1 range, but should not throw errors outside of it either.
 */
public interface DoubleFun<T> {

    T getValueAt(double pos);

}
