package org.demoflow.particles.components;

import com.badlogic.gdx.math.Vector3;
import org.entityflow.component.Component;
import org.entityflow.component.ComponentBase;
import org.flowutils.Check;

/**
 *
 */
public final class Physical extends ComponentBase {

    /**
     * Previous position of the particle.  Used in Verlet integration.
     */
    public final Vector3 previousPos = new Vector3();

    /**
     * Force accumulated on this particle during the recent timestep.
     * Should be zeroed at the end of each update.
     */
    public final Vector3 force = new Vector3();

    /**
     * Mass of the particle
     */
    public float mass_kg = 1;

    public boolean previousPosInitialized = false;

    public Physical(float mass_kg) {
        Check.positive(mass_kg, "mass_kg");
        this.mass_kg = mass_kg;
    }
}
