package com.wilson.paino;

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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
public class GameScreen implements Screen{
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
	private int gameState=0; //0 for running, 1 for paused, 2 for exit, 3 for game over, 4 for win
	private Stage stage;
	Viewport viewport;
	private ImageButton resumeButton;
	private ImageButton restartButton;
	private ImageButton exitButton;
	int WORLD_width=1920;
	int WORLD_height=1030;
	int mapPosition=0;
	Group pauseGroup;
	int combo=0;
	Rectangle box1;
	Rectangle box2;
	Rectangle box3;
	Rectangle box4;
	boolean box1Active;
	boolean box2Active;
	boolean box3Active;
	boolean box4Active;
	Drawable resumePNG;
	Image pauseScreen;
	Texture boxImg;

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
		box1=new Rectangle();
		box1.x=Gdx.graphics.getWidth()/2-253;
		box1.y=-30;
		box1.height=64;
		box1.width=64;
		box2=new Rectangle();
		box2.x=Gdx.graphics.getWidth()/2-42;
		box2.y=-30;
		box2.width=64;
		box2.height=64;
		box3=new Rectangle();
		box3.x=Gdx.graphics.getWidth()/2+165;
		box3.y=-30;
		box3.height=64;
		box3.width=64;
		box4=new Rectangle();
		box4.x=Gdx.graphics.getWidth()/2+370;
		box4.y=-30;
		box4.height=64;
		box4.width=64;
		boxImg=new Texture(Gdx.files.internal("ui//boxOutline.png"));
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
		noteSpawnList=new ArrayList<Rectangle>();
		BPMFrequency=60000/Integer.parseInt(map.getBPM());
		BPMFrequency*=1000000;
		pauseScreen=new Image(new Texture(Gdx.files.internal("ui//pause.png")));
		resumePNG=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//resume.png")));
		resumeButton=new ImageButton(resumePNG);
		resumeButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2+200);
		notesList=map.getNotesIntList();
		resumeButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resume();
            }
        });
		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
		//noteSpawnList=new ArrayList<>();
		if (mapPosition<notesList.length)
		{
			for (int i=0;i<4;i++)
			{
				Rectangle note=new Rectangle();
				//System.out.println(notesList[mapPosition][i]);
				if (i==0&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 0");
					note.x=Gdx.graphics.getWidth()/2-157;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 64;
					noteSpawnList.add(note);
				}
				else if (i==1&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 1");
					note.x=Gdx.graphics.getWidth()/2+53;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 64;
					noteSpawnList.add(note);
				}
				else if (i==2&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 2");
					note.x=Gdx.graphics.getWidth()/2+258;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 64;
					noteSpawnList.add(note);
				}
				else if (i==3&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 3");
					note.x=Gdx.graphics.getWidth()/2+465;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 64;
					noteSpawnList.add(note);
				}
				else
				{
					//System.out.println("no note spawned");
				}
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
			if(TimeUtils.nanoTime() - lastDropTime > BPMFrequency) spawnNote(); //spawner
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
			//System.out.println("user died");
		}
		else
		{
			//System.out.println("user won");
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
		stage.getBatch().draw(boxImg,box1.x,box1.y);
		stage.getBatch().draw(boxImg,box2.x,box2.y);
		stage.getBatch().draw(boxImg,box3.x,box3.y);
		stage.getBatch().draw(boxImg,box4.x,box4.y);
		for(Rectangle noteList: noteSpawnList) {  //spawns
			stage.getBatch().draw(noteImg, noteList.x, noteList.y);
		}
		if (gameState==0)
		{
			for (Iterator<Rectangle> iter = noteSpawnList.iterator(); iter.hasNext(); ) {  //moves blocks
				Rectangle note = iter.next();
				note.y -= 600 * Gdx.graphics.getDeltaTime();
				if(note.y + 90 < 0) iter.remove();
				if (note.overlaps(box1)) {
					combo++;
					System.out.print("box1");
					hitSound.play();
					iter.remove();
				}
			}
		}
		stage.getBatch().end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		if (Gdx.input.isKeyJustPressed(Input.Keys.D))
		{
			System.out.println("D was pressed on time");
		}
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
		pauseGroup.addActor(pauseScreen);
		pauseGroup.addActor(resumeButton);
		stage.addActor(pauseGroup);
		// if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
		// {
			// 	resume();
			// }
		// resumeButton.addListener(new ClickListener()
		// {
		// 	System.out.println("Resume button clicked");
		// });
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
		boxImg.dispose();
        holdNoteImg1.dispose();
        holdNoteImg2.dispose();
        holdNoteImg3.dispose();
		backingMusic.dispose();
		hitSound.dispose();
		holdSound.dispose();
    }
}
