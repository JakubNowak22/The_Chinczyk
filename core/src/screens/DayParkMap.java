package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.GameObject;
import com.thechinczyk.game.MyTheChinczyk;

public class DayParkMap implements Screen {

    MyTheChinczyk game;

    private Texture dayParkBackground;
    private Texture dayParkTopground;


    private TextureAtlas yellowPlayer1Atlas;
    private Animation<TextureRegion> yellowPlayer1Anim;
    private float yellowPlayer1ElapsedTime;
    private int yellowPlayer1AnimStarted;


    private TextureAtlas iceCreamAtlas;
    private Animation<TextureRegion> iceCreamAnim;
    private float loopElapsedTime;
    private TextureAtlas swingAtlas;
    private Animation<TextureRegion> swingAnim;

    private TextureAtlas cardAtlas;
    private Animation<TextureRegion> cardAnim;
    private float cardElapsedTime;
    private boolean cardAnimStarted;
    private Texture cardMessage1;

    private TextureAtlas yellowBusAtlas;
    private Animation<TextureRegion> yellowBusAnim;
    private float yellowBusElapsedTime;
    private boolean yellowBusAnimStarted;

    public DayParkMap(MyTheChinczyk game){
        this.game = game;
    }

    @Override
    public void show() {
        dayParkBackground = new Texture("Map1/TC_Map1_Main.png");
        dayParkTopground = new Texture("Map1/TC_Map1_TopLayer.png");


        yellowPlayer1Atlas = new TextureAtlas("Map1/YellowPlayerAnimSheet/YellowPlayerAnimSheet.atlas");
        yellowPlayer1Anim = new Animation<TextureRegion>(1f/30f, yellowPlayer1Atlas.getRegions());
        yellowPlayer1ElapsedTime = 0f;
        yellowPlayer1AnimStarted = 0;


        iceCreamAtlas = new TextureAtlas("Map1/IceCreamAnimationSheet/myIceCreamAnimationSheet.atlas");
        iceCreamAnim = new Animation<TextureRegion>(1f/30f, iceCreamAtlas.getRegions());
        loopElapsedTime = 0f;
        swingAtlas = new TextureAtlas("Map1/SwingAnimSheet/SwingAnimSheet.atlas");
        swingAnim = new Animation<TextureRegion>(1f/30f, swingAtlas.getRegions());

        cardAtlas = new TextureAtlas("Map1/CardAnimSheet/CardAnimSheet.atlas");
        cardAnim = new Animation<TextureRegion>(1f/30f, cardAtlas.getRegions());
        cardElapsedTime = 0f;
        cardAnimStarted = false;
        cardMessage1 = new Texture("Map1/TC_Map1_Message1.png");

        yellowBusAtlas = new TextureAtlas("Map1/YellowBusAnimSheet/YellowBusAnimSheet.atlas");
        yellowBusAnim = new Animation<TextureRegion>(1f/30f, yellowBusAtlas.getRegions());
        yellowBusElapsedTime = 0f;
        yellowBusAnimStarted = false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        //Wyświetlenie głównego tła planszy
        game.batch.draw(dayParkBackground, 0, 0, 1920, 1080);
        //Animacja rożka z lodem i huśtawki na planszy
        loopElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(iceCreamAnim.getKeyFrame(loopElapsedTime, true), 0, 888, 179, 192);
        game.batch.draw(swingAnim.getKeyFrame(loopElapsedTime, true), 1009, 137, 199, 95);

        //Powrót do menu głównego
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.gameScreen = 3;
        }

        //Przykładowa obsługa animacji busa z zółtym pionkiem
        if(Gdx.input.isKeyJustPressed(Input.Keys.B) && !yellowBusAnimStarted){
            yellowBusAnimStarted = true;
        }
        else if(yellowBusAnim.isAnimationFinished(yellowBusElapsedTime) && yellowBusAnimStarted){
            yellowBusAnimStarted = false;
            yellowBusElapsedTime = 0;
        }
        if(yellowBusAnimStarted) {
            yellowBusElapsedTime += Gdx.graphics.getDeltaTime();
        }
        game.batch.draw(yellowBusAnim.getKeyFrame(yellowBusElapsedTime, false), 1015, 0, 905, 1080);


        //Przykład poruszania się pionkiem
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            //Tutaj dodaje żeby wymusić wykonanie pierwszej klatki
            yellowPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
            yellowPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
            yellowPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if(yellowPlayer1Anim.getKeyFrameIndex(yellowPlayer1ElapsedTime)%10!=0){
            //To się wykonuje aż nie zrobi się 10 klatek, tyle trwa przesunięcie o jedno pole
            yellowPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if(yellowPlayer1ElapsedTime < 8.03f || yellowPlayer1ElapsedTime > 16.35f){
            //Animacje w tym muszą mieć małą rozdzielczość więc podzieliłem ekran na dwa i
            //w zależności gdzie jest pionek, jego animacja musi być albo po lewo (ten if) albo po prawo (następny if)
            game.batch.draw(yellowPlayer1Anim.getKeyFrame(yellowPlayer1ElapsedTime, false), 0, 0, 1080, 1080);
        }
        else{
            game.batch.draw(yellowPlayer1Anim.getKeyFrame(yellowPlayer1ElapsedTime, false), 840, 0, 1080, 1080);
        }


        //Przykładowa obsługa animacji karty
        if(Gdx.input.isKeyJustPressed(Input.Keys.C) && !cardAnimStarted){
            //Wysunięcie karty
            cardAnimStarted = true;
        }
        else if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && cardAnim.isAnimationFinished(cardElapsedTime)){
            //Zamknięcie karty
            cardAnimStarted = false;
            cardElapsedTime = 0;
        }
        if(cardAnimStarted) {
            cardElapsedTime += Gdx.graphics.getDeltaTime();
            game.batch.draw(cardAnim.getKeyFrame(cardElapsedTime, false), 304, 71, 1270, 938);
            if(cardElapsedTime>1.5f){
                //Pojawienie się tekstu w momencie gdy karta się obraca
                game.batch.draw(cardMessage1, 0, 0, 1920, 1080);
            }
        }

        //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
        game.batch.draw(dayParkTopground, 0, 0, 1920, 1080);

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
        iceCreamAtlas.dispose();
        swingAtlas.dispose();
        cardAtlas.dispose();
        yellowBusAtlas.dispose();
        yellowPlayer1Atlas.dispose();

        dayParkBackground.dispose();
        dayParkTopground.dispose();
        cardMessage1.dispose();
    }
}
