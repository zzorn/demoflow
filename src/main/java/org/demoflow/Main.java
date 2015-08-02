package org.demoflow;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.demoflow.effect.effects.CubeEffect;
import org.flowutils.LogUtils;

/**
 *
 */
public class Main {
    public static final String TITLE = "Demoflow";
    public static final String ASSET_SOURCE_DIR = "asset-sources/";
    public static final String ASSET_TARGET_DIR = "assets/";
    public static final String TEXTURE_SUBDIR = "textures";
    public static final String SKINS_SUBDIR = "skins";
    public static final String TEXTURE_ATLAS_NAME = "TextureAtlas";
    public static final int MAX_TEXTURE_SIZE = 512;


    private static View view;


    public static void main(String[] args) {

        // Pack textures
        packTextures();

        // Create view
        final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = Main.TITLE;
        configuration.width = 800;
        configuration.height = 600;

        view = new View();
        new LwjglApplication(view, configuration);

        // Create editor
        Editor editor = new Editor(view);

        // Setup effects
        setupEffects(view.getEffectService());

        // Start
        editor.init();

    }

    /**
     * This will load textures from the asset-sources/textures directory and merge them into bigger textures
     * to the assets/textures directory, along with an atlas file that contains the coordinates of the subtextures
     * inside the new combined texture.
     */
    public static void packTextures() {
        LogUtils.getLogger().info("Updating textures.");

        final TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = Main.MAX_TEXTURE_SIZE;
        settings.maxHeight = Main.MAX_TEXTURE_SIZE;
        settings.paddingX = 2;
        settings.paddingY = 2;
        TexturePacker.process(settings,
                              Main.ASSET_SOURCE_DIR + Main.TEXTURE_SUBDIR,
                              Main.ASSET_TARGET_DIR + Main.TEXTURE_SUBDIR,
                              Main.TEXTURE_ATLAS_NAME);

        LogUtils.getLogger().debug("Textures updated.");
    }


    private static void setupEffects(final EffectService effectService) {
        // NOTE: This is a temporary test setup, until we implement the editor and loading & saving of demos.
        final CubeEffect cubeEffect = new CubeEffect();
        effectService.addEffect(cubeEffect);

        cubeEffect.setParameter(CubeEffect.COLOR, new Color(0.7f, 0f, 0.5f, 1f));
        cubeEffect.setParameter(CubeEffect.SCALE, new Vector3(10, 5, 1));
        cubeEffect.setParameter(CubeEffect.POSITION, new Vector3(2, 0, 0));

    }

}
