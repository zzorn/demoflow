package org.demoflow.field.colorfield.colorfields;

import com.badlogic.gdx.graphics.Color;
import org.demoflow.field.Field;
import org.demoflow.field.colorfield.ColorFieldBase;
import org.demoflow.parameter.Parameter;

/**
 * A color filed with separate fields for red, green, blue and alpha.
 */
public final class RGBColorField extends ColorFieldBase {

    public final Parameter<Field> red;
    public final Parameter<Field> green;
    public final Parameter<Field> blue;
    public final Parameter<Field> alpha;

    public RGBColorField() {
        this(null, null, null, null);
    }

    public RGBColorField(Field red, Field green, Field blue, Field alpha) {
        this.red = addParameter("red", red);
        this.green = addParameter("green", green);
        this.blue = addParameter("blue", blue);
        this.alpha = addParameter("alpha", alpha);
    }

    @Override public void get(double x, double y, Color colorOut) {

        final Field redField = red.get();
        final Field greenField = green.get();
        final Field blueField = blue.get();
        final Field alphaField = alpha.get();

        final double r = redField != null ? redField.get(x, y) : 0;
        final double g = greenField != null ? greenField.get(x, y) : 0;
        final double b = blueField != null ? blueField.get(x, y) : 0;
        final double a = alphaField != null ? alphaField.get(x, y) : 1;

        colorOut.set((float) r,
                     (float) g,
                     (float) b,
                     (float) a);
    }
}
