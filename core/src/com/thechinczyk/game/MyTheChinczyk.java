package com.thechinczyk.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import screens.MainMenu;
import screens.dayParkMap;

public class MyTheChinczyk extends Game {

	public OrthographicCamera camera;
	public Viewport viewport;

	public int gameScreen;
	public int playerCount;

	public SpriteBatch batch;

	
	@Override
	public void create () {
		camera = new OrthographicCamera(1920,1080);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		batch = new SpriteBatch();
		gameScreen = 0;
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		if(gameScreen == 1){
			gameScreen = 0;
			this.setScreen(new dayParkMap(this));
		}
		else if(gameScreen == 2){
			gameScreen = 0;
			this.setScreen(new MainMenu(this));
		}
		else if(gameScreen == 3){
			gameScreen = 0;
			this.setScreen(new MainMenu(this));
		}

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}

	@Override
	public void resize (int width, int height) {
		viewport.update(width, height);
	}
}
