package org.demoflow;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import org.demoflow.demo.DefaultDemo;
import org.demoflow.demo.Demo;
import org.demoflow.editor.DefaultEditorManager;
import org.demoflow.calculator.calculators.*;
import org.demoflow.editor.DemoEditor;
import org.demoflow.effect.effects.CubeEffect;
import org.demoflow.effect.effects.Plasma;
import org.demoflow.view.View;
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

    public static void main(String[] args) {

        // Pack textures
        packTextures();

        // Create view
        final LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = Main.TITLE;
        configuration.width = 800;
        configuration.height = 600;

        // Fix broken defaults buffer sizes (at least on linux the small default buffers result in mangled sound)
        configuration.audioDeviceBufferSize = 4*2048;
        configuration.audioDeviceBufferCount = 6;

        // Create asset managers
        DemoComponentManager demoComponentManager = new DemoComponentManager();
        DefaultEditorManager editorManager = new DefaultEditorManager();

        // Create view
        View view = new View();
        new LwjglApplication(view, configuration);

        // Create editor (and show it)
        DemoEditor demoEditor = new DemoEditor(view, demoComponentManager, editorManager);

        // Create empty demo
        Demo demo = new DefaultDemo("Example Demo", 60);

        // Edit demo
        demoEditor.setDemo(demo);
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

    private static Plasma createPlasmaEffect(final double relativeEntryTime, final double relativeExitTime){
    	final Plasma p = new Plasma();
    	p.setEffectTimePeriod(relativeEntryTime, relativeExitTime);
		return p;
    }

    private static CubeEffect createCubeEffect(Color color, Vector3 position, final double phase, final double relativeEntryTime, final double relativeExitTime) {

        // Example effect that just displays a cube
        final CubeEffect cubeEffect = new CubeEffect(color, new Vector3(1, 1, 1), position);

        // Create a noise wave whose frequency is modulated by a sine wave
        final NoiseCalculator wobble = new NoiseCalculator(1, 2, 1, phase);
        wobble.wavelength.setCalculator(new SineCalculator(8, 3.1, 3, 0.25));

        // Scales a vector with the sine wobble
        final Vector3ScaleCalculator scaleCalculator = new Vector3ScaleCalculator();
        scaleCalculator.scale.setCalculator(wobble);

        // Scale the cube with the scaled vector
        cubeEffect.scale.setCalculator(scaleCalculator);

        // Tune the color a bit as well
        final ColorCalculator cubeColor = new ColorCalculator();
        cubeColor.r.setCalculator(new NoiseCalculator(0.08, color.r-0.1, 0.1));
        cubeColor.b.setCalculator(new NoiseCalculator(0.13, color.b-0.1, 0.1));
        cubeEffect.color.setCalculator(cubeColor);


        // Set the time that the effect is visible
        cubeEffect.setEffectTimePeriod(relativeEntryTime, relativeExitTime);

        return cubeEffect;
    }


}
