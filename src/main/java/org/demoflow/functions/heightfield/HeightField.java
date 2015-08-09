package org.demoflow.functions.heightfield;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import org.demoflow.functions.Function2;

/**
 * Something that calculates or returns a height field, that is, an y value for given x and z values.
 */
public interface HeightField extends Function2 {

    /**
     * @return the height (y coordinate) at the specified x,z location.
     */
    double get(double x, double z);

    /**
     * @param positionInOut a vector with x and z coordinates to calculate the height for.
     *                      Stores the height in the y coordinate and returns this vector.
     * @return the provided input vector with the calculated height set in the y coordinate.
     */
    Vector3 get(Vector3 positionInOut);

    /**
     * @param positionInOut a vector with x and z coordinates to calculate the height for.
     *                      Stores the height in the y coordinate and returns this vector.
     * @param gradientOut for heightfields that support gradients (slope direction information) the gradient for the location
     *                    should be stored in this vector.
     * @return the provided input vector with the calculated height set in the y coordinate.
     */
    Vector3 get(Vector3 positionInOut, Vector2 gradientOut);

}
