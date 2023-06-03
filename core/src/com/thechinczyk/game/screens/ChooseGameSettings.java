package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class ChooseGameSettings implements Screen {
    MyTheChinczyk game;

    private Vector2 cursorPosition;
    private boolean startPressed = false;
    private boolean playPressed = false;
    private int chooseMapNumber;
    private int playerCount;

    private float elapsedTime;
    private Texture mainMenuBackground2;

    private GameObject buttonX;
    private Texture buttonXHovered;
    private Sprite buttonXHoveredSprite;
    private Texture buttonXClicked;

    private GameObject buttonMD;
    private Texture buttonMDHovered;
    private Sprite buttonMDHoveredSprite;
    private Texture buttonMDClicked;

    private GameObject buttonMN;
    private Texture buttonMNHovered;
    private Sprite buttonMNHoveredSprite;
    private Texture buttonMNClicked;

    private GameObject buttonP2;
    private Texture buttonP2Hovered;
    private Sprite buttonP2HoveredSprite;
    private Texture buttonP2Clicked;

    private GameObject buttonP3;
    private Texture buttonP3Hovered;
    private Sprite buttonP3HoveredSprite;
    private Texture buttonP3Clicked;

    private GameObject buttonP4;
    private Texture buttonP4Hovered;
    private Sprite buttonP4HoveredSprite;
    private Texture buttonP4Clicked;

    private GameObject buttonPLAY;
    private Texture buttonPLAYHovered;
    private Sprite buttonPLAYHoveredSprite;
    private Texture buttonPLAYClicked;

    private GameObject buttonExit;
    private Texture buttonExitHovered;
    private Texture buttonExitClicked;
    private Sprite buttonExitHoveredSprite;


    public ChooseGameSettings(MyTheChinczyk game) {
        this.game = game;
        mainMenuBackground2 = new Texture("Menu/TC_Menu_MainBackground2.png");

        buttonXHovered = new Texture("Menu/TC_Menu_MainBackground2_X_Hovered.png");
        buttonX = new GameObject(buttonXHovered, 1421, 855, 40, 52);
        buttonXHoveredSprite = spriteInit(buttonXHovered, 0, 0, 1920, 1080);
        buttonXClicked = new Texture("Menu/TC_Menu_MainBackground2_X_Clicked.png");

        buttonMDHovered = new Texture("Menu/TC_Menu_MainBackground2_MD_Hovered.png");
        buttonMD = new GameObject(buttonMDHovered, 455, 347, 502, 500);
        buttonMDHoveredSprite = spriteInit(buttonMDHovered, 0, 0, 1920, 1080);
        buttonMDClicked = new Texture("Menu/TC_Menu_MainBackground2_MD_Clicked.png");

        buttonMNHovered = new Texture("Menu/TC_Menu_MainBackground2_MN_Hovered.png");
        buttonMN = new GameObject(buttonMNHovered, 963, 345, 504, 505);
        buttonMNHoveredSprite = spriteInit(buttonMNHovered, 0, 0, 1920, 1080);
        buttonMNClicked = new Texture("Menu/TC_Menu_MainBackground2_MN_Clicked.png");

        buttonP2Hovered = new Texture("Menu/TC_Menu_MainBackground2_2P_Hovered.png");
        buttonP2 = new GameObject(buttonP2Hovered, 716, 226, 136, 93);
        buttonP2HoveredSprite = spriteInit(buttonP2Hovered, 0, 0, 1920, 1080);
        buttonP2Clicked = new Texture("Menu/TC_Menu_MainBackground2_2P_Clicked.png");

        buttonP3Hovered = new Texture("Menu/TC_Menu_MainBackground2_3P_Hovered.png");
        buttonP3 = new GameObject(buttonP3Hovered, 892, 229, 132, 93);
        buttonP3HoveredSprite = spriteInit(buttonP3Hovered, 0, 0, 1920, 1080);
        buttonP3Clicked = new Texture("Menu/TC_Menu_MainBackground2_3P_Clicked.png");

        buttonP4Hovered = new Texture("Menu/TC_Menu_MainBackground2_4P_Hovered.png");
        buttonP4 = new GameObject(buttonP4Hovered, 1073, 229, 134, 93);
        buttonP4HoveredSprite = spriteInit(buttonP4Hovered, 0, 0, 1920, 1080);
        buttonP4Clicked = new Texture("Menu/TC_Menu_MainBackground2_4P_Clicked.png");

        buttonPLAYHovered = new Texture("Menu/TC_Menu_MainBackground2_PLAY_Hovered.png");
        buttonPLAY = new GameObject(buttonPLAYHovered, 837, 108, 258, 77);
        buttonPLAYHoveredSprite = spriteInit(buttonPLAYHovered, 0, 0, 1920, 1080);
        buttonPLAYClicked = new Texture("Menu/TC_Menu_MainBackground2_PLAY_Clicked.png");

        buttonExitHovered = new Texture("Menu/TC_Menu_Exit_Hovered.png");
        buttonExit = new GameObject(buttonExitHovered, 1436, 331, 262, 97);
        buttonExitClicked = new Texture("Menu/TC_Menu_Exit_Clicked.png");
        buttonExitHoveredSprite = spriteInit(buttonExitHovered, 0, 0, 1920, 1080);
    }


    private boolean menuButtonFunc(GameObject buttonObject, Texture clicked, Texture hovered,
                                   Sprite sprite, boolean started) {
        getMousePosition();
        if (buttonObject.contains(cursorPosition) && !started) {
            if (!buttonObject.soundPlayed) {
                game.klik1.play(0.25f);
                buttonObject.soundPlayed = true;
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                game.klik2.play(0.4f);
                sprite.setTexture(clicked);
                sprite.draw(game.batch);
                return true;
            } else {
                if (buttonObject.alpha < 0.85f) {
                    sprite.setAlpha(buttonObject.alpha += 9f * Gdx.graphics.getDeltaTime());
                } else {
                    sprite.setAlpha(1);
                }
                sprite.setTexture(hovered);
                sprite.draw(game.batch);
            }
        } else if (buttonObject.alpha > 0) {
            if (buttonObject.alpha > 0.15f) {
                sprite.setAlpha(buttonObject.alpha -= 9f * Gdx.graphics.getDeltaTime());
            } else {
                sprite.setAlpha(buttonObject.alpha = 0);
            }
            sprite.draw(game.batch);
            buttonObject.soundPlayed = false;
        }
        return false;
    }

    private Sprite spriteInit(Texture texture, float x, float y, float width, float height) {
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        return sprite;
    }

    @Override
    public void show() {
        chooseMapNumber = 0;
        playerCount = 0;
        elapsedTime = 0f;
    }

    private void getMousePosition() {
        Vector3 cursorPosition3 = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        cursorPosition = new Vector2(cursorPosition3.x, cursorPosition3.y);
    }

    @Override
    public void render(float delta) {
        game.batch.begin();
        game.batch.draw(mainMenuBackground2, 0, 0, 1920, 1080);
        if (menuButtonFunc(buttonX, buttonXClicked, buttonXHovered,
                buttonXHoveredSprite, false) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // klikniecie X
            elapsedTime = 0;
            chooseMapNumber = 0;
            playerCount = 0;
            game.setScreen(game.MainMenu);
        }
        if (chooseMapNumber != 0 && playerCount != 0) {
            game.batch.draw(buttonPLAYClicked, 0, 0, 1920, 1080);
            if (menuButtonFunc(buttonPLAY, buttonPLAYClicked, buttonPLAYHovered,
                    buttonPLAYHoveredSprite, false)) {
                game.klik3.play(0.4f);
                if (game.music.isPlaying()) {
                    game.music.setLooping(false);
                    game.music.stop();
                    game.music.dispose();
                }
                game.gameScreen = chooseMapNumber;
                game.playerCount = playerCount;
                game.MenuLoadingScreen.setPlayerCount(playerCount);
                game.setScreen(game.MenuLoadingScreen);
            }
        }
        selectSettings();
        drawSelectedSettings();

        game.batch.end();
    }

    private void drawSelectedSettings() {
        if (chooseMapNumber == 1) {
            game.batch.draw(buttonMDClicked, 0, 0, 1920, 1080);
        } else if (chooseMapNumber == 2) {
            game.batch.draw(buttonMNClicked, 0, 0, 1920, 1080);
        }

        if (playerCount == 2) {
            game.batch.draw(buttonP2Clicked, 0, 0, 1920, 1080);
        } else if (playerCount == 3) {
            game.batch.draw(buttonP3Clicked, 0, 0, 1920, 1080);
        } else if (playerCount == 4) {
            game.batch.draw(buttonP4Clicked, 0, 0, 1920, 1080);
        }
    }

    private void selectSettings() {
        if (menuButtonFunc(buttonMD, buttonMDClicked, buttonMDHovered,
                buttonMDHoveredSprite, false)) {
            chooseMapNumber = 1;
        }
        if (menuButtonFunc(buttonMN, buttonMNClicked, buttonMNHovered,
                buttonMNHoveredSprite, false)) {
            chooseMapNumber = 2;
        }
        if (menuButtonFunc(buttonP2, buttonP2Clicked, buttonP2Hovered,
                buttonP2HoveredSprite, false)) {
            playerCount = 2;
        }
        if (menuButtonFunc(buttonP3, buttonP3Clicked, buttonP3Hovered,
                buttonP3HoveredSprite, false)) {
            playerCount = 3;
        }
        if (menuButtonFunc(buttonP4, buttonP4Clicked, buttonP4Hovered,
                buttonP4HoveredSprite, false)) {
            playerCount = 4;
        }
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
        buttonExitHovered.dispose();
        buttonExitClicked.dispose();
        mainMenuBackground2.dispose();
        buttonXHovered.dispose();
        buttonXClicked.dispose();
        buttonMDHovered.dispose();
        buttonMDClicked.dispose();
        buttonMNHovered.dispose();
        buttonMNClicked.dispose();
        buttonP2Clicked.dispose();
        buttonP3Clicked.dispose();
        buttonP4Clicked.dispose();
        buttonP2Hovered.dispose();
        buttonP3Hovered.dispose();
        buttonP4Hovered.dispose();
        buttonPLAYClicked.dispose();
        buttonPLAYHovered.dispose();
    }
}