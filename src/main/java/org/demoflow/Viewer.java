package org.demoflow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.demoflow.particles.processors.ParticlePhysicsProcessor;
import org.demoflow.particles.processors.ParticleRenderProcessor;
import org.demoflow.particles.components.Positioned;
import org.entityflow.world.ConcurrentWorld;
import org.entityflow.world.World;
import org.flowutils.random.RandomSequence;
import org.flowutils.random.XorShift;

/**
 */
public final class Viewer extends Game {

    private static final String SKIN_PATH = Main.ASSET_TARGET_DIR + Main.SKINS_SUBDIR + "/";
    private static final String DEFAULT_SKIN_PATH = SKIN_PATH + "uiskin.json";
    private static final String DEFAULT_SKIN_ATLAS_PATH = SKIN_PATH + "uiskin.atlas";
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = Main.ASSET_TARGET_DIR + Main.TEXTURE_SUBDIR + "/" + Main.TEXTURE_ATLAS_NAME + ".atlas";

    private int port;

    private Skin skin;

    private TextureAtlas textureAtlas;


    private World world = new ConcurrentWorld();

    public Viewer() {
    }


    @Override public void create() {
        // Load texture atlas
        textureAtlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);

        // Load skin
        skin = new Skin(Gdx.files.internal(DEFAULT_SKIN_PATH), new TextureAtlas(DEFAULT_SKIN_ATLAS_PATH));

        world.init();
        world.addProcessor(new ParticleRenderProcessor());
        world.addProcessor(new ParticlePhysicsProcessor());
        buildTestWorld(world);
    }

    @Override public void render() {
        world.update();

    }

    @Override public void dispose() {
        skin.dispose();
        textureAtlas.dispose();
        world.shutdown();
    }

    private void buildTestWorld(World world) {
        RandomSequence randomSequence = new XorShift();

        world.createEntity(new Positioned(randomPos(randomSequence, 100)))

    }

    private Vector3 randomPos(RandomSequence randomSequence, int spread) {
        return new Vector3(randomSequence.nextGaussianFloat() * spread,
                           randomSequence.nextGaussianFloat() * spread,
                           randomSequence.nextGaussianFloat() * spread);
    }
}
