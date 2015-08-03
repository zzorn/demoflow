package org.demoflow.calculator.calculators;

import com.badlogic.gdx.math.Vector3;
import org.demoflow.animation.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.calculator.CalculatorBase;

/**
 * Calculates the value of a vector from components.
 */
public final class Vector3Calculator extends CalculatorBase<Vector3> {

    public final Parameter<Double> x;
    public final Parameter<Double> y;
    public final Parameter<Double> z;

    public Vector3Calculator() {
        this(0.0, 0.0, 0.0);
    }
    public Vector3Calculator(double initialX, double initialY, double initialZ) {
        x = addParameter("x", initialX);
        y = addParameter("y", initialY);
        z = addParameter("z", initialZ);
    }

    @Override
    protected Vector3 doCalculate(CalculationContext calculationContext,
                                  Vector3 currentValue,
                                  Parameter<Vector3> parameter) {
        final double x = this.x.get();
        final double y = this.y.get();
        final double z = this.z.get();

        currentValue.set((float) x,
                         (float) y,
                         (float) z);

        return currentValue;
    }

    @Override protected void doResetState() {
        // No changing state to reset.
    }
}
