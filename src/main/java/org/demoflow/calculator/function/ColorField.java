package org.demoflow.calculator.function;

import com.badlogic.gdx.graphics.Color;
import org.demoflow.calculator.Calculator;

/**
 * Returns a color value based on the two input double values.
 * Can be used e.g. for heightfield coloring.
 */
// TODO: Add a HSL color field
public interface ColorField extends Calculator<ColorField> {

    /**
     * @param x x coordinate to get the color for.
     * @param y y coordinate to get the color for.
     * @param colorOut the color instance to write the color at this point to.
     */
    void get(double x, double y, Color colorOut);

}
