package org.demoflow.effect.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tanjent.tanjentxm.Player;
import org.demoflow.effect.EffectBase;
import org.demoflow.effect.RenderContext;
import org.demoflow.parameter.Parameter;
import org.demoflow.calculator.CalculationContext;
import org.demoflow.parameter.range.ranges.BooleanRange;
import org.flowutils.LogUtils;
import org.flowutils.random.RandomSequence;

/**
 * Plays XM tracker files.
 */
// IDEA: Support for multiple songs in sequence?  The player has some crossfade support.
// LATER: Add support for playback speed setting to the XM player, this allows use of the time dilation effect and editor speed settings.
public final class XMPlayerEffect extends EffectBase {

    public static final String DEFAULT_SONG_PATH = "assets/music/";
    private static final int NO_MODULE_ID = -1;

    public final Parameter<String> songName;
    public final Parameter<Double> volume;
    public final Parameter<Boolean> loop;

    private Player player;

    private double prevVolume = 0;
    private int moduleId = NO_MODULE_ID;

    public XMPlayerEffect() {
        this("song.xm", 1.0, false);
    }

    public XMPlayerEffect(final String songName, final double volume, final boolean loop) {
        this.songName = addParameter("module", songName);
        this.volume = addParameter("volume", volume);
        this.loop = addParameter("loop", loop, BooleanRange.FULL, true);
    }

    @Override protected void doSetup(Object preCalculatedData) {
        // Create player
        player = new Player(44100, Player.INTERPOLATION_MODE_NONE);
        updateVolume(true);

    }

    @Override protected void doReset() {
        // Remove old song, if any
        if (moduleId >= 0) {
            player.removeXM(moduleId);
        }

        // Reload the song (so we can edit and update it without relaunching the demo)
        final FileHandle songFile = Gdx.files.internal(DEFAULT_SONG_PATH + this.songName.get());
        if (songFile != null) {
            try {
                moduleId = player.loadXM(songFile.readBytes(), -1);
            } catch (Exception e) {
                LogUtils.getLogger().error("Could not load song file " + songFile + ": " + e.getMessage(), e);
                moduleId = NO_MODULE_ID;
            }
        }
    }

    @Override protected void doActivate() {
        if (player != null && moduleId != NO_MODULE_ID) {
            // Start playing
            player.play(moduleId, true, loop.get(), -1, 1);
        }
    }

    @Override protected void doDeactivate() {
        if (player != null) {
            player.pause();
        }
    }

    @Override protected void doUpdate(CalculationContext calculationContext) {
        // TODO: Adjust playback rate based on elapsed time

    }

    @Override protected void doRender(RenderContext renderContext) {
        updateVolume(false);
    }

    @Override protected void doShutdown() {
        if (player != null) {
            player.dispose();
            player = null;
        }
    }

    private void updateVolume(boolean forceUpdate) {
        final double newVolume = volume.get();
        if (player != null && (forceUpdate || newVolume != prevVolume)) {
            player.setAmplitude((float) newVolume);
            prevVolume = newVolume;
        }
    }

    @Override public void setPaused(boolean paused) {
        if (player != null) {
            if (paused) player.pause();
            else player.resume();
        }
    }
}
