package com.wilson.paino;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainMenuScreen extends Stage implements Screen {

    private boolean visible=true;
	final Start game;
	private OrthographicCamera camera;
    private Table table;
    private Image backgroundImg;
    private Texture bgTexture;
    ImageButton startButton;
    SpriteBatch sprites;
    Stage stage;

	public MainMenuScreen(final Start game) {
        
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		this.game = game;
        stage=new Stage();
        sprites=new SpriteBatch();
        table=new Table();
        table.setFillParent(true);
        table.center();
        Drawable startTexture=new TextureRegionDrawable(new Texture(Gdx.files.internal("ui//play.png")));
        startButton=new ImageButton(startTexture);
        stage.addActor(table);
        table.add(startButton);
        startButton.setBounds(561, 132, 636, 339);
        Gdx.input.setInputProcessor(stage);
        bgTexture=new Texture(Gdx.files.internal("ui//start.png"));
        backgroundImg=new Image(bgTexture);
        
	}

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		//game.font.draw(game.batch, "Welcome to Paino!!! ", 100, 150);
		//game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.draw(bgTexture, 0, 0);
		game.batch.end();
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        if (Gdx.input.isTouched()) {
			game.setScreen(new GameScreen(game));
			dispose();
		}
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
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
        // bgTexture.dispose();
        // stage.dispose();
        // sprites.dispose();
    }


        //...Rest of class omitted for succinctness.

}
