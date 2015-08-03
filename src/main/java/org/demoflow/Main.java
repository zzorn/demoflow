package org.demoflow;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.demoflow.demo.DefaultDemo;
import org.demoflow.demo.Demo;
import org.demoflow.calculator.calculators.SineCalculator;
import org.demoflow.calculator.calculators.Vector3ScaleCalculator;
import org.demoflow.effect.effects.CubeEffect;
import org.flowutils.LogUtils;

/**
 * Creates an example demo and opens it in an editor and in a view.
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

        // Create demo
        Demo demo = createExampleDemo();

        // Edit demo (and view it)
        editor.setDemo(demo);

        /* The editor will already show the demo.
        // Show demo
        view.setDemo(demo);
         */
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

    private static Demo createExampleDemo() {
        Demo demo = new DefaultDemo();
        demo.addEffect(createCubeEffect(new Color(1f, 0f, 0f, 1f),     new Vector3( 10, 0, 0), 0.0));
        demo.addEffect(createCubeEffect(new Color(0.7f, 0f, 0.7f, 1f), new Vector3(  0, 0, 0), 0.1));
        demo.addEffect(createCubeEffect(new Color(0f, 0f, 1f, 1f),     new Vector3(-10, 0, 0), 0.2));
        return demo;
    }

    private static CubeEffect createCubeEffect(Color color, Vector3 position, final double phase) {

        // Example effect that just displays a cube
        final CubeEffect cubeEffect = new CubeEffect(color, new Vector3(1, 1, 1), position);

        // Create a sine wave whose frequency is modulated by another sine wave
        final SineCalculator sineWobble = new SineCalculator(1, 2, 1, phase);
        sineWobble.waveLength.setCalculator(new SineCalculator(8, 2.5, 2, 0.25));

        // Scales a vector with the sine wobble
        final Vector3ScaleCalculator scaleCalculator = new Vector3ScaleCalculator();
        scaleCalculator.scale.setCalculator(sineWobble);

        // Scale the cube with the scaled vector
        cubeEffect.scale.setCalculator(scaleCalculator);

        return cubeEffect;
    }


}
