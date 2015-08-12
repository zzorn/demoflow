package org.demoflow.field.colorfield.colorfields;

import com.badlogic.gdx.graphics.Color;
import org.demoflow.field.Field;
import org.demoflow.field.colorfield.ColorFieldBase;
import org.demoflow.parameter.Parameter;

/**
 * A color filed with a constant color.
 */
public final class ConstantColorField extends ColorFieldBase {

    public final Parameter<Color> color;

    public ConstantColorField() {
        this(Color.GRAY);
    }

    public ConstantColorField(Color color) {
        this.color = addParameter("color", color);
    }

    @Override public void get(double x, double y, Color colorOut) {
        colorOut.set(color.get());
    }
}
