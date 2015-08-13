package org.demoflow.parameter;

import org.demoflow.calculator.Calculator;

/**
 *
 */
public abstract class ParameterListenerAdapter<T> implements ParameterListener<T> {

    @Override public void onChanged(Parameter<T> parameter, T newValue) {
    }

    @Override public void onDefaultValueChanged(Parameter<T> parameter, T newValue) {
    }

    @Override public void onCalculatorChanged(Parameter<T> parameter, Calculator<T> newCalculator) {
    }
}
