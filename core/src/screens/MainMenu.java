package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class MainMenu implements Screen{

    MyTheChinczyk game;

    private boolean startPressed = false;

    private Texture mainMenuBackground;
    private Texture mainMenuBackgroundBlured;
    private Sprite mainMenuBackgroundBluredSprite;
    private float mainMenuBackgroundBluredSpriteAlpha;
    private Texture tempTexture;
    private GameObject buttonExit;
    private GameObject buttonStart;

    public MainMenu(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        mainMenuBackground = new Texture("Menu/TC_Menu_MainBackground1.png");
        mainMenuBackgroundBlured = new Texture("Menu/TC_Menu_MainBackground1Blured.png");
        tempTexture = new Texture("tempRed.png");
        buttonExit = new GameObject(tempTexture, 1436, 331, 262, 97);
        buttonStart = new GameObject(tempTexture, 1408, 473, 330, 109);
        mainMenuBackgroundBluredSprite = new Sprite(mainMenuBackgroundBlured);
        mainMenuBackgroundBluredSprite.setPosition(0,0);
        mainMenuBackgroundBluredSprite.setSize(1920,1080);
        mainMenuBackgroundBluredSpriteAlpha = 0;
    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(0, 0, 0, 1);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Vector3 cursorPosition3 = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 cursorPosition = new Vector2(cursorPosition3.x, cursorPosition3.y);

        game.batch.begin();

        game.batch.draw(mainMenuBackground, 0, 0,1920,1080);
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


        if(buttonStart.contains(cursorPosition) && !startPressed){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                startPressed = true;
            }
        }
        else if(buttonExit.contains(cursorPosition) && !startPressed){
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                Gdx.app.exit();
            }
        }

        if(startPressed && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            startPressed = false;
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
        mainMenuBackground.dispose();
        mainMenuBackgroundBlured.dispose();
        tempTexture.dispose();
    }
}
