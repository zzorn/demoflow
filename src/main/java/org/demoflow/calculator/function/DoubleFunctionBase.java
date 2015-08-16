package org.demoflow.calculator.function;

/**
 *
 */
public abstract class DoubleFunctionBase extends FunctionBase<DoubleFunction> implements DoubleFunction {

    @Override public final Class<DoubleFunction> getReturnType() {
        return DoubleFunction.class;
    }

}
