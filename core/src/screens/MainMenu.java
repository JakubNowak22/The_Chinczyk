package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class MainMenu implements Screen{

    MyTheChinczyk game;

    private Vector2 cursorPosition;
    private boolean startPressed = false;

    private Texture mainMenuBackground;
    private Texture mainMenuBackgroundBlured;
    private Sprite mainMenuBackgroundBluredSprite;
    private float mainMenuBackgroundBluredSpriteAlpha;

    private GameObject buttonExit;
    private Texture buttonExitHovered;
    private Texture buttonExitClicked;
    private Sprite buttonExitHoveredSprite;

    private GameObject buttonStart;
    private Texture buttonStartHovered;
    private Texture buttonStartClicked;
    private Sprite buttonStartHoveredSprite;

    public MainMenu(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        mainMenuBackground = new Texture("Menu/TC_Menu_MainBackground1.png");
        mainMenuBackgroundBlured = new Texture("Menu/TC_Menu_MainBackground1Blured.png");
        mainMenuBackgroundBluredSprite = spriteInit(mainMenuBackgroundBlured, 0, 0, 1920, 1080);
        mainMenuBackgroundBluredSpriteAlpha = 0;

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
    public void render(float delta) {
        //ScreenUtils.clear(0, 0, 0, 1);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMousePosition();

        game.batch.begin();

        game.batch.draw(mainMenuBackground, 0, 0,1920,1080);

        //OBSŁUGA PRZYCISKU START
        if(menuButtonFunc(buttonStart,buttonStartClicked,buttonStartHovered, delta, buttonStartHoveredSprite)){
            startPressed = true;
        }

        //OBSŁUGA PRZYCISKU EXIT
        if(menuButtonFunc(buttonExit,buttonExitClicked,buttonExitHovered, delta, buttonExitHoveredSprite)){
            Gdx.app.exit();
        }

        //OBSŁUGA TŁA PO WCIŚNIĘCIU START
        if(startPressed){
            if(mainMenuBackgroundBluredSpriteAlpha<0.9f) mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha+=7f*delta);
            else mainMenuBackgroundBluredSprite.setAlpha(1);
            mainMenuBackgroundBluredSprite.draw(game.batch);
        }
        else if(mainMenuBackgroundBluredSpriteAlpha>0){
            if(mainMenuBackgroundBluredSpriteAlpha>0.15f) mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha-=7f*delta);
            else mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha=0);
            mainMenuBackgroundBluredSprite.draw(game.batch);
        }

        if(startPressed && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            startPressed = false;
        }
        game.batch.end();
    }

    private Sprite spriteInit(Texture texture, float x, float y, float width, float height){
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(x,y);
        sprite.setSize(width,height);
        return sprite;
    }

    private void getMousePosition(){
        Vector3 cursorPosition3 = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        cursorPosition = new Vector2(cursorPosition3.x, cursorPosition3.y);
    }

    private boolean menuButtonFunc(GameObject buttonObject,Texture clicked, Texture hovered, float delta, Sprite sprite){
        getMousePosition();
        if(buttonObject.contains(cursorPosition) && !startPressed){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                sprite.setTexture(clicked);
                sprite.draw(game.batch);
                return true;
            }
            else{
                if(buttonObject.alpha<0.85f) sprite.setAlpha(buttonObject.alpha+=9f*delta);
                else sprite.setAlpha(1);
                sprite.setTexture(hovered);
                sprite.draw(game.batch);
            }
        }
        else if(buttonObject.alpha>0){
            if(buttonObject.alpha>0.15f) sprite.setAlpha(buttonObject.alpha-=9f*delta);
            else sprite.setAlpha(buttonObject.alpha=0);
            sprite.draw(game.batch);
        }
        return false;
    }

    @Override
    public void dispose() {
        mainMenuBackground.dispose();
        mainMenuBackgroundBlured.dispose();
        buttonStartHovered.dispose();
        buttonExitHovered.dispose();
        buttonStartClicked.dispose();
        buttonExitClicked.dispose();
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
