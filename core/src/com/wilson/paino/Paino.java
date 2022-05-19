package com.wilson.paino;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Paino extends ApplicationAdapter {
	private SpriteBatch batch;
	private	Texture bgImg;
	private Texture hpBar;
	private Sound hitSound;
	private Sound holdSound;
	private Music backingMusic;
	private int hpPos;
	private boolean isPaused;
	private static int frameCounter;
	private MapInterpreter map;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bgImg = new Texture(Gdx.files.internal("ui/layout.png"));
		hpBar = new Texture(Gdx.files.internal("ui/HealthBar.png"));
		hpPos=0;
		hitSound=Gdx.audio.newSound(Gdx.files.internal("fx/hit.ogg"));
		holdSound=Gdx.audio.newSound(Gdx.files.internal("fx/hold.ogg"));
		backingMusic=Gdx.audio.newMusic(Gdx.files.internal("songs/funny.mp3"));
		backingMusic.setVolume((float) 0.5);
		map=new MapInterpreter("example");
		System.out.println(map.getMap());
	}

	@Override
	public void render () {
		frameCounter++;
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(bgImg, 0, -30);
		batch.draw(hpBar,hpPos, 863);
		batch.end();
		if (!isPaused&&hpPos>-411)
		{
			backingMusic.play();
			if (frameCounter%60==0)	//loop to run every 60 frames or every 1 second
			{
				if (hpPos>-411)
				{
					hpPos-=40;
					System.out.println(hpPos);
				}
			}
		}
		else 
		{
			backingMusic.pause();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bgImg.dispose();
		hpBar.dispose();
	}

}
