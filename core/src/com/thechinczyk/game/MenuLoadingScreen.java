package com.thechinczyk.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import jdk.tools.jmod.Main;
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

    private Texture loadingBackground;
    private Sprite loadingBackgroundSprite;
    private float loadingBackgroundSpriteAlpha;
    boolean disposeFlag = false;

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
        thread = new Thread(new AllocateTextureForTheGame(game));
        thread.start();
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(loadingBackground, 0, 0, 1920, 1080);
        if(!disposeFlag){
            game.ChooseGameSettings.dispose();
            game.MainMenu.dispose();
            disposeFlag = true;
        }
        //game.setScreen(Game); to potem będzie
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
    AllocateTextureForTheGame(MyTheChinczyk game){
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
