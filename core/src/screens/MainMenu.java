package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class MainMenu implements Screen{

    MyTheChinczyk game;

    private Vector2 cursorPosition;
    private boolean startPressed = false;
    private boolean playPressed = false;
    private int mapNo;
    private int playerCount;

    private Music music;
    private Sound klik1;
    private Sound klik2;
    private Sound klik3;

    private Texture mainMenuBackground;
    private Texture mainMenuBackgroundBlured;
    private Sprite mainMenuBackgroundBluredSprite;
    private float mainMenuBackgroundBluredSpriteAlpha;

    private Texture loadingBackground;
    private Sprite loadingBackgroundSprite;
    private float loadingBackgroundSpriteAlpha;

    private TextureAtlas menuTransitionAtlas;
    private Animation<TextureRegion> menuTransitionAnim;
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

    private GameObject buttonStart;
    private Texture buttonStartHovered;
    private Texture buttonStartClicked;
    private Sprite buttonStartHoveredSprite;

    public MainMenu(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        mapNo = 0;
        playerCount = 0;

        klik1 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik1.mp3"));
        klik2 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik2.mp3"));
        klik3 = Gdx.audio.newSound(Gdx.files.internal("Menu/klik3.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Menu/Menu_Music.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);

        mainMenuBackground = new Texture("Menu/TC_Menu_MainBackground1.png");
        mainMenuBackgroundBlured = new Texture("Menu/TC_Menu_MainBackground1Blured.png");
        mainMenuBackgroundBluredSprite = spriteInit(mainMenuBackgroundBlured, 0, 0, 1920, 1080);
        mainMenuBackgroundBluredSpriteAlpha = 0;

        loadingBackground = new Texture("TC_Loading_Screen.png");
        loadingBackgroundSprite = spriteInit(loadingBackground, 0, 0, 1920, 1080);
        loadingBackgroundSpriteAlpha = 0;

        menuTransitionAtlas = new TextureAtlas("Menu/Menu_Transition_Sheet/my_Menu_Transition_Sheet.atlas");
        menuTransitionAnim = new Animation<TextureRegion>(1f/30f, menuTransitionAtlas.getRegions());
        elapsedTime = 0f;
        mainMenuBackground2 = new Texture("Menu/TC_Menu_MainBackground2.png");

        buttonXHovered = new Texture("Menu/TC_Menu_MainBackground2_X_Hovered.png");
        buttonX = new GameObject(buttonXHovered,1421 , 855, 40, 52);
        buttonXHoveredSprite = spriteInit(buttonXHovered, 0, 0, 1920, 1080);
        buttonXClicked = new Texture("Menu/TC_Menu_MainBackground2_X_Clicked.png");

        buttonMDHovered = new Texture("Menu/TC_Menu_MainBackground2_MD_Hovered.png");
        buttonMD = new GameObject(buttonMDHovered,455 , 347, 502, 500);
        buttonMDHoveredSprite = spriteInit(buttonMDHovered, 0, 0, 1920, 1080);
        buttonMDClicked = new Texture("Menu/TC_Menu_MainBackground2_MD_Clicked.png");

        buttonMNHovered = new Texture("Menu/TC_Menu_MainBackground2_MN_Hovered.png");
        buttonMN = new GameObject(buttonMNHovered,963 , 345, 504, 505);
        buttonMNHoveredSprite = spriteInit(buttonMNHovered, 0, 0, 1920, 1080);
        buttonMNClicked = new Texture("Menu/TC_Menu_MainBackground2_MN_Clicked.png");

        buttonP2Hovered = new Texture("Menu/TC_Menu_MainBackground2_2P_Hovered.png");
        buttonP2 = new GameObject(buttonP2Hovered,716 , 226, 136, 93);
        buttonP2HoveredSprite = spriteInit(buttonP2Hovered, 0, 0, 1920, 1080);
        buttonP2Clicked = new Texture("Menu/TC_Menu_MainBackground2_2P_Clicked.png");

        buttonP3Hovered = new Texture("Menu/TC_Menu_MainBackground2_3P_Hovered.png");
        buttonP3 = new GameObject(buttonP3Hovered,892 , 229, 132, 93);
        buttonP3HoveredSprite = spriteInit(buttonP3Hovered, 0, 0, 1920, 1080);
        buttonP3Clicked = new Texture("Menu/TC_Menu_MainBackground2_3P_Clicked.png");

        buttonP4Hovered = new Texture("Menu/TC_Menu_MainBackground2_4P_Hovered.png");
        buttonP4 = new GameObject(buttonP4Hovered,1073 , 229, 134, 93);
        buttonP4HoveredSprite = spriteInit(buttonP4Hovered, 0, 0, 1920, 1080);
        buttonP4Clicked = new Texture("Menu/TC_Menu_MainBackground2_4P_Clicked.png");

        buttonPLAYHovered = new Texture("Menu/TC_Menu_MainBackground2_PLAY_Hovered.png");
        buttonPLAY = new GameObject(buttonPLAYHovered,837 , 108, 258, 77);
        buttonPLAYHoveredSprite = spriteInit(buttonPLAYHovered, 0, 0, 1920, 1080);
        buttonPLAYClicked = new Texture("Menu/TC_Menu_MainBackground2_PLAY_Clicked.png");

        buttonExitHovered = new Texture("Menu/TC_Menu_Exit_Hovered.png");
        buttonExit = new GameObject(buttonExitHovered, 1436, 331, 262, 97);
        buttonExitClicked = new Texture("Menu/TC_Menu_Exit_Clicked.png");
        buttonExitHoveredSprite = spriteInit(buttonExitHovered, 0, 0, 1920, 1080);

        buttonStartHovered = new Texture("Menu/TC_Menu_Start_Hovered.png");
        buttonStartClicked = new Texture("Menu/TC_Menu_Start_Clicked.png");
        buttonStart = new GameObject(buttonStartHovered, 1408, 473, 330, 109);
        buttonStartHoveredSprite = spriteInit(buttonStartHovered, 0, 0, 1920, 1080);

        music.play();
    }

    @Override
    public void render(float delta) {
        //ScreenUtils.clear(0, 0, 0, 1);
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getMousePosition();

        game.batch.begin();

        if(playPressed){
            if(loadingBackgroundSpriteAlpha<0.9f){
                game.batch.draw(mainMenuBackground2, 0, 0, 1920, 1080);
                loadingBackgroundSprite.setAlpha(loadingBackgroundSpriteAlpha+=7f*Gdx.graphics.getDeltaTime());
                loadingBackgroundSprite.draw(game.batch);
            }
            else if(loadingBackgroundSpriteAlpha == 1){
                game.playerCount = playerCount;
                game.gameScreen = mapNo;
            }
            else{
                game.batch.draw(loadingBackground, 0, 0,1920,1080);
                loadingBackgroundSpriteAlpha = 1;
            }
        }
        else{
            game.batch.draw(mainMenuBackground, 0, 0,1920,1080);
            if (menuButtonFunc(buttonStart, buttonStartClicked, buttonStartHovered, Gdx.graphics.getDeltaTime(), buttonStartHoveredSprite, startPressed)) {
                startPressed = true;
            }
            if (menuButtonFunc(buttonExit, buttonExitClicked, buttonExitHovered, Gdx.graphics.getDeltaTime(), buttonExitHoveredSprite, startPressed)) {
                Gdx.app.exit();
            }
            if (startPressed && !menuTransitionAnim.isAnimationFinished(elapsedTime)) {
                if (mainMenuBackgroundBluredSpriteAlpha < 0.9f) {
                    mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha += 7f * Gdx.graphics.getDeltaTime());
                    mainMenuBackgroundBluredSprite.draw(game.batch);
                }
                else {
                    mainMenuBackgroundBluredSprite.setAlpha(1);
                    elapsedTime += Gdx.graphics.getDeltaTime();
                    mainMenuBackgroundBluredSprite.draw(game.batch);
                    game.batch.draw(menuTransitionAnim.getKeyFrame(elapsedTime, false), 455, 35, 1010, 1010);
                }
            }
            else if (startPressed) {
                game.batch.draw(mainMenuBackground2, 0, 0, 1920, 1080);
                if (menuButtonFunc(buttonX, buttonXClicked, buttonXHovered, Gdx.graphics.getDeltaTime(), buttonXHoveredSprite, !startPressed) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    elapsedTime = 0;
                    mapNo = 0;
                    playerCount = 0;
                    startPressed = false;
                }
                if(mapNo!=0 && playerCount !=0){
                    game.batch.draw(buttonPLAYClicked, 0, 0, 1920, 1080);
                    if(menuButtonFunc(buttonPLAY,buttonPLAYClicked,buttonPLAYHovered, Gdx.graphics.getDeltaTime(), buttonPLAYHoveredSprite, !startPressed)){
                        klik3.play(0.4f);
                        if(music.isPlaying()){
                            music.setLooping(false);
                            music.stop();
                            music.dispose();
                        }
                        playPressed = true;
                    }
                }
                if (menuButtonFunc(buttonMD, buttonMDClicked, buttonMDHovered, Gdx.graphics.getDeltaTime(), buttonMDHoveredSprite, !startPressed)) {
                    mapNo = 1;
                }
                if (menuButtonFunc(buttonMN, buttonMNClicked, buttonMNHovered, Gdx.graphics.getDeltaTime(), buttonMNHoveredSprite, !startPressed)) {
                    mapNo = 2;
                }
                if (menuButtonFunc(buttonP2, buttonP2Clicked, buttonP2Hovered, Gdx.graphics.getDeltaTime(), buttonP2HoveredSprite, !startPressed)) {
                    playerCount = 2;
                }
                if (menuButtonFunc(buttonP3, buttonP3Clicked, buttonP3Hovered, Gdx.graphics.getDeltaTime(), buttonP3HoveredSprite, !startPressed)) {
                    playerCount = 3;
                }
                if (menuButtonFunc(buttonP4, buttonP4Clicked, buttonP4Hovered, Gdx.graphics.getDeltaTime(), buttonP4HoveredSprite, !startPressed)) {
                    playerCount = 4;
                }
            }
            else if (mainMenuBackgroundBluredSpriteAlpha > 0) {
                if (mainMenuBackgroundBluredSpriteAlpha > 0.15f)
                    mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha -= 7f * Gdx.graphics.getDeltaTime());
                else mainMenuBackgroundBluredSprite.setAlpha(mainMenuBackgroundBluredSpriteAlpha = 0);
                mainMenuBackgroundBluredSprite.draw(game.batch);
            }

            if (mapNo == 1) {
                game.batch.draw(buttonMDClicked, 0, 0, 1920, 1080);
            } else if (mapNo == 2) {
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

    private boolean menuButtonFunc(GameObject buttonObject,Texture clicked, Texture hovered, float delta, Sprite sprite, boolean started){
        getMousePosition();
        if(buttonObject.contains(cursorPosition) && !started){
            if(!buttonObject.soundPlayed){
                klik1.play(0.25f);
                buttonObject.soundPlayed=true;
            }
            if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                klik2.play(0.4f);
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
            buttonObject.soundPlayed=false;
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
        menuTransitionAtlas.dispose();
        mainMenuBackground2.dispose();
        buttonXHovered.dispose();
        buttonXClicked.dispose();
        loadingBackground.dispose();
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
        klik1.dispose();
        klik2.dispose();
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
