package org.demoflow;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.flowutils.LogUtils;

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


    public Viewer() {
    }


    @Override public void create() {
        // Load texture atlas
        textureAtlas = new TextureAtlas(DEFAULT_TEXTURE_ATLAS_PATH);

        // Load skin
        skin = new Skin(Gdx.files.internal(DEFAULT_SKIN_PATH), new TextureAtlas(DEFAULT_SKIN_ATLAS_PATH));

    }




}
