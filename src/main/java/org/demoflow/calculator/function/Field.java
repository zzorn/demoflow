package org.demoflow.calculator.function;

import org.demoflow.calculator.Calculator;

/**
 * Returns a double value based on the two input double values.
 * Can be used e.g. for heightfields.
 */
// IDEA: Functions to add: Worley noise, distributed splatter using Function2s zeroed in on the splat and tuned out at splat radius (e.g. craters).
// IDEA: Tune power distribution of splat sizes and opacity
// IDEA: Three function base, using one function to mix two others.
// IDEA: Specify noise functions output range.
// IDEA: Function1, and various functions.
// TODO: HeightField demo effect, and HeightField renderable.
// IDEA: Provide gradient optionally, and have a vector2 field that extracts it?  Maybe just have a vector2 gradient noise field instead, perhaps providing the height value in the third coordinate in a vector3..
public interface Field extends Calculator<Field> {

    double get(double x, double y);

}
