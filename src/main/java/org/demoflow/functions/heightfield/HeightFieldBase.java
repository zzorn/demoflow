package org.demoflow.functions.heightfield;

import com.badlogic.gdx.math.Vector3;

/**
 *
 */
public abstract class HeightFieldBase implements HeightField {

    @Override public Vector3 get(Vector3 positionInOut) {
        positionInOut.y = (float) get(positionInOut.x, positionInOut.z);
        return positionInOut;
    }

}
