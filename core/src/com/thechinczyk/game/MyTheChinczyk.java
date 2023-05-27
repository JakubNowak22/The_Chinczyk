package com.thechinczyk.game;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thechinczyk.game.screens.ChooseGameSettings;
import com.thechinczyk.game.screens.MainMenu;
import com.thechinczyk.game.screens.DayParkMap;
import com.thechinczyk.game.screens.MenuLoadingScreen;

public class MyTheChinczyk extends Game {

	public OrthographicCamera camera;
	public Viewport viewport;

	public int gameScreen;
	public int playerCount;

	public SpriteBatch batch;

	public Music music;
	public Sound klik1;
	public Sound klik2;
	public Sound klik3;


	public com.thechinczyk.game.screens.ChooseGameSettings ChooseGameSettings;
	public MainMenu MainMenu;

	public DayParkMap dayParkMap;
	public com.thechinczyk.game.screens.MenuLoadingScreen MenuLoadingScreen;
	// public Game game alokacja w Loading Menu żeby nie marnować zasobów


	@Override
	public void create () {
		camera = new OrthographicCamera(1920,1080);
		camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
		viewport = new FitViewport(camera.viewportWidth, camera.viewportHeight, camera);
		batch = new SpriteBatch();
		gameScreen = 0;
		klik1 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik1.mp3"));
		klik2 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik2.mp3"));
		klik3 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik3.mp3"));
		music = Gdx.audio.newMusic(Gdx.files.internal("Menu/Menu_Music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);

		/*ChooseGameSettings = new ChooseGameSettings(this);
		MainMenu = new MainMenu(this);
		MenuLoadingScreen = new MenuLoadingScreen(this);
		this.setScreen(MainMenu);*/

		this.setScreen(new DayParkMap(this));
	}

	@Override
	public void render () {
		super.render();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

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
