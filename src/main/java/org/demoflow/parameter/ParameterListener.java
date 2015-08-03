package org.demoflow.parameter;

/**
 * Listener that is notified when a parameter changes.
 */
public interface ParameterListener<T> {

    /**
     * @param parameter parameter that changed.
     * @param newValue new value of parameter.
     */
    void onChanged(Parameter<T> parameter, T newValue);

}
