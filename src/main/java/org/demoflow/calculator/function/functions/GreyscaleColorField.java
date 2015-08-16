package org.demoflow.calculator.function.functions;

import com.badlogic.gdx.graphics.Color;
import org.demoflow.calculator.function.Field;
import org.demoflow.calculator.function.ColorFieldBase;
import org.demoflow.parameter.Parameter;

/**
 * A color field that uses a double field to determine the greyscale level.
 * Multiplies that with a single base color parameter.
 */
public final class GreyscaleColorField extends ColorFieldBase {

    public final Parameter<Color> color;
    public final Parameter<Field> luminance;

    public GreyscaleColorField() {
        this(Color.WHITE, null);
    }

    public GreyscaleColorField(Color baseColor, Field luminance) {
        this.color = addParameter("baseColor", baseColor);
        this.luminance = addParameter("luminance", luminance);

    }

    @Override public void get(double x, double y, Color colorOut) {
        colorOut.set(color.get());

        final Field luminanceField = this.luminance.get();
        if (luminanceField != null) {
            colorOut.mul((float) luminanceField.get(x, y));
        }
    }
}
