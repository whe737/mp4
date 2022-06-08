package com.wilson.paino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends Stage implements Screen {

    private boolean visible=true;
	Start game;
	private OrthographicCamera camera;
    private Table table;
    private Stage stage;
    private Image backgroundImg;
    private Texture bgTexture;
    ImageButton closeButton;
    ImageButton startButton;
    SpriteBatch batch;

	public MainMenuScreen(Start game) {
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1030);
		this.game = game;
        stage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        batch=new SpriteBatch();
        bgTexture=new Texture(Gdx.files.internal("ui//start.png"));
        backgroundImg=new Image(bgTexture);
        
	}


    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.5f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bgTexture, 0, -30);
        batch.end();
	    stage.act(Gdx.graphics.getDeltaTime());
	    stage.draw();
        if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        System.out.println("main menu disposed");
        // bgTexture.dispose();
        stage.dispose();
        batch.dispose();
    }


        //...Rest of class omitted for succinctness.

}
