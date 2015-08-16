TanjentXM is a free and open source library to play FastTracker II (FT2)
.XM-files for HaXe 3+ and Java.

Version History
=======================================================================
2014-01-01  Version 1.0 - First released version
2014-05-12  Version 1.1 - Small fix and updated examples
                          for OpenFL 1.4.0+ and libgdx 1.0+
2014-08-20  Version 1.2 - Small fix and updated examples
                          for OpenFL 2.0.1+* and libgdx 1.3.1+
						  
*) WARNING! OpenFL 2.0.1 has shipped with a BUG that totally cripples
dynamic sound generation! To fix this you must modify the
SampleDataEvent.hx file in:

... \openfl\2,0,1\backends\native\openfl\events\SampleDataEvent.hx

Make sure the class constructor looks like this (i.e. uncomment lines 20 and 21):
---
	public function new (type:String, bubbles:Bool = false, cancelable:Bool = false) {	
		super (type, bubbles, cancelable);		
		data = new ByteArray ();
		data.bigEndian = false;
		position = 0.0;		
	}
---

						  
Introduction
=======================================================================
This is an XM-file playback library written in Java and the HaXe 3.0
language. 

My ambition has been to create a free, fast, portable and light-weight 
XM-file player that can be deployed and used on various devices 
including desktop, mobile phones and tablet devices.

Included in the library is: 

1) XM-file loader and parser (Files: XM*)
2) Mixing engine (Files: FixedPoint, TickMixer)
3) Playback engine (Files: Player)

The playback engine (3) is the only part of the library that requires
integration with a game library or another realtime media library. For
the HaXe version this is typically the OpenFL/Lime platform. For the
Java version this is typically libgdx.

Since (1) and (2) does not rely on any special libraries it should be
easy to port this library to other (game) libraries that support dynamic
(streaming) sound output, the only part that will need rewriting is (3).

Example Playback code (HaXe)
=======================================================================
To play XM-files loaded as ByteArray-assets in a OpenFL/HaXe project the
following code can be used as a starting point:
	
	var myPlayer:Player = new Player();
	var moduleOne:Int = myPlayer.loadXM(Assets.getBytes("assets/moduleOne.xm"), 0);	
	var moduleTwo:Int = myPlayer.loadXM(Assets.getBytes("assets/moduleTwo.xm"), 0);	
	myPlayer.play(moduleOne);
	
To fade out the current playing song for 0.25 seconds and play a new 
song with a fade-in of 1 second you can call the play() function with 
the following parameters: 	

	myPlayer.play(moduleTwo, true, true, 0.25, 1);
	
The first boolean specifies if the module should restart when playback
starts. The second boolean specifies if the module should loop from
the beginning (or the specified restart pattern index) when it has
reached the end. 
	
To stop the player, call:
	
	myPlayer.stop();
	
To completely halt the player and stop the SampleDataEvent from
firing (saves CPU/Battery) call:

	myPlayer.halt();
	
To re-start a halted player you have to create a new player and add the 
modules again: 
	
	var myNewPlayer:Player = new Player();
	moduleOne = myNewPlayer.addXMModule(myPlayer.getModule(moduleOne));
	moduleTwo = myNewPlayer.addXMModule(myPlayer.getModule(moduleTwo));
	myNewPlayer.play(moduleOne);

Please take a look at the provided examples in the examples folder.

Example Playback code (Java)
=======================================================================
To play XM-files loaded as assets in a libgdx project the following code
can be used as a starting point:
	
	Player myPlayer = new Player(44100, Player.INTERPOLATION_MODE_NONE);
	int moduleOne = myPlayer.loadXM(Gdx.files.internal("data/moduleOne.xm").readBytes(), 0);	
	int moduleTwo = myPlayer.loadXM(Gdx.files.internal("data/moduleTwo.xm").readBytes(), 0);	
	myPlayer.play(moduleOne, true, true, 0, 0);

To fade out the current playing song for 0.25 seconds and play a new 
song with a fade-in of 1 second you can call the play() function with 
the following parameters: 	

	myPlayer.play(moduleTwo, true, true, 0.25, 1);	

The first boolean specifies if the module should restart when playback
starts. The second boolean specifies if the module should loop from
the beginning (or the specified restart pattern index) when it has
reached the end. 

To pause the player, call:
	
	myPlayer.pause();

To resume the player, call:
	
	myPlayer.resume();
	
To completely halt the player and stop the player Thread
(saves CPU/Battery) call:

	myPlayer.dispose();
	
To re-start a halted player you have to create a new player and add the 
modules again: 
	
	Player myNewPlayer = new Player(44100, Player.INTERPOLATION_MODE_NONE);
	moduleOne = myNewPlayer.addXM(myPlayer.getModule(moduleOne));
	moduleTwo = myNewPlayer.addXM(myPlayer.getModule(moduleTwo));
	myNewPlayer.play(moduleOne);

Please take a look at the provided examples in the examples folder.
	
Design
=======================================================================
This library has been designed to do one thing only, and that is to
make it easy to play .xm files!

The XM-file format has been chosen mainly because it has a good mix of 
musical expression/capabilities and reasonable performance tradeoffs: 

1) Support for 2 to 32 stereo channels
2) Support for 8-bit and 16-bit audio
3) Samples can have bidirectional loops
4) Support for instruments with vibrato, volume and panning envelopes
5) Patterns have both volume column and effect column
6) Determinable voice allocation (1 channel = 1 voice = 1 sample)
7) Lack of DSP-heavy per sample processing (i.e. no filters)

From the above list it is obvious that XM-files can reproduce a wide
range of different sounds. Songs can range from simple chip tunes
(< 20 kB) to massive multi-channel songs (> several MBs).

The software mixer has been designed using a Fixed Point-technique.
This means that it treats a certain number of bits of an integer as a 
decimal. This gives a very good performance when mixing samples on 
mobile devices. 

Tests have shown that a floating point version of the library has given 
about 1.5x - 2x slower performance in VM environments (such as the Flash 
player) and on Android (4+) devices, i.e. CPU usage has increased from 
4% to ~8% for certain modules (HaXe version).

The library also supports three kinds of interpolation modes when
pitch shifting samples:

1) No Interpolation (Nearest Neighbour)
2) Linear Interpolation
3) Cubic Interpolation

Use (1) on mobile phones to save as much CPU as possible and to be able 
to play 32-channel XM-files. The downside to (1) is some audible 
aliasing (a high pitched ringing) when pitching sounds - but it is also
very fast. Both (2) and (3) provided give less aliasing and can be used
on mobile of the channel count is low (< 8 channels) but they are of
course slower. Use (3) for the "best" quality this library offers.
Cubic interpolation will usually be used on powerful and stationary
devices such as desktops, or when doing off-line processing. 

To provide a sample rate of 96000 Hz or a cubic interpolation mode,
pass these parameters when creating the player object:

	... = new Player(96000, Player.INTERPOLATION_MODE_CUBIC);
	
Since many platform assumes the standard 44100 samples/second sample
rate, this is used as the default sample rate.

Development Resources
=======================================================================
The following files and sources have been very helpful in decoding the
.XM-file format and the playback effects:

	XM.TXT - by Triton with comments by ByteRaver & Wodan
	tracker_notes.txt - by Carraro & Matsuoka
	MODFILXX.TXT - by Thunder with comments by ByteRaver
	MilkyTracker.html - by the MilkyTracker team
	
For song creation and playback reference I've used OpenMPT.

Downloads
=======================================================================
TanjentXM is written and maintained by Jonas Murman at Tanjent. The
initial development started in the summer of 2013 and the first
version was released in January 2014.

The latest version of this library will be available at
http://www.tanjent.se/labs/tanjentxm.html

Reporting Issues
=======================================================================
If you find a module that does not play correctly, please visit the
homepage and let me know!

If you manage to find a problem and correct it I would be very happy to
incorporate your changes to the library, provided you are ready to
license them under the MIT-license.

License
=======================================================================
TanjentXM is licensed under the MIT-license. This means that you can use
it free of charge, without strings attached in commercial and
non-commercial projects. Please read license.txt for the full license.

