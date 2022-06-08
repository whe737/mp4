package com.wilson.paino;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.Text;

public class Paino extends ApplicationAdapter {
	private SpriteBatch batch;
	private	Texture bgImg;
	private Texture hpBar;
	private Texture noteImg;
	private Texture holdNoteImg1;
	private Texture holdNoteImg2;
	private Texture holdNoteImg3;
	private Sound hitSound;
	private Sound holdSound;
	private Music backingMusic;
	private int hpPos;
	private int durationLeft;
	private String durationLeftString;
	private boolean isPaused;
	private static int frameCounter;
	private MapInterpreter map;
	private OrthographicCamera camera;
	private Note[][] notes;
	private ArrayList<Rectangle> noteSpawnList;
	private boolean loading;
	private long lastDropTime;

	@Override
	public void create () {
		//sprites
		batch = new SpriteBatch();
		bgImg = new Texture(Gdx.files.internal("ui//layout.png"));
		hpBar = new Texture(Gdx.files.internal("ui//HealthBar.png"));
		noteImg = new Texture(Gdx.files.internal("ui//BlueBox.png"));
		holdNoteImg1 = new Texture(Gdx.files.internal("ui//HoldB.png"));
		hpPos=0;
		//camera
		camera=new OrthographicCamera();
		camera.setToOrtho(false,1920,1030);
		//audio
		hitSound=Gdx.audio.newSound(Gdx.files.internal("fx//hit.ogg"));
		holdSound=Gdx.audio.newSound(Gdx.files.internal("fx//hold.ogg"));
		backingMusic=Gdx.audio.newMusic(Gdx.files.internal("songs//song.mp3"));
		backingMusic.setVolume((float) 0.5);
		//map interpreter
		map=new MapInterpreter("map");
		System.out.println(map.getMap());
		//initializing methods
		initializeDuration();
		initializeMap();
		noteSpawnList=new ArrayList<Rectangle>();
		spawnNote();
	}
	
	private void initializeMap() //mean to only be called at create
	{
		loading=true;
		notes=map.getNotesList();
		loading=false;
	}

	private void initializeDuration() //use for first time
	{
		durationLeft=map.getDurationint();
		durationLeftString=""+(int) (durationLeft/60)+":";
		if (durationLeft%60<10)
		{
			durationLeftString=durationLeftString+"0"+durationLeft%60;
		}
		else
		{
			durationLeftString+=durationLeft%60;
		}
	}

	private void getDurationString()  //use for refreshing every second
	{
		durationLeftString=""+(int) (durationLeft/60)+":";
		if (durationLeft%60<10)
		{
			durationLeftString=durationLeftString+"0"+durationLeft%60;
		}
		else
		{
			durationLeftString+=durationLeft%60;
		}
	}

	private void spawnNote()
	{
		Rectangle note=new Rectangle();
		note.x = MathUtils.random(0, 800-64);
      	note.y = 480;
      	note.width = 64;
      	note.height = 64;
      	noteSpawnList.add(note);
      	lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		frameCounter++;
		ScreenUtils.clear(0, 0, 0.5f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bgImg, 0, -30);
		batch.draw(hpBar,hpPos, 863);
		for(Rectangle noteList: noteSpawnList) {
			batch.draw(noteImg, noteList.x, noteList.y);
		 }
		batch.end();
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnNote();
		for (Iterator<Rectangle> iter = noteSpawnList.iterator(); iter.hasNext(); ) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 600 * Gdx.graphics.getDeltaTime();
			if(raindrop.y + 30 < 0) iter.remove();
		 }
		
		if (loading)
		{
			System.out.println("loading");
		}
		if (hpPos>-411) //checks if game over&&durationLeft>=0
		{
			if (!isPaused&&!loading)
			{
				backingMusic.play();
				if (frameCounter%60==0)	//loop to run every 60 frames or every 1 second
				{
					frameCounter=0;
					durationLeft--;
					getDurationString();
					System.out.println(durationLeftString);
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
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		
		bgImg.dispose();
		hpBar.dispose();
		backingMusic.dispose();
		hitSound.dispose();
		holdSound.dispose();
	}

}
