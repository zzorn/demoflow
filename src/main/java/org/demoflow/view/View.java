package org.demoflow.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.demoflow.Main;
import org.demoflow.demo.Demo;
import org.demoflow.effect.DefaultRenderContext;
import org.flowutils.time.RealTime;
import org.flowutils.time.Time;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Renders a demo to the screen.
 */
public final class View extends Game {

    private static final String SKIN_PATH = Main.ASSET_TARGET_DIR + Main.SKINS_SUBDIR + "/";
    private static final String DEFAULT_SKIN_PATH = SKIN_PATH + "uiskin.json";
    private static final String DEFAULT_SKIN_ATLAS_PATH = SKIN_PATH + "uiskin.atlas";
    private static final String DEFAULT_TEXTURE_ATLAS_PATH = Main.ASSET_TARGET_DIR + Main.TEXTURE_SUBDIR + "/" + Main.TEXTURE_ATLAS_NAME + ".atlas";

    private Skin skin;
    private TextureAtlas textureAtlas;

    private Time time;
    private PerspectiveCamera camera;
    private ModelBatch modelBatch;

    private Demo demo;
    private AtomicReference<Demo> demoToSwitchTo = new AtomicReference<>();
    public DefaultRenderContext renderContext;


    public View() {

    }

    public Demo getDemo() {
        return demo;
    }

    public void setDemo(Demo demo) {
        demoToSwitchTo.set(demo);
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override public void create() {

        camera = createCamera();

        // Init services
        time = new RealTime();

        // Load texture atlas
        textureAtlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);

        // Load skin
        skin = new Skin(Gdx.files.internal(DEFAULT_SKIN_PATH), new TextureAtlas(DEFAULT_SKIN_ATLAS_PATH));

        modelBatch = new ModelBatch();

        renderContext = new DefaultRenderContext(this, modelBatch);
    }

    @Override public void render() {

        // Change the current demo as needed
        switchDemoIfNeeded();

        time.nextStep();

        final float deltaTime = Gdx.graphics.getRawDeltaTime();

        // Clear screen
        // IDEA: An effect could take care of this, so that we could adjust how much of the old view that is visible
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);

        if (demo != null) {
            // Update demo
            demo.update(deltaTime);

            // Render demo
            demo.render(renderContext);
        }

        modelBatch.end();
    }


    @Override public void dispose() {
        if (demo != null) {
            demo.shutdown();
        }

        modelBatch.dispose();
    }

    private void switchDemoIfNeeded() {
        final Demo newDemo = demoToSwitchTo.get();
        if (demo != newDemo) {
            if (demo != null) {

                // Tell demo to shut down on next update
                demo.shutdown();

                // Allow the demo and its effects to shut down in its update methods
                demo.update(0.001);

                demo.setView(null);
            }

            demo = newDemo;

            if (demo != null) {
                demo.setView(this);

                // Setup the demo
                demo.setup();
            }
        }

    }

    private PerspectiveCamera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1f;
        camera.far = 1000f;
        camera.update();
        return camera;
    }
}
