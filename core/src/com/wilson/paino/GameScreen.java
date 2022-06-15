package com.wilson.paino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JOptionPane;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
public class GameScreen implements Screen, InputProcessor{
    Start game;

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
	public static int gameState=0; //0 for running, 1 for paused, 2 for game over, 3 for win
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
	public static boolean box1Active;
	public static boolean box2Active;
	public static boolean box3Active;
	public static boolean box4Active;
	Drawable resumePNG;
	Image pauseScreen;
	Texture boxImg;
	InputProcessor keyboardInputs;
	InputMultiplexer multiplexer;
	BitmapFont font;
	FreeTypeFontGenerator generator;
	TextureRegionDrawable quitPNG;
	//ImageButton quitButton;
	TextureRegionDrawable restartPNG;
	Image failScreen;
	Image winScreen;

    public GameScreen(final Start game)
    {
		// generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts//Play-Regular.ttf"));
		// FreeTypeFontParameter param = new FreeTypeFontParameter();
		// param. = 12;
		// font=generator.generateFont((FreeTypeFontParameter)param);
		// generator.dispose();
		// font.getData().setScale(25.0f);
        this.game=game;
        batch = new SpriteBatch();
		stage=new Stage(new ScreenViewport());
		keyboardInputs=new InputReader();
		multiplexer=new InputMultiplexer();
		multiplexer.addProcessor(keyboardInputs);
		multiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(multiplexer);
		bgImg = new Texture(Gdx.files.internal("ui//layout.png"));
		hpBar = new Texture(Gdx.files.internal("ui//HealthBar.png"));
		noteImg = new Texture(Gdx.files.internal("ui//BlueBox.png"));
		holdNoteImg1 = new Texture(Gdx.files.internal("ui//HoldB.png"));
		pauseOverlay = new Texture(Gdx.files.internal("ui//pause.png"));
		hpPos=0;
		box1=new Rectangle();
		box1.x=Gdx.graphics.getWidth()/2-157;
		box1.y=-10;
		box1.height=147;
		box1.width=173;
		box2=new Rectangle();
		box2.x=Gdx.graphics.getWidth()/2+53;
		box2.y=-10;
		box2.height=147;
		box2.width=173;
		box3=new Rectangle();
		box3.x=Gdx.graphics.getWidth()/2+258;
		box3.y=-10;
		box3.height=147;
		box3.width=173;
		box4=new Rectangle();
		box4.x=Gdx.graphics.getWidth()/2+465;
		box4.y=-10;
		box4.height=147;
		box4.width=173;
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
		backingMusic=Gdx.audio.newMusic(Gdx.files.internal("songs//song.mp3"));
		backingMusic.setVolume((float) 1);
		System.out.println(repeatLettersIterative("example")); //demonstrates the congruence between iteration and recursion
		System.out.println(repeatLettersRecursive("example")); //recursive method
		int[] example={1,2,3,4,5,6};
		System.out.println(multiplyBy2AndAddRecursive(example, example.length)); //recursive method with more than one base case and recursively traverse an array
		ArrayList<Integer> example2=new ArrayList<Integer>();
        example2.add(1); example2.add(2); example2.add(3); example2.add(4); example2.add(5); example2.add(6); 
		printReverseListRecursive(example2); //recursively traverse an arraylist
		int[] example3={213,1,2,13,2213,12,3,12,312,3,123,142,5647,5,2,435,42,659,56,85};
		System.out.println(binarySearch(example3,0,example3.length-1,3)); //uses binary search
		sort(example3,0,example3.length-1);
		System.out.println(Arrays.toString(example3)); //sorts an array using merge sort
		ArrayList<Integer> example4=new ArrayList<>();
		example4.add(123); example4.add(34); example4.add(436); example4.add(445645); example4.add(5347); example4.add(62);
		sort(example4, 0, example4.size()-1);
		System.out.println(example4);
		//map interpreter
		map=new MapInterpreter("map");
		//initializing methods
		initializeDuration();
		noteSpawnList=new ArrayList<Rectangle>();
		BPMFrequency=60000/Integer.parseInt(map.getBPM());
		BPMFrequency*=1000000;
		failScreen=new Image(new Texture(Gdx.files.internal("ui//fail.png")));
		winScreen=new Image(new Texture(Gdx.files.internal("ui//win.png")));
		pauseScreen=new Image(new Texture(Gdx.files.internal("ui//pause.png")));
		resumePNG=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//resume.png")));
		resumeButton=new ImageButton(resumePNG);
		quitPNG=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//quit.png")));
		restartPNG=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//restart.png")));
		restartButton=new ImageButton(restartPNG);
		exitButton=new ImageButton(quitPNG);
		notesList=map.getNotesIntList();
		resumeButton.addListener(new ClickListener()
        {
            public void clicked(InputEvent event, float x, float y)
            {
                resume();
            }
        });
		exitButton.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				dispose();
				System.exit(1);
				// GameScreen.this.game.setScreen(MainMenuScreen(GameScreen.this.game));
			}
		});
		restartButton.addListener(new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				System.out.println("restart button");
				restart();
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
					note.height = 24;
					noteSpawnList.add(note);
				}
				else if (i==1&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 1");
					note.x=Gdx.graphics.getWidth()/2+53;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 24;
					noteSpawnList.add(note);
				}
				else if (i==2&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 2");
					note.x=Gdx.graphics.getWidth()/2+258;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 24;
					noteSpawnList.add(note);
				}
				else if (i==3&&notesList[mapPosition][i]==1)
				{
					//System.out.println("case 3");
					note.x=Gdx.graphics.getWidth()/2+465;
					note.y = Gdx.graphics.getHeight()+100;
					note.width = 64;
					note.height = 24;
					noteSpawnList.add(note);
				}
				else
				{
				}
			}
		}
		mapPosition++;
		lastDropTime = TimeUtils.nanoTime();
	}

	public void fail()
	{
		backingMusic.pause();
		pauseGroup=new Group();
		pauseGroup.addActor(failScreen);
		exitButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2+70);
		restartButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2-160);
		pauseGroup.addActor(exitButton);
		pauseGroup.addActor(restartButton);
		stage.addActor(pauseGroup);
	}

	public void win()
	{
		backingMusic.pause();
		pauseGroup=new Group();
		pauseGroup.addActor(winScreen);
		exitButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2+70);
		restartButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2-160);
		pauseGroup.addActor(exitButton);
		pauseGroup.addActor(restartButton);
		stage.addActor(pauseGroup);
	}

	public void restart()
	{
		gameState=0;
		hpPos=0;
		backingMusic.stop();
		durationLeft=map.getDurationint();
		noteSpawnList=new ArrayList<>();
		frameCounter=0;
		mapPosition=0;
		backingMusic=Gdx.audio.newMusic(Gdx.files.internal("songs//song.mp3"));
		resume();
	}

    @Override
    public void show() {
		if (hpPos>-411&&durationLeft>=0) //checks if game over
		{
			backingMusic.play();
			if(TimeUtils.nanoTime() - lastDropTime > BPMFrequency) spawnNote(); //spawner
			if (frameCounter%60==0)	//loop to run every 60 frames or every 1 second
			{
				//hpPos-=100;
				frameCounter=0;
				durationLeft--;
				getDurationString();
				
			}
		}
		else if (hpPos<-411)
		{
			System.out.println("user died");
			gameState=2;
		}
		else if (durationLeft<0)
		{
			System.out.println("user won");
			gameState=3;
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
		stage.getBatch().draw(boxImg,box1.x,box1.y,box1.width,box1.height);
		stage.getBatch().draw(boxImg,box2.x,box2.y,box2.width,box2.height);
		stage.getBatch().draw(boxImg,box3.x,box3.y,box3.width,box3.height);
		stage.getBatch().draw(boxImg,box4.x,box4.y,box4.width,box4.height);
		//font.draw(stage.getBatch(), ""+combo, 100, 480);
		for(Rectangle noteList: noteSpawnList) {  //spawns
			stage.getBatch().draw(noteImg, noteList.x, noteList.y);
		}
		if (gameState==0)
		{
			Iterator<Rectangle> iter = noteSpawnList.iterator();
			while (iter.hasNext()) {
				Rectangle note = iter.next();
				note.y -= 800 * Gdx.graphics.getDeltaTime();
				if (note.y + 90 < 0)
				{
					iter.remove();
					hpPos-=10*Integer.parseInt(map.getDifficulty());
					combo=0;
				}
				if (note.overlaps(box1)&&box1Active) {
					combo++;
					if (hpPos<-(20/Integer.parseInt(map.getDifficulty())))
					{
						hpPos+=(20/Integer.parseInt(map.getDifficulty()));
					}
					else
					{
						hpPos=0;
					}
					//hitSound.play(0.2f);
					iter.remove();
					if (iter.hasNext())
					{
						iter.next();
					}
				}
				else if (note.overlaps(box2)&&box2Active)
				{
					combo++;
					if (hpPos<-(20/Integer.parseInt(map.getDifficulty())))
					{
						hpPos+=(20/Integer.parseInt(map.getDifficulty()));
					}
					else
					{
						hpPos=0;
					}
					System.out.println("box2");
					//hitSound.play(0.2f);
					iter.remove();
					if (iter.hasNext())
					{
						iter.next();
					}				
				}
				else if (note.overlaps(box3)&&box3Active)
				{
					combo++;
					if (hpPos<-(20/Integer.parseInt(map.getDifficulty())))
					{
						hpPos+=(20/Integer.parseInt(map.getDifficulty()));
					}
					else
					{
						hpPos=0;
					}
					System.out.println("box3");
					//hitSound.play(0.2f);
					iter.remove();
					if (iter.hasNext())
					{
						iter.next();
					}
				}
				else if (note.overlaps(box4)&&box4Active)
				{
					combo++;
					if (hpPos<-(20/Integer.parseInt(map.getDifficulty())))
					{
						hpPos+=(20/Integer.parseInt(map.getDifficulty()));
					}
					else
					{
						hpPos=0;
					}
					System.out.println("box4");
					//hitSound.play(0.2f);
					iter.remove();
					if (iter.hasNext())
					{
						iter.next();
					}
				}
			}
		}
		stage.getBatch().end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		switch (gameState)
		{
			case 0:
				show();
				break;
			case 1:
				pause();
				break;
			case 2:
				fail();
				break;
			case 3:
				win();
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
		exitButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2-180);
		restartButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2+10);
		resumeButton.setPosition(Gdx.graphics.getWidth()/2+45, Gdx.graphics.getHeight()/2+200);
		pauseGroup.addActor(resumeButton);
		pauseGroup.addActor(exitButton);
		pauseGroup.addActor(restartButton);
		stage.addActor(pauseGroup);
	}

    @Override
    public void resume() {
		gameState=0;
		pauseGroup.remove();
		show();
    }


    @Override
    public void hide() {
        backingMusic.pause();
		dispose();
    }

    @Override
    public void dispose() {
		System.out.println("Duration left: "+durationLeft);
		System.out.println("Map Position: "+mapPosition);
        batch.dispose();
		bgImg.dispose();
		hpBar.dispose();
        noteImg.dispose();
		boxImg.dispose();
        // holdNoteImg1.dispose();
        // holdNoteImg2.dispose();
        // holdNoteImg3.dispose();
		backingMusic.dispose();
		hitSound.dispose();
		holdSound.dispose();
    }

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}

	//requirements
	public static String repeatLettersRecursive(String str) //recursive method
    {
        String output="";
        if (str.length()>0)
        {
            output+=str.substring(0, 1);
            output+=str.substring(0,1);
            output+=repeatLettersRecursive(str.substring(1));
        }
        return output;
    }
	public static String repeatLettersIterative(String str) //Demonstrates the congruence between iteration and recursion.
    {
        String output="";
        for (int i=0;i<str.length();i++)
        {
            output+=str.substring(i, i+1);
            output+=str.substring(i,i+1);
        }
        return output;
    }
	public static int multiplyBy2AndAddRecursive(int[] arr,int index) //Recursively traverses an array.
    {
        int output=0;
        if (index>0)
        {
            output+=arr[index-1]*2;
            output+=multiplyBy2AndAddRecursive(arr, index-1);
        }
        return output;
    }
	public static void printReverseListRecursive(ArrayList<Integer> arr) //Recursively traverses an ArrayList.
    {
        ArrayList<Integer> arrCopy=new ArrayList<>();
        for (Integer i:arr)
        {
            arrCopy.add(i);
        }
        if (arrCopy.size()>0)
        {
            System.out.println(arrCopy.remove(arrCopy.size()-1));
            printReverseListRecursive(arrCopy);
        }
    }
	public static int binarySearch(int[] arr, int min,int max, int toFind) //Uses binary search, calls a recursive method with more than one base case.
    {
        int middle=(min+max)/2;
        if (min<=max)
        {
            if (arr[middle]==toFind)
            {
                return middle;
            }
            else if (arr[middle]>toFind)
            {
                return binarySearch(arr, min, middle-1, toFind);
            }
            else
            {
                return binarySearch(arr, middle+1, max, toFind);
            }
        }
        return -1;
    }
	public static void merge(int arr[], int l, int m, int r)
    {
        int n1 = m - l + 1;
        int n2 = r - m;
        int left[] = new int[n1];
        int right[] = new int[n2];
        for (int i = 0; i < n1; i++)
        {
            left[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j)
        {
            right[j] = arr[m + 1 + j];
        }
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2)
        {
        if (left[i] <= right[j])
        {
            arr[k] = left[i];
            i++;
        }
        else
        {
            arr[k] = right[j];
            j++;
        }
        k++;
        }
        while (i < n1)
        {
            arr[k] = left[i];
            i++;
            k++;
        }
        while (j < n2)
        {
            arr[k] = right[j];
            j++;
            k++;
        }
    }
    public static void sort(int arr[], int l, int r) //Sorts an array using merge sort.
    {
        if (l < r) 
        {
            int m =(l + r)/2;
            sort(arr, l, m);
            sort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }
	public static void merge(ArrayList<Integer> arr, int l, int m, int r)
    {
        int n1 = m - l + 1;
        int n2 = r - m;
        int left[] = new int[n1];
        int right[] = new int[n2];
        for (int i = 0; i < n1; i++)
        {
            left[i] = arr.get(l+i);
        }
        for (int j = 0; j < n2; ++j)
        {
            right[j] = arr.get(m + 1 + j);
        }
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2)
        {
        if (left[i] <= right[j])
        {
            arr.set(k,left[i]);
            i++;
        }
        else
        {
            arr.set(k, right[j]); 
            j++;
        }
        k++;
        }
        while (i < n1)
        {
            arr.set(k, left[i]);
            i++;
            k++;
        }
        while (j < n2)
        {
            arr.set(k, right[j]);
            j++;
            k++;
        }
    }
    public static void sort(ArrayList<Integer> arr, int l, int r) //Sorts an ArrayList using merge sort.
    {
        if (l < r) 
        {
            int m =(l + r)/2;
            sort(arr, l, m);
            sort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }
	
}
