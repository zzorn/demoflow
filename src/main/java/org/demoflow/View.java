package org.demoflow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.flowutils.time.RealTime;
import org.flowutils.time.Time;

/**
 * Holds opengl context, methods to add objects to the scenegraph?, etc.
 * Also keeps track of the current effects and updates & renders them.
 */
public final class View extends Game {

    private static final String SKIN_PATH = Main.ASSET_TARGET_DIR + Main.SKINS_SUBDIR + "/";
    private static final String DEFAULT_SKIN_PATH = SKIN_PATH + "uiskin.json";
    private static final String DEFAULT_SKIN_ATLAS_PATH = SKIN_PATH + "uiskin.atlas";
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = Main.ASSET_TARGET_DIR + Main.TEXTURE_SUBDIR + "/" + Main.TEXTURE_ATLAS_NAME + ".atlas";

    private Skin skin;
    private TextureAtlas textureAtlas;

    private EffectService effectService;
    private Time time;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;


    public View() {
        effectService = new EffectService(this);
    }

    public EffectService getEffectService() {
        return effectService;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override public void create() {

        camera = createCamera();

        // Init services
        time = new RealTime();
        effectService.init();

        // Load texture atlas
        textureAtlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);

        // Load skin
        skin = new Skin(Gdx.files.internal(DEFAULT_SKIN_PATH), new TextureAtlas(DEFAULT_SKIN_ATLAS_PATH));

        modelBatch = new ModelBatch();

    }

    @Override public void render() {
        time.nextStep();

        // Clear screen
        // IDEA: An effect could take care of this, so that we could adjust how much of the old view that is visible
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);

        // Render effects
        effectService.render(time.getSecondsSinceLastStep(), modelBatch);

        modelBatch.end();
    }


    @Override public void dispose() {
        effectService.shutdown();
        modelBatch.dispose();
    }

    private PerspectiveCamera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
        return camera;
    }
}
