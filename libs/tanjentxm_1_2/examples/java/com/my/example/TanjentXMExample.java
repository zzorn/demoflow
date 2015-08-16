package com.my.example;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tanjent.tanjentxm.Player;

public class TanjentXMExample extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	Player myPlayer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		myPlayer = new Player(44100, Player.INTERPOLATION_MODE_NONE);
		int moduleOne = myPlayer.loadXM(Gdx.files.internal("moduleOne.xm").readBytes(), -1);		
		int moduleTwo = myPlayer.loadXM(Gdx.files.internal("moduleTwo.xm").readBytes(), -1);
		int moduleThree = myPlayer.loadXM(Gdx.files.internal("moduleThree.xm").readBytes(), -1);
		myPlayer.play(moduleThree, true,  true, -1, 1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		myPlayer.dispose();
	}
	
	@Override
	public void pause() {
		myPlayer.pause();
	}

	@Override
	public void resume() {
		myPlayer.resume();
	}
}
