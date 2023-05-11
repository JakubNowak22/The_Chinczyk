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
    private float buttonExitHoveredAlpha;

    private GameObject buttonStart;
    private Texture buttonStartHovered;
    private Texture buttonStartClicked;
    private Sprite buttonStartHoveredSprite;
    private float buttonStartHoveredAlpha;

    public MainMenu(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        mainMenuBackground = new Texture("Menu/TC_Menu_MainBackground1.png");
        mainMenuBackgroundBlured = new Texture("Menu/TC_Menu_MainBackground1Blured.png");
        mainMenuBackgroundBluredSprite = spriteInit(mainMenuBackgroundBlured, 0, 0, 1920, 1080);
        mainMenuBackgroundBluredSpriteAlpha = 0;

        buttonExitHovered = new Texture("tempRed.png");
        buttonExit = new GameObject(buttonExitHovered, 1436, 331, 262, 97);
        buttonExitClicked = new Texture("tempBlue.png");
        buttonExitHoveredSprite = spriteInit(buttonExitHovered, 1436, 331, 262, 97);
        buttonExitHoveredAlpha = 0;

        buttonStartHovered = new Texture("tempBlue.png");
        buttonStartClicked = new Texture("tempRed.png");
        buttonStart = new GameObject(buttonStartHovered, 1408, 473, 330, 109);
        buttonStartHoveredSprite = spriteInit(buttonStartHovered, 1408, 473, 330, 109);
        buttonStartHoveredAlpha = 0;
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
        if(buttonStart.contains(cursorPosition) && !startPressed){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                startPressed = true;
                game.batch.draw(buttonStartClicked, 1408, 473,330,109);
            }
            else{
                if(buttonStartHoveredAlpha<0.85f) buttonStartHoveredSprite.setAlpha(buttonStartHoveredAlpha+=9f*delta);
                else buttonStartHoveredSprite.setAlpha(1);
                buttonStartHoveredSprite.draw(game.batch);
            }
        }
        else if(buttonStartHoveredAlpha>0){
            if(buttonStartHoveredAlpha>0.15f) buttonStartHoveredSprite.setAlpha(buttonStartHoveredAlpha-=9f*delta);
            else buttonStartHoveredSprite.setAlpha(buttonStartHoveredAlpha=0);
            buttonStartHoveredSprite.draw(game.batch);
        }

        //OBSŁUGA PRZYCISKU EXIT
        if(buttonExit.contains(cursorPosition) && !startPressed){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                Gdx.app.exit();
                game.batch.draw(buttonExitClicked, 1408, 473,330,109);
            }
            else{
                if(buttonExitHoveredAlpha<0.85f) buttonExitHoveredSprite.setAlpha(buttonExitHoveredAlpha+=9f*delta);
                else buttonExitHoveredSprite.setAlpha(1);
                buttonExitHoveredSprite.draw(game.batch);
            }
        }
        else if(buttonExitHoveredAlpha>0){
            if(buttonExitHoveredAlpha>0.15f) buttonExitHoveredSprite.setAlpha(buttonExitHoveredAlpha-=9f*delta);
            else buttonExitHoveredSprite.setAlpha(buttonExitHoveredAlpha=0);
            buttonExitHoveredSprite.draw(game.batch);
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
