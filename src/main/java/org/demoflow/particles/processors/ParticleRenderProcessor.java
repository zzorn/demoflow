package org.demoflow.particles.processors;

import org.demoflow.particles.components.ParticleAppearance;
import org.entityflow.entity.Entity;
import org.entityflow.processors.EntityProcessorBase;
import org.flowutils.time.Time;

/**
 *
 */
public class ParticleRenderProcessor extends EntityProcessorBase {


    public ParticleRenderProcessor() {
        super(ParticleAppearance.class);
    }

    @Override protected void processEntity(Time time, Entity entity) {


    }
}
