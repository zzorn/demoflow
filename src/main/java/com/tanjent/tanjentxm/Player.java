/* Licensed under The MIT License (MIT), see license.txt*/
package com.tanjent.tanjentxm;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

/** <p>
 * This is a helper class to load and play {@link XMModule} modules.
 * This class is meant to be used with the libgdx library.
 * Note that this Player will create its own sample mixing {@link Thread}.
 * </p>
 * <p>
 * The player object references modules by an auto generated module reference number. This number is
 * returned from the loadXM() and addXM() functions and later passed on to the play() method to tell
 * which module should be played next.  
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <p>
 * <code>
 * Player p = new Player(44100,  INTERPOLATION_MODE_NONE);<br>
 * int awesome = p.loadXM(Gdx.files.internal("data/awesome.xm").readBytes(), 0);<br>
 * p.play(awesome, true, true, 0, 0);<br>
 * ... do stuff, and when needed:<br>
 * p.pause();<br>
 * p.resume();<br>
 * ... then do more stuff, and when done:<br>
 * p.dispose();<br>
 * </code>
 * </p>
 * @author Jonas Murman */
public class Player {
	
	private int sampleRate;	
	
	public static final int INTERPOLATION_MODE_NONE = 0;
	public static final int INTERPOLATION_MODE_LINEAR = 1;
	public static final int INTERPOLATION_MODE_CUBIC = 2;	
	private int interpolationMode;
	
	private ModulePlayer modulePlayer;
	private Thread thread;
		
	/// Number of stereo samples to be rendered for each buffer.
	/// Actual latency depends on libgdx and the underlying audio platform.
	private static int BUFFER_SIZE = 1024;
	
	private ArrayList<XMModule> modules;

	/**
	 * Creates a new Player.
	 * @param sampleRate the sample rate to use (usually 44100 samples/second).
	 * @param interpolationMode the interpolation mode to use (Player.INTERPOLATION_MODE...)
	 */
	public Player(int sampleRate, int interpolationMode) {	
		
		if (sampleRate <= 0) sampleRate = 44100; 
		this.sampleRate = sampleRate;
		
		this.interpolationMode = Player.INTERPOLATION_MODE_NONE;
		switch(interpolationMode) {
		case INTERPOLATION_MODE_LINEAR:
			this.interpolationMode = INTERPOLATION_MODE_LINEAR;
			break;
		case INTERPOLATION_MODE_CUBIC:
			this.interpolationMode = INTERPOLATION_MODE_CUBIC;
			break;
		}
						
		this.modules = new ArrayList<XMModule>();
	}
	
	/**
	 * <p>Loads and creates a {@link XMModule} from a binary data representation of a FT2 .XM-file. Adds the module
	 * to an internal module collection. Use the returned module reference number to play this module when calling the play() method.
	 * <ul> 
	 * <li>Pass -1 to auto calculate the mixing factor as 1/sqrt(numberOfChannels)
	 * <li>Pass 0 to auto calculate the mixing factor as 1/numberOfChannels
	 * <li>Pass >0 for anything else, i.e. 2.0 will play the module twice as loud (and will probably distort!)
	 * </ul> 
	 * </p>
	 * @param data the binary data of an XM file 
	 * @param mixFactor the mixing factor to use when rendering samples (set to -1 or 0 to auto calculate the mixing factor)
	 * @return the module reference number or -1 on error
	 */
	public int loadXM(byte[] data, float mixFactor)
	{
		XMModule newXMModule = new XMModule(this.sampleRate, mixFactor);
		
		if (newXMModule.loadXM(data) == false)
		{
			return -1;
		}
		
		for(int i=0;i<modules.size();i++)
		{
			if (modules.get(i) == null) {
				modules.set(i, newXMModule);
				return i;
			}
		}
		
		modules.add(newXMModule);
		return modules.size() - 1;
	}
	
	/**
	 * Adds an already loaded {@link XMModule} to the internal module collection and returns
	 * a module reference number to be passed to to the play() method.
	 * The same module can have several reference numbers, but they will all share the same underlying
	 * base module. This means they will share patterns, channels and currently playing sample states.
	 * @param module the module to add
	 * @return the module reference number or -1 on error
	 */
	public int addXM(XMModule module)
	{
		if (module != null) {
			modules.add(module);
			return modules.size() - 1;
		}
		return -1;
	}


	/**
	 * Removes an already loaded {@link XMModule} in the internal module collection.
	 * If the module is playing it will also be stopped.
	 * @param moduleNumber the module reference number
	 */
	public void removeXM(int moduleNumber)
	{
		if (moduleNumber < 0) return;
		if (moduleNumber >= this.modules.size()) return;
		
		if (this.modulePlayer != null) {		
			if (this.modulePlayer.currentModule == this.modules.get(moduleNumber)) {
				this.modulePlayer.play(this.modulePlayer.dummyModule, true, true, 0.25f, 0);
			}
			if (this.modulePlayer.nextModule == this.modules.get(moduleNumber)) {
				this.modulePlayer.nextModule = this.modulePlayer.dummyModule;
			}
		}
		
		this.modules.set(moduleNumber, null);
	}

	
	/**
	 * Returns the associated {@link XMModule} for a given module reference number.
	 * @param moduleNumber the module reference number
	 * @return the associated {@link XMModule} or null on error
	 */
	public XMModule getModule(int moduleNumber)
	{
		if (moduleNumber < 0) return null;
		if (moduleNumber >= this.modules.size()) return null;		
		return this.modules.get(moduleNumber);
	}	
		
	public int getSampleRate()
	{
		return this.sampleRate;
	}	
	
	public float getAmplitude()
	{
		if (this.modulePlayer != null) {
			return this.modulePlayer.getAmplitude();
		}
		return 1.0f;
	}
	
	/**
	 * Sets the amplitude of the player.     
	 * @param amplitude the amplitude to use (0 = mute player, < 1 decrease volume, > 1 increase volume)
	 */
	public void setAmplitude(float amplitude)
	{
		if (this.modulePlayer != null) {
			this.modulePlayer.setAmplitude(amplitude);
		}
	}
	
	/**
	 * Starts a new Thread (if not already active) and plays the {@link XMModule} associated with the given module reference number.
	 * @param moduleNumber the reference number of the module to play (use -1 to play silence)
	 * @param restartModule true = restart module, false = play from last position
	 * @param loopOnEnd true = loop from the restart position (usually the beginning), false = stop on last row of the last pattern (beware of hanging notes!)
	 * @param fadeOutCurrentSeconds the number of seconds to fade out the current playing song before fading in the next
	 * @param fadeInNextSeconds  the number of seconds to fade in the next song 
	 */
	public void play(int moduleNumber, boolean restartModule, boolean loopOnEnd, float fadeOutCurrentSeconds, float fadeInNextSeconds)
	{
		this.start();
		if (this.modulePlayer != null) {
			this.modulePlayer.play(this.getModule(moduleNumber), restartModule, loopOnEnd, fadeOutCurrentSeconds, fadeInNextSeconds);
		}
	}
	
	private class ModulePlayer implements Runnable
	{
		private boolean stopped;
		private boolean paused;
		
		private int sampleRate;
		private int interpolationMode;
		AudioDevice device;
		
		private float amplitude;
				
		TickMixer tickMixer;
				
		private XMModule dummyModule;		
		private XMModule currentModule;
		private XMModule nextModule;
		
		private boolean fading;		
		private static final int FADE_STATUS_FADE_DONE = 0;
		private static final int FADE_STATUS_FADE_OUT = 1;
		private static final int FADE_STATUS_FADE_IN= 2;
		private int fadeStatus;
		private int fadeSamples;
		private int fadeInSamples;
		private float fadeFactor;
		private float fadeFactorDelta;
		private int fadeFactorFP;
		
		private int samplesPosition;
		private int bufferPosition;
		private int tickSamples;
		
		public ModulePlayer(int sampleRate, int interpolationMode)
		{			
			this.stopped = false;
			this.paused = true;
			
			this.sampleRate = sampleRate;
			this.interpolationMode = interpolationMode;			
			this.device = Gdx.app.getAudio().newAudioDevice(this.sampleRate, false);
			
			this.amplitude = 1.0f;
			this.device.setVolume(this.amplitude);
			
			this.tickMixer = new TickMixer(this.sampleRate);		
			
			this.dummyModule = new XMModule(this.sampleRate, 1.0f);
			this.currentModule = this.dummyModule;
			this.nextModule = this.dummyModule;
			
			this.fading = false;
			this.fadeStatus = ModulePlayer.FADE_STATUS_FADE_DONE;
			this.fadeSamples = 0;
			this.fadeInSamples = 0;									
			this.fadeFactor = 1.0f;
			this.fadeFactorDelta = 0;
			this.fadeFactorFP = 0;
			
			this.samplesPosition = 0;
			this.bufferPosition = 0;
			this.tickSamples = 0;			
		}
		
		
		public void play(XMModule module, boolean restartModule, boolean loopOnEnd, float fadeOutCurrentSeconds, float fadeInNextSeconds)
		{
			this.paused = false;
			this.nextModule = module;

			if (this.currentModule == this.nextModule) {
				this.validateAndPlayModule(restartModule, loopOnEnd);
				return;
			}
			
			this.fading = true;			
			
			this.fadeStatus = ModulePlayer.FADE_STATUS_FADE_OUT;
			if (fadeOutCurrentSeconds <= 0) {
				this.fadeSamples = 0;
				this.fadeFactorDelta = 0;
			} else {
				this.fadeSamples = (int)(fadeOutCurrentSeconds * this.sampleRate);
				this.fadeFactorDelta = -this.fadeFactor / (float)this.fadeSamples;
			}
			
			if (fadeInNextSeconds <= 0) {
				this.fadeInSamples = 0;
			} else {
				this.fadeInSamples = (int)(fadeInNextSeconds * this.sampleRate);
			}
			
			this.validateAndPlayModule(restartModule, loopOnEnd);			
		}
		
		public void stop()
		{
			this.stopped = true;
		}		
		
		private void validateAndPlayModule(boolean restart, boolean loop)
		{
			if (this.currentModule == null) this.currentModule = this.dummyModule;
			if (this.nextModule == null) this.nextModule = this.dummyModule;
			
			if (this.nextModule != this.dummyModule) {
				this.nextModule.play(loop);
				if (restart == true) {
					this.nextModule.restart();
				}
			}
		}		
		
		@Override
		public void run() {
			//the floating point version has been commented out in this method
			//according to the libgdx sources a floating point sample array will undergo some extra operations
			//first a float -> short stage and then some amplitude scaling and clamping
			//all this is a bit uneccessary because the FP-mixing code already provides the samples in an integer/short compatible format    

			//float samples[] = new float[Player.BUFFER_SIZE];
			//float silenceSamples[] = new float[Player.BUFFER_SIZE];
			short samples[] = new short[Player.BUFFER_SIZE];
			short silenceSamples[] = new short[Player.BUFFER_SIZE];
			
			while (!this.stopped) {
						
				if (this.paused == false) {
					
					this.currentModule.advanceByOneTick();
					switch(this.interpolationMode) {
					case Player.INTERPOLATION_MODE_LINEAR:
						this.tickSamples = this.tickMixer.renderTickLinearInterpolation(this.currentModule);
						break;
					case Player.INTERPOLATION_MODE_CUBIC:
						this.tickSamples = this.tickMixer.renderTickCubicInterpolation(this.currentModule);
						break;
					default:
						this.tickSamples = this.tickMixer.renderTickNoInterpolation(this.currentModule);
						break;
					}
					this.bufferPosition = 0;
					
					if (this.fading == false) { 
						for (int s=0;s<this.tickSamples;s++) {
							//floating point version
							//samples[this.samplesPosition++] = FixedPoint.FP_TO_FLOAT(this.tickMixer.leftSamples[this.bufferPosition]);
							//samples[this.samplesPosition++] = FixedPoint.FP_TO_FLOAT(this.tickMixer.rightSamples[this.bufferPosition]);
							samples[this.samplesPosition++] = (short)(this.tickMixer.leftSamples[this.bufferPosition] << 1);
							samples[this.samplesPosition++] = (short)(this.tickMixer.rightSamples[this.bufferPosition] << 1);
							this.bufferPosition++;
							if (this.samplesPosition >= Player.BUFFER_SIZE) {
								// write to device
								device.writeSamples(samples, 0, samples.length);
								this.samplesPosition = 0;
							}
						}	
					} else {
						for (int s=0;s<this.tickSamples;s++) {
							if (this.fadeSamples <= 0) {
								if (this.fadeStatus == ModulePlayer.FADE_STATUS_FADE_OUT) {
									// start fade in
									this.currentModule = this.nextModule;
									this.fadeStatus = ModulePlayer.FADE_STATUS_FADE_IN;
									this.fadeSamples = this.fadeInSamples;
									this.fadeFactorDelta = (1.0f - this.fadeFactor) / (float)this.fadeSamples;									
									break;
								}
								if (this.fadeStatus == ModulePlayer.FADE_STATUS_FADE_IN) {
									// stop fading
									this.fading = false;
									this.fadeStatus = ModulePlayer.FADE_STATUS_FADE_DONE;
									break;
								}
							}
							//floating point version
							//samples[this.samplesPosition++] = FixedPoint.FP_TO_FLOAT(this.tickMixer.leftSamples[this.bufferPosition]) * this.fadeFactor;
							//samples[this.samplesPosition++] = FixedPoint.FP_TO_FLOAT(this.tickMixer.rightSamples[this.bufferPosition]) * this.fadeFactor;
							samples[this.samplesPosition++] = (short)(((this.tickMixer.leftSamples[this.bufferPosition] * this.fadeFactorFP) >> FixedPoint.FP_SHIFT) << 1);
							samples[this.samplesPosition++] = (short)(((this.tickMixer.rightSamples[this.bufferPosition] * this.fadeFactorFP) >> FixedPoint.FP_SHIFT) << 1);
							this.fadeFactor += this.fadeFactorDelta;
							this.fadeFactorFP = FixedPoint.FLOAT_TO_FP(this.fadeFactor);
							this.fadeSamples--;							
							this.bufferPosition++;
							if (this.samplesPosition >= Player.BUFFER_SIZE) {
								// write to device
								device.writeSamples(samples, 0, samples.length);
								this.samplesPosition = 0;								
							}
						}							
					}
				} else {
					// write silence to device
					device.writeSamples(silenceSamples, 0, samples.length);					
				}
			}
			
			device.dispose();
		}
		
		public float getAmplitude()
		{
			return this.amplitude;
		}
		
		public void setAmplitude(float amplitude)
		{
			this.amplitude = amplitude;
			if (this.device != null) {
				this.device.setVolume(amplitude);
			}
		}
		
	}
	
	private void start()
	{
		if (this.thread == null) {
			this.modulePlayer = new ModulePlayer(this.sampleRate, this.interpolationMode);
			this.thread = new Thread(this.modulePlayer);
			this.thread.start();			
		}		
	}
	
	/**
	 * Pauses playback.
	 */
	public void pause()
	{
		if (this.modulePlayer != null)
		{
			this.modulePlayer.paused = true;
		}
	}
	
	/**
	 * Resumes playback.
	 */
	public void resume()
	{
		if (this.modulePlayer != null)
		{
			this.modulePlayer.paused = false;
		}		
	}

	/**
	 * Stops the player Thread. 
	 */
	public void dispose () {
		try {
			if (thread != null) {
				this.modulePlayer.stop();
				this.thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
