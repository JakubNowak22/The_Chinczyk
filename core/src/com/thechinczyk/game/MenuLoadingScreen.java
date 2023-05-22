package com.thechinczyk.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import jdk.tools.jmod.Main;
import screens.DayParkMap;
import screens.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MenuLoadingScreen implements Screen {
    /**
     * podczas ładowania tego okna czyli jego wyświetlania uruchomimym wątek którty
     * posłuży do zalokowania wszytkich assetów poprzez przxekazanie referencji do konstuktora
     * oraz zwolnimy zasoby które zostały zalokowane przy poprzednich ekranach ładujących(MainMenu oraz ChooseGameSettings)
     */
    MyTheChinczyk game;
    Thread thread;
    private int chooseMapNumber;
    private int playerCount;
    private Texture loadingBackground;
    private Sprite loadingBackgroundSprite;
    private float loadingBackgroundSpriteAlpha;

    private final Texture dayParkBackground = new Texture("Map1/TC_Map1_Main.png");

    boolean disposeFlag = false;

    void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    void setChooseMapNumber(int chooseMapNumber) {
        this.chooseMapNumber = chooseMapNumber;
    }

    MenuLoadingScreen(MyTheChinczyk game) {
        this.game = game;
        loadingBackground = new Texture("TC_Loading_Screen.png");
        loadingBackgroundSprite = spriteInit(loadingBackground, 0, 0, 1920, 1080);
        loadingBackgroundSpriteAlpha = 0;
    }

    private Sprite spriteInit(Texture texture, float x, float y, float width, float height) {
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        return sprite;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        game.batch.begin();

        if (loadingBackgroundSpriteAlpha < 0.9f) {
            loadingBackgroundSprite.setAlpha(loadingBackgroundSpriteAlpha += 7f * Gdx.graphics.getDeltaTime());
            loadingBackgroundSprite.draw(game.batch);
        } else {
            loadingBackgroundSprite.setAlpha(1);
            loadingBackgroundSprite.draw(game.batch);
            game.dayParkMap = new DayParkMap(game);
            game.setScreen(game.dayParkMap);
        }

        /*if (animationEnd) {
            game.batch.draw(dayParkBackground, 0, 0, 1920, 1080);
            if (loadingBackgroundSpriteAlpha > 0.15f) {
                loadingBackgroundSprite.setAlpha(loadingBackgroundSpriteAlpha -= 2f * Gdx.graphics.getDeltaTime());
            } else {
                loadingBackgroundSprite.setAlpha(loadingBackgroundSpriteAlpha = 0);
                game.setScreen(game.dayParkMap);
            }
            loadingBackgroundSprite.draw(game.batch);
        }*/ //może kiedyś

        if (!disposeFlag) {
            game.ChooseGameSettings.dispose();
            game.MainMenu.dispose();
            disposeFlag = true;
        }
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

class AllocateTextureForTheGame implements Runnable {

    MyTheChinczyk game;

    AllocateTextureForTheGame(MyTheChinczyk game) {
        this.game = game;
    }

    @Override
    public void run() {
        /***
         *alokacja do game.game odpowiednich zasobów
         * WAŻNE!!!!!!!
         */
    }
}
