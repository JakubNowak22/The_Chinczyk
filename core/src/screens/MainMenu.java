package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class MainMenu implements Screen {

    MyTheChinczyk game;

    private Vector2 cursorPosition;
    private boolean clickStartButton;

    private Texture mainMenuBackground;
    private Texture mainMenuBackgroundBlured;
    private Sprite mainMenuBackgroundBluredSprite;
    private float mainMenuBackgroundBluredSpriteAlpha;

    private TextureAtlas menuTransitionAtlas;
    private Animation<TextureRegion> menuTransitionAnim;
    private float elapsedTime;
    private Texture mainMenuBackground2;

    private GameObject buttonExit;
    private Texture buttonExitHovered;
    private Texture buttonExitClicked;
    private Sprite buttonExitHoveredSprite;

    private GameObject buttonStart;
    private Texture buttonStartHovered;
    private Texture buttonStartClicked;
    private Sprite buttonStartHoveredSprite;

    public MainMenu(MyTheChinczyk game) {
        this.game = game;
        this.loadTextures();

        game.music.play();
    }
    public void loadTextures(){
        mainMenuBackground = new Texture("Menu/TC_Menu_MainBackground1.png");
        mainMenuBackgroundBlured = new Texture("Menu/TC_Menu_MainBackground1Blured.png");
        mainMenuBackgroundBluredSprite = spriteInit(mainMenuBackgroundBlured, 0, 0, 1920, 1080);
        mainMenuBackgroundBluredSpriteAlpha = 0;

        menuTransitionAtlas = new TextureAtlas("Menu/Menu_Transition_Sheet/my_Menu_Transition_Sheet.atlas");
        menuTransitionAnim = new Animation<TextureRegion>(1f / 30f, menuTransitionAtlas.getRegions());
        mainMenuBackground2 = new Texture("Menu/TC_Menu_MainBackground2.png");

        buttonExitHovered = new Texture("Menu/TC_Menu_Exit_Hovered.png");
        buttonExit = new GameObject(buttonExitHovered, 1436, 331, 262, 97);
        buttonExitClicked = new Texture("Menu/TC_Menu_Exit_Clicked.png");
        buttonExitHoveredSprite = spriteInit(buttonExitHovered, 0, 0, 1920, 1080);

        buttonStartHovered = new Texture("Menu/TC_Menu_Start_Hovered.png");
        buttonStartClicked = new Texture("Menu/TC_Menu_Start_Clicked.png");
        buttonStart = new GameObject(buttonStartHovered, 1408, 473, 330, 109);
        buttonStartHoveredSprite = spriteInit(buttonStartHovered, 0, 0, 1920, 1080);
    }

    @Override
    public void show() {
        elapsedTime = 0f;
        clickStartButton = false;
    }

    @Override
    public void render(float delta) {
        game.batch.begin();

        game.batch.draw(mainMenuBackground, 0, 0, 1920, 1080);
        if (menuButtonFunc(buttonStart, buttonStartClicked, buttonStartHovered, Gdx.graphics.getDeltaTime(),
                buttonStartHoveredSprite, clickStartButton)) {
            clickStartButton = true;
        }
        if (menuButtonFunc(buttonExit, buttonExitClicked, buttonExitHovered, Gdx.graphics.getDeltaTime(),
                buttonExitHoveredSprite, clickStartButton)) {
            Gdx.app.exit();
        }

        /**
         * rozpoczęcie animacji przejścia z manu początkowego do menu wyboru ustawień
         * */
        if (clickStartButton && !menuTransitionAnim.isAnimationFinished(elapsedTime)) { // wywołanie animacji przejścia
            // z menu początkowego do menu wyboru ustwaień gry
            if (mainMenuBackgroundBluredSpriteAlpha < 0.9f) {
                mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha += 7f * Gdx.graphics.getDeltaTime());
                mainMenuBackgroundBluredSprite.draw(game.batch);
            } else { // trwanie animacji przejśica z menu początkowego do menu wyboru ustwaień gry
                mainMenuBackgroundBluredSprite.setAlpha(1);
                elapsedTime += Gdx.graphics.getDeltaTime();
                mainMenuBackgroundBluredSprite.draw(game.batch);
                game.batch.draw(menuTransitionAnim.getKeyFrame(elapsedTime, false), 455, 35, 1010, 1010);
            }

        } else if (clickStartButton) {
            this.hide();
            game.batch.draw(mainMenuBackground2, 0, 0, 1920, 1080); //to musi być żeby nie mrygnęlo podczas przejścia
            game.setScreen(game.ChooseGameSettings);
        }

        game.batch.end();
    }

    private Sprite spriteInit(Texture texture, float x, float y, float width, float height) {
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        return sprite;
    }

    private void getMousePosition() {
        Vector3 cursorPosition3 = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        cursorPosition = new Vector2(cursorPosition3.x, cursorPosition3.y);
    }

    private boolean menuButtonFunc(GameObject buttonObject, Texture clicked, Texture hovered, float delta,
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
                    sprite.setAlpha(buttonObject.alpha += 9f * delta);
                } else {
                    sprite.setAlpha(1);
                }
                sprite.setTexture(hovered);
                sprite.draw(game.batch);
            }
        } else if (buttonObject.alpha > 0) {
            if (buttonObject.alpha > 0.15f) {
                sprite.setAlpha(buttonObject.alpha -= 9f * delta);
            } else {
                sprite.setAlpha(buttonObject.alpha = 0);
            }
            sprite.draw(game.batch);
            buttonObject.soundPlayed = false;
        }
        return false;
    }

    @Override
    public void dispose() {
        mainMenuBackgroundBlured.dispose();
        mainMenuBackground.dispose();
        buttonStartHovered.dispose();
        buttonExitHovered.dispose();
        buttonStartClicked.dispose();
        buttonExitClicked.dispose();
        menuTransitionAtlas.dispose();
        mainMenuBackground2.dispose();
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
}
