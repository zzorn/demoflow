package org.demoflow.calculator.calculators;

import com.badlogic.gdx.math.Vector2;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;

/**
 * Calculates the value of a vector from components.
 */
public final class Vector2Calculator extends CalculatorBase<Vector2> {

    public final Parameter<Double> x;
    public final Parameter<Double> y;

    public Vector2Calculator() {
        this(0.0, 0.0);
    }
    public Vector2Calculator(double initialX, double initialY) {
        x = addParameter("x", initialX);
        y = addParameter("y", initialY);
    }

    @Override
    protected Vector2 doCalculate(CalculationContext calculationContext,
                                  Vector2 currentValue,
                                  Parameter<Vector2> parameter) {
        final double x = this.x.get();
        final double y = this.y.get();

        currentValue.set((float) x,
                         (float) y);

        return currentValue;
    }

    @Override protected void doResetState() {
        // No changing state to reset.
    }

    @Override public Class<Vector2> getReturnType() {
        return Vector2.class;
    }
}
