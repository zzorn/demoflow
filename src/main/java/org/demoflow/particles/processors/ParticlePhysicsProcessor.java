package org.demoflow.particles.processors;

import org.demoflow.particles.components.Physical;
import org.demoflow.particles.components.Positioned;
import org.entityflow.entity.Entity;
import org.entityflow.processors.EntityProcessorBase;
import org.flowutils.time.Time;

/**
 *
 */
public class ParticlePhysicsProcessor extends EntityProcessorBase {

    private static final double TIMESTEP_SECONDS = 0.01;

    public ParticlePhysicsProcessor() {
        super(TIMESTEP_SECONDS, Physical.class, Positioned.class);
    }

    @Override protected void processEntity(Time time, Entity entity) {
        Positioned positioned = entity.get(Positioned.class);
        Physical physical = entity.get(Physical.class);





    }
}
