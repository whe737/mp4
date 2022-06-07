package com.wilson.paino;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
public class GameScreen implements Screen {
    final Start game;

    private SpriteBatch batch;
	private	Texture bgImg;
	private Texture hpBar;
	private Texture noteImg;
	private Texture holdNoteImg1;
	private Texture holdNoteImg2;
	private Texture holdNoteImg3;
	private Texture pauseOverlay;
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
	private Note[][] notesObjList;
	private int[][] notesList;
	private ArrayList<Rectangle> noteSpawnList;
	private boolean loading;
	private long lastDropTime;
	private long BPMFrequency;
	private int gameState=0; //0 for running, 1 for paused, 2 for exit
	private Stage stage;
	Viewport viewport;
	private ImageButton resumeButton;
	private ImageButton restartButton;
	private ImageButton exitButton;
	int WORLD_width=1920;
	int WORLD_height=1030;
	int mapPosition=0;
	Group pauseGroup;

    public GameScreen(final Start game)
    {
        this.game=game;
        batch = new SpriteBatch();
		stage=new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		bgImg = new Texture(Gdx.files.internal("ui//layout.png"));
		hpBar = new Texture(Gdx.files.internal("ui//HealthBar.png"));
		noteImg = new Texture(Gdx.files.internal("ui//BlueBox.png"));
		holdNoteImg1 = new Texture(Gdx.files.internal("ui//HoldB.png"));
		pauseOverlay = new Texture(Gdx.files.internal("ui//pause.png"));
		hpPos=0;
		//camera
		camera=new OrthographicCamera(WORLD_width,WORLD_height);
		//camera.setToOrtho(false,1920,1030);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		viewport = new StretchViewport(WORLD_width, WORLD_height, camera);
		stage.getViewport().apply();
		//audio
		hitSound=Gdx.audio.newSound(Gdx.files.internal("fx//hit.ogg"));
		holdSound=Gdx.audio.newSound(Gdx.files.internal("fx//hold.ogg"));
		backingMusic=Gdx.audio.newMusic(Gdx.files.internal("songs//funny.mp3"));
		backingMusic.setVolume((float) 0.5);
		//map interpreter
		map=new MapInterpreter("example");
		//System.out.println(map.getMap());
		//initializing methods
		initializeDuration();
		initializeMap();
		noteSpawnList=new ArrayList<Rectangle>();
		BPMFrequency=60000/Integer.parseInt(map.getBPM());
		BPMFrequency*=1000000;
		//System.out.println(Arrays.deepToString(map.getNotesIntList()));
		notesList=map.getNotesIntList();
		System.out.println(Arrays.deepToString(notesList));
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void initializeMap() //mean to only be called at create
	{
		loading=true;
		notesList=map.getNotesIntList();
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
		noteSpawnList=new ArrayList<>();
		Rectangle note=new Rectangle();
		if (mapPosition<notesList.length)
		{
			for (int i=0;i<4;i++)
			{
				System.out.println(notesList[mapPosition][i]);
				// if (i==0&&notesList[mapPosition][i]==1)
				// {
				// 	note.x=Gdx.graphics.getWidth()/2-157;
				// }
				// else if (i==1&&notesList[mapPosition][i]==1)
				// {
				// 	note.x=Gdx.graphics.getWidth()/2+53;
				// }
				// else if (i==2&&notesList[mapPosition][i]==1)
				// {
				// 	note.x=Gdx.graphics.getWidth()/2+258;
				// }
				// else
				// {
				// 	note.x=Gdx.graphics.getWidth()/2+465;
				// }
				switch (i)
				{
					case 0:
						if (notesList[mapPosition][0]==1)
						{
							note.x=Gdx.graphics.getWidth()/2-157;
							break;
						}
					case 1:
						if (notesList[mapPosition][1]==1)
						{
							note.x=Gdx.graphics.getWidth()/2-157;
							break;
						}
					case 2:
						if (notesList[mapPosition][2]==1)
						{
							note.x=Gdx.graphics.getWidth()/2-157;
							break;
						}
					case 3:
						if (notesList[mapPosition][3]==1)
						{
							note.x=Gdx.graphics.getWidth()/2-157;
							break;
						}
				}
				note.y = Gdx.graphics.getHeight()+200;
				note.width = 64;
				note.height = 64;
				noteSpawnList.add(note);
			}
			System.out.println();
		}
		mapPosition++;
		lastDropTime = TimeUtils.nanoTime();
	}

    @Override
    public void show() {
        // TODO Auto-generated method stub
		if (hpPos>-411&&durationLeft>=0) //checks if game over
		{
			backingMusic.play();
			// if(TimeUtils.nanoTime() - lastDropTime > BPMFrequency) spawnNote(); //spawner
			if (frameCounter%60==0)	//loop to run every 60 frames or every 1 second
			{
				frameCounter=0;
				durationLeft--;
				getDurationString();
				//System.out.println(durationLeftString);
				if (hpPos>-411)
				{
					hpPos-=40;
					//System.out.println(hpPos);
				}
			}
		}
		else if (hpPos<-411)
		{
			System.out.println("user died");
		}
		else
		{
			System.out.println("user won");
		}
    }

    @Override
    public void render(float delta) {
        frameCounter++;
		Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
        stage.getCamera().update();
		stage.getBatch().begin();
		stage.getBatch().draw(bgImg, 0, -30,1920,1080);
		stage.getBatch().draw(hpBar,hpPos, 863,414,95);
		for (Iterator<Rectangle> iter = noteSpawnList.iterator(); iter.hasNext(); ) {  //moves blocks
			Rectangle note = iter.next();
			note.y -= 600 * Gdx.graphics.getDeltaTime();
			if(note.y + 30 < 0) iter.remove();
		}
		for(Rectangle noteList: noteSpawnList) {  //spawns
			stage.getBatch().draw(noteImg, noteList.x, noteList.y);
		}
		stage.getBatch().end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			if (gameState==0) gameState=1;
		}
		switch (gameState)
		{
			case 0:
				show();
				break;
			case 1:
				pause();
				break;
			case 2:
				hide();
				break;
		}
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        stage.getCamera().viewportWidth = this.WORLD_width;
        stage.getCamera().viewportHeight = this.WORLD_height;
        stage.getCamera().position.set(stage.getCamera().viewportWidth / 2, stage.getCamera().viewportHeight / 2, 0);
        stage.getCamera().update();
    }

    @Override
    public void pause() {
        backingMusic.pause();
		pauseGroup=new Group();
		Image pauseScreen=new Image(new Texture(Gdx.files.internal("ui//pause.png")));
		//pauseScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Drawable resumePNG=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//resume.png")));
		resumeButton=new ImageButton(resumePNG);
		pauseGroup.addActor(pauseScreen);
		pauseGroup.addActor(resumeButton);
		stage.addActor(pauseGroup);
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		{
			resume();
		}
    }

    @Override
    public void resume() {
		if (gameState==1)
		{
			gameState=0;
			pauseGroup.remove();
			show();
		}
    }

    @Override
    public void hide() {
        backingMusic.pause();
		// game.setScreen(new MainMenuScreen(game));
		// dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
		bgImg.dispose();
		hpBar.dispose();
        noteImg.dispose();
        holdNoteImg1.dispose();
        holdNoteImg2.dispose();
        holdNoteImg3.dispose();
		backingMusic.dispose();
		hitSound.dispose();
		holdSound.dispose();
    }

}
