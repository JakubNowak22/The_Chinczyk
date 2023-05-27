package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;
import org.lwjgl.Sys;

import java.util.Random;

public class DayParkMap implements Screen {

    MyTheChinczyk game;
    int turnSignKeyFrame;
    Random rand;
    double diceRoll;

    int yellowPawnsInBase;
    int bluePawnsInBase;
    int greenPawnsInBase;
    int pinkPawnsInBase;

    public DayParkMap(MyTheChinczyk game){
        this.game = game;
    }
    GameTextures gameTextures;
    @Override
    public void show() {
        gameTextures = new GameTextures();
        rand = new Random();
        diceRoll = 6;

        yellowPawnsInBase=4;
        bluePawnsInBase = 4;
        greenPawnsInBase = 4;
        pinkPawnsInBase = 4;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        //Wyświetlenie głównego tła planszy
        drawBackGround();
        //Animacja rożka z lodem i huśtawki na planszy
        drawIceCreamAndSwingAnim();

        //Przykładowa obsługa zmiany ilości pionków w bazie
        if(Gdx.input.isKeyJustPressed(Input.Keys.MINUS) && yellowPawnsInBase>0){
            yellowPawnsInBase--;
            bluePawnsInBase--;
            greenPawnsInBase--;
            pinkPawnsInBase--;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) && yellowPawnsInBase<4){
            yellowPawnsInBase++;
            bluePawnsInBase++;
            greenPawnsInBase++;
            pinkPawnsInBase++;
        }
        drawBase();

        //Wyświetlenie liczby oczek
        drawDice();

        //Powrót do menu głównego
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            game.gameScreen = 3;
            game.setScreen(game.MenuLoadingScreen);
        }

        //Przykładowa obsługa animacji busa
        drawBusAnim();


        //Przykład poruszania się pionkiem
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            //Tutaj dodaje żeby wymusić wykonanie pierwszej klatki
            gameTextures.yellowPlayer1ElapsedTime += 3*Gdx.graphics.getDeltaTime();
            gameTextures.bluePlayer1ElapsedTime += 3*Gdx.graphics.getDeltaTime();
            gameTextures.greenPlayer1ElapsedTime += 3*Gdx.graphics.getDeltaTime();
            gameTextures.pinkPlayer1ElapsedTime += 3*Gdx.graphics.getDeltaTime();
        }
        if(gameTextures.yellowPlayer1Anim.getKeyFrameIndex(gameTextures.yellowPlayer1ElapsedTime)%10!=0){
            //To się wykonuje aż nie zrobi się 10 klatek, tyle trwa przesunięcie o jedno pole
            gameTextures.yellowPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.bluePlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.greenPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.pinkPlayer1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        //Niebieski
        if(gameTextures.bluePlayer1ElapsedTime < 8.35f){
            //Animacje w tym muszą mieć małą rozdzielczość więc podzieliłem ekran na dwa i
            //w zależności gdzie jest pionek, jego animacja musi być albo po prawo (ten if) albo po lewo (następny if)
            game.batch.draw(gameTextures.bluePlayer1Anim.getKeyFrame(gameTextures.bluePlayer1ElapsedTime, false), 840, 0, 1080, 1080);
        }
        else{
            game.batch.draw(gameTextures.bluePlayer1Anim.getKeyFrame(gameTextures.bluePlayer1ElapsedTime, false), 0, 0, 1080, 1080);
        }
        //Żółty
        if(gameTextures.yellowPlayer1ElapsedTime < 8.03f || gameTextures.yellowPlayer1ElapsedTime > 16.36f){
            game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(gameTextures.yellowPlayer1ElapsedTime, false), 0, 0, 1080, 1080);
        }
        else{
            game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(gameTextures.yellowPlayer1ElapsedTime, false), 840, 0, 1080, 1080);
        }
        //Zielony
        if(gameTextures.greenPlayer1ElapsedTime < 5.68f || gameTextures.greenPlayer1ElapsedTime > 14.01f){
            game.batch.draw(gameTextures.greenPlayer1Anim.getKeyFrame(gameTextures.greenPlayer1ElapsedTime, false), 0, 0, 1080, 1080);
        }
        else{
            game.batch.draw(gameTextures.greenPlayer1Anim.getKeyFrame(gameTextures.greenPlayer1ElapsedTime, false), 840, 0, 1080, 1080);
        }
        //Różowy
        if(gameTextures.pinkPlayer1ElapsedTime < 6.68f || gameTextures.pinkPlayer1ElapsedTime > 14.69f){
            game.batch.draw(gameTextures.pinkPlayer1Anim.getKeyFrame(gameTextures.pinkPlayer1ElapsedTime, false), 840, 0, 1080, 1080);
        }
        else{
            game.batch.draw(gameTextures.pinkPlayer1Anim.getKeyFrame(gameTextures.pinkPlayer1ElapsedTime, false), 0, 0, 1080, 1080);
        }


        //Przykładowa obsługa animacji karty
        drawCardAnim("Hello World!\nSample text, sample tex");

        //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
        game.batch.draw(gameTextures.dayParkTopground, 0, 0, 1920, 1080);

        //Obsługa animacji tabliczek oznaczających nową turę
        changeWhichPlayersTurn();

        //Wyświetlanie tabliczek w rogu z kolorem aktualnego gracza
        drawWhichPlayersTurnUI();

        //Przykładowa obsługa kostki
        drawDiceAnim();

        game.batch.end();
    }

    public void drawBase(){
        if(yellowPawnsInBase == 3){
            game.batch.draw(gameTextures.yellowBase3, 810, 365, 300, 350);
        }
        else if(yellowPawnsInBase == 2){
            game.batch.draw(gameTextures.yellowBase2, 810, 365, 300, 350);
        }
        else if(yellowPawnsInBase == 1){
            game.batch.draw(gameTextures.yellowBase1, 810, 365, 300, 350);
        }
        else if(yellowPawnsInBase != 4){
            game.batch.draw(gameTextures.yellowBase0, 810, 365, 300, 350);
        }

        if(greenPawnsInBase == 3){
            game.batch.draw(gameTextures.greenBase3, 810, 365, 300, 350);
        }
        else if(greenPawnsInBase == 2){
            game.batch.draw(gameTextures.greenBase2, 810, 365, 300, 350);
        }
        else if(greenPawnsInBase == 1){
            game.batch.draw(gameTextures.greenBase1, 810, 365, 300, 350);
        }
        else if(greenPawnsInBase != 4){
            game.batch.draw(gameTextures.greenBase0, 810, 365, 300, 350);
        }

        if(bluePawnsInBase == 3){
            game.batch.draw(gameTextures.blueBase3, 810, 365, 300, 350);
        }
        else if(bluePawnsInBase == 2){
            game.batch.draw(gameTextures.blueBase2, 810, 365, 300, 350);
        }
        else if(bluePawnsInBase == 1){
            game.batch.draw(gameTextures.blueBase1, 810, 365, 300, 350);
        }
        else if(bluePawnsInBase != 4){
            game.batch.draw(gameTextures.blueBase0, 810, 365, 300, 350);
        }

        if(pinkPawnsInBase == 3){
            game.batch.draw(gameTextures.pinkBase3, 810, 365, 300, 350);
        }
        else if(pinkPawnsInBase == 2){
            game.batch.draw(gameTextures.pinkBase2, 810, 365, 300, 350);
        }
        else if(pinkPawnsInBase == 1){
            game.batch.draw(gameTextures.pinkBase1, 810, 365, 300, 350);
        }
        else if(pinkPawnsInBase != 4){
            game.batch.draw(gameTextures.pinkBase0, 810, 365, 300, 350);
        }
    }

    public void drawCardAnim(String message){
        if(Gdx.input.isKeyJustPressed(Input.Keys.C) && !gameTextures.cardAnimStarted){
            //Wysunięcie karty
            gameTextures.cardAnimStarted = true;
        }
        else if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && gameTextures.cardAnim.isAnimationFinished(gameTextures.cardElapsedTime)){
            //Zamknięcie karty
            gameTextures.cardAnimStarted = false;
            gameTextures.cardElapsedTime = 0;
        }
        if(gameTextures.cardAnimStarted) {
            gameTextures.cardElapsedTime += Gdx.graphics.getDeltaTime();
            game.batch.draw(gameTextures.cardAnim.getKeyFrame(gameTextures.cardElapsedTime, false), 304, 71, 1270, 938);
            if(gameTextures.cardElapsedTime>1.5f){
                //Pojawienie się tekstu w momencie gdy karta się obraca
                gameTextures.font.draw(game.batch, message, 750, 600);
            }
        }
    }

    public void drawWhichPlayersTurnUI(){
        if(gameTextures.turnSignWhichPlayer == 1){
            game.batch.draw(gameTextures.turnSignYellowBackground, 1190, 980, 300, 100);
        }
        else if(gameTextures.turnSignWhichPlayer == 2){
            game.batch.draw(gameTextures.turnSignGreenBackground, 1190, 980, 300, 100);
        }
        else if(gameTextures.turnSignWhichPlayer == 3){
            game.batch.draw(gameTextures.turnSignBlueBackground, 1190, 980, 300, 100);
        }
        else{
            game.batch.draw(gameTextures.turnSignPinkBackground, 1190, 980, 300, 100);
        }
    }

    public void drawWhichPlayersTurnAnim(){
        if((turnSignKeyFrame > 0 && turnSignKeyFrame <40) || (turnSignKeyFrame > 40 && turnSignKeyFrame <73) || (turnSignKeyFrame > 73 && turnSignKeyFrame <106) || (turnSignKeyFrame > 106 && turnSignKeyFrame <139)){
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if(turnSignKeyFrame != 40 && turnSignKeyFrame != 73 && turnSignKeyFrame != 106 && turnSignKeyFrame != 139){
            game.batch.draw(gameTextures.turnSignAnim.getKeyFrame(gameTextures.turnSignElapsedTime, false), 753, 469, 420, 140);
        }
    }

    public void changeWhichPlayersTurn(){
        turnSignKeyFrame = gameTextures.turnSignAnim.getKeyFrameIndex(gameTextures.turnSignElapsedTime);
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            if(turnSignKeyFrame == 40 || turnSignKeyFrame == 73 || turnSignKeyFrame == 106){
                gameTextures.turnSignWhichPlayer++;
            }
            else if(turnSignKeyFrame == 139){
                gameTextures.turnSignWhichPlayer = 1;
                gameTextures.turnSignElapsedTime = 0;
            }
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        }
        drawWhichPlayersTurnAnim();
    }

    public void drawBackGround(){
        game.batch.draw(gameTextures.dayParkBackground, 0, 0, 1920, 1080);
    }
    public void drawIceCreamAndSwingAnim(){
        gameTextures.loopElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(gameTextures.iceCreamAnim.getKeyFrame(gameTextures.loopElapsedTime, true),
                0, 888, 179, 192);
        game.batch.draw(gameTextures.swingAnim.getKeyFrame(gameTextures.loopElapsedTime, true),
                1009, 137, 199, 95);
    }
    public void drawBusAnim(){
        //WYBÓR KOLORU PIONKA WSIADAJĄCEGO DO AUTOBUSU
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && gameTextures.BusAnimStarted == 0){
            //Żółty autobus
            gameTextures.BusAnimStarted = 1;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && gameTextures.BusAnimStarted == 0){
            //Zielony autobus
            gameTextures.BusAnimStarted = 2;
            gameTextures.BusElapsedTime = 0.5805f;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && gameTextures.BusAnimStarted == 0){
            //Niebieski autobus
            gameTextures.BusAnimStarted = 3;
            gameTextures.BusElapsedTime = 1.1444f;
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && gameTextures.BusAnimStarted == 0){
            //Różowy autobus
            gameTextures.BusAnimStarted = 4;
            gameTextures.BusElapsedTime = 1.7144f;
        }
        else if(gameTextures.BusAnim.isAnimationFinished(gameTextures.BusElapsedTime) && gameTextures.BusAnimStarted != 0){
            //Stan spoczynku animacji
            gameTextures.BusAnimStarted = 0;
            gameTextures.BusElapsedTime = 0;
        }

        //DZIELENIE ANIMACJI ZE WZGLĘDU NA KOLORY
        if(gameTextures.BusAnimStarted==1){
            //Żółty autobus
            if(gameTextures.BusElapsedTime > 0.54 && gameTextures.BusElapsedTime < 0.6){
                //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
                gameTextures.BusElapsedTime = 2.3f;
            }
            else if(gameTextures.BusElapsedTime > 6.51f){
                //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
                gameTextures.BusElapsedTime = 100f;
            }
        }
        else if(gameTextures.BusAnimStarted==2){
            //Zielony autobus
            if(gameTextures.BusElapsedTime > 1.11 && gameTextures.BusElapsedTime < 1.15){
                //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
                gameTextures.BusElapsedTime = 2.3f;
            }
            else if(gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f){
                //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
                gameTextures.BusElapsedTime = 6.5477f;
            }
            else if(gameTextures.BusElapsedTime > 8.92f){
                //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
                gameTextures.BusElapsedTime = 100f;
            }
        }
        else if(gameTextures.BusAnimStarted==3){
            //Niebieski autobus
            if(gameTextures.BusElapsedTime > 1.68 && gameTextures.BusElapsedTime < 1.7){
                //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
                gameTextures.BusElapsedTime = 2.3f;
            }
            else if(gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f){
                //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
                gameTextures.BusElapsedTime = 8.9455f;
            }
            else if(gameTextures.BusElapsedTime > 11.30f){
                //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
                gameTextures.BusElapsedTime = 100f;
            }
        }
        else if(gameTextures.BusAnimStarted==4){
            //Różowy autobus
            if(gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f){
                //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
                gameTextures.BusElapsedTime = 11.3432f;
            }
        }

        //Wyświetlenie animacji autobusu
        if(gameTextures.BusAnimStarted != 0) {
            gameTextures.BusElapsedTime += Gdx.graphics.getDeltaTime();
        }
        game.batch.draw(gameTextures.BusAnim.getKeyFrame(gameTextures.BusElapsedTime, false), 1526, 0, 394, 1080);
    }
    public void drawDiceAnim(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.D) && !gameTextures.diceAnimStarted){
            gameTextures.diceAnimStarted = true;
        }
        if(gameTextures.diceAnimStarted){
            if(!gameTextures.diceAnim.isAnimationFinished(gameTextures.diceElapsedTime)){
                gameTextures.diceElapsedTime += Gdx.graphics.getDeltaTime();
                game.batch.draw(gameTextures.diceAnim.getKeyFrame(gameTextures.diceElapsedTime, false), 300, 0, 1000, 850);
            }
            else{
                gameTextures.diceAnimStarted = false;
                gameTextures.diceElapsedTime = 0;
            }
        }
        if(gameTextures.diceAnim.getKeyFrameIndex(gameTextures.diceElapsedTime) == 40){
            diceRoll = rand.nextInt(6) + 1;
        }
    }
    public void drawDice(){
        if(diceRoll==5){
            game.batch.draw(gameTextures.dice5, 925, 505, 70, 70);
        }
        else if(diceRoll==4){
            game.batch.draw(gameTextures.dice4, 925, 505, 70, 70);
        }
        else if(diceRoll==3){
            game.batch.draw(gameTextures.dice3, 925, 505, 70, 70);
        }
        else if(diceRoll==2){
            game.batch.draw(gameTextures.dice2, 925, 505, 70, 70);
        }
        else if(diceRoll==1){
            game.batch.draw(gameTextures.dice1, 925, 505, 70, 70);
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
        gameTextures.iceCreamAtlas.dispose();
        gameTextures.swingAtlas.dispose();
        gameTextures.cardAtlas.dispose();
        gameTextures.BusAtlas.dispose();
        gameTextures.yellowPlayer1Atlas.dispose();
        gameTextures.pinkPlayer1Atlas.dispose();
        gameTextures.bluePlayer1Atlas.dispose();
        gameTextures.greenPlayer1Atlas.dispose();
        gameTextures.diceAtlas.dispose();
        gameTextures.font.dispose();
        gameTextures.turnSignAtlas.dispose();

        gameTextures.turnSignBlueBackground.dispose();
        gameTextures.turnSignPinkBackground.dispose();
        gameTextures.turnSignGreenBackground.dispose();
        gameTextures.turnSignYellowBackground.dispose();
        gameTextures.dice5.dispose();
        gameTextures.dice4.dispose();
        gameTextures.dice3.dispose();
        gameTextures.dice2.dispose();
        gameTextures.dice1.dispose();

        gameTextures.yellowBase3.dispose();
        gameTextures.yellowBase2.dispose();
        gameTextures.yellowBase1.dispose();
        gameTextures.yellowBase0.dispose();
        gameTextures.greenBase3.dispose();
        gameTextures.greenBase2.dispose();
        gameTextures.greenBase1.dispose();
        gameTextures.greenBase0.dispose();
        gameTextures.blueBase3.dispose();
        gameTextures.blueBase2.dispose();
        gameTextures.blueBase1.dispose();
        gameTextures.blueBase0.dispose();
        gameTextures.pinkBase3.dispose();
        gameTextures.pinkBase2.dispose();
        gameTextures.pinkBase1.dispose();
        gameTextures.pinkBase0.dispose();

        gameTextures.dayParkBackground.dispose();
        gameTextures.dayParkTopground.dispose();
    }
}
class GameTextures{
    BitmapFont font;
    public Texture dayParkBackground;
    public Texture dayParkTopground;


    public TextureAtlas yellowPlayer1Atlas;
    public Animation<TextureRegion> yellowPlayer1Anim;
    public float yellowPlayer1ElapsedTime;

    public TextureAtlas bluePlayer1Atlas;
    public Animation<TextureRegion> bluePlayer1Anim;
    public float bluePlayer1ElapsedTime;

    public TextureAtlas greenPlayer1Atlas;
    public Animation<TextureRegion> greenPlayer1Anim;
    public float greenPlayer1ElapsedTime;

    public TextureAtlas pinkPlayer1Atlas;
    public Animation<TextureRegion> pinkPlayer1Anim;
    public float pinkPlayer1ElapsedTime;

    public Texture yellowBase3;
    public Texture yellowBase2;
    public Texture yellowBase1;
    public Texture yellowBase0;
    public Texture greenBase3;
    public Texture greenBase2;
    public Texture greenBase1;
    public Texture greenBase0;
    public Texture blueBase3;
    public Texture blueBase2;
    public Texture blueBase1;
    public Texture blueBase0;
    public Texture pinkBase3;
    public Texture pinkBase2;
    public Texture pinkBase1;
    public Texture pinkBase0;


    public TextureAtlas iceCreamAtlas;
    public Animation<TextureRegion> iceCreamAnim;
    public float loopElapsedTime;
    public TextureAtlas swingAtlas;
    public Animation<TextureRegion> swingAnim;

    public TextureAtlas cardAtlas;
    public Animation<TextureRegion> cardAnim;
    public float cardElapsedTime;
    public boolean cardAnimStarted;

    public TextureAtlas BusAtlas;
    public Animation<TextureRegion> BusAnim;
    public float BusElapsedTime;
    public int BusAnimStarted;

    public TextureAtlas diceAtlas;
    public Animation<TextureRegion> diceAnim;
    public float diceElapsedTime;
    public boolean diceAnimStarted;
    public Texture dice5;
    public Texture dice4;
    public Texture dice3;
    public Texture dice2;
    public Texture dice1;

    public TextureAtlas turnSignAtlas;
    public Animation<TextureRegion> turnSignAnim;
    public float turnSignElapsedTime;
    public short turnSignWhichPlayer;
    public Texture turnSignYellowBackground;
    public Texture turnSignGreenBackground;
    public Texture turnSignBlueBackground;
    public Texture turnSignPinkBackground;

    GameTextures(){
        dayParkBackground = new Texture("Map1/TC_Map1_Main.png");
        dayParkTopground = new Texture("Map1/TC_Map1_TopLayer.png");


        yellowPlayer1Atlas = new TextureAtlas("Map1/YellowPlayerAnimSheet/YellowPlayerAnimSheet.atlas");
        yellowPlayer1Anim = new Animation<TextureRegion>(1f/30f, yellowPlayer1Atlas.getRegions());
        yellowPlayer1ElapsedTime = 0f;

        greenPlayer1Atlas = new TextureAtlas("Map1/GreenPlayerAnimSheet/GreenPlayerAnimSheet.atlas");
        greenPlayer1Anim = new Animation<TextureRegion>(1f/30f, greenPlayer1Atlas.getRegions());
        greenPlayer1ElapsedTime = 0f;

        bluePlayer1Atlas = new TextureAtlas("Map1/BluePlayerAnimSheet/BluePlayerAnimSheet.atlas");
        bluePlayer1Anim = new Animation<TextureRegion>(1f/30f, bluePlayer1Atlas.getRegions());
        bluePlayer1ElapsedTime = 0f;

        pinkPlayer1Atlas = new TextureAtlas("Map1/PinkPlayerAnimSheet/PinkPlayerAnimSheet.atlas");
        pinkPlayer1Anim = new Animation<TextureRegion>(1f/30f, pinkPlayer1Atlas.getRegions());
        pinkPlayer1ElapsedTime = 0f;


        yellowBase3= new Texture("Map1/Base/Base_Yellow3.png");
        yellowBase2= new Texture("Map1/Base/Base_Yellow2.png");
        yellowBase1= new Texture("Map1/Base/Base_Yellow1.png");
        yellowBase0= new Texture("Map1/Base/Base_Yellow0.png");
        greenBase3= new Texture("Map1/Base/Base_Green3.png");
        greenBase2= new Texture("Map1/Base/Base_Green2.png");
        greenBase1= new Texture("Map1/Base/Base_Green1.png");
        greenBase0= new Texture("Map1/Base/Base_Green0.png");
        blueBase3= new Texture("Map1/Base/Base_Blue3.png");
        blueBase2= new Texture("Map1/Base/Base_Blue2.png");
        blueBase1= new Texture("Map1/Base/Base_Blue1.png");
        blueBase0= new Texture("Map1/Base/Base_Blue0.png");
        pinkBase3= new Texture("Map1/Base/Base_Pink3.png");
        pinkBase2= new Texture("Map1/Base/Base_Pink2.png");
        pinkBase1= new Texture("Map1/Base/Base_Pink1.png");
        pinkBase0= new Texture("Map1/Base/Base_Pink0.png");


        iceCreamAtlas = new TextureAtlas("Map1/IceCreamAnimationSheet/myIceCreamAnimationSheet.atlas");
        iceCreamAnim = new Animation<TextureRegion>(1f/30f, iceCreamAtlas.getRegions());
        loopElapsedTime = 0f;
        swingAtlas = new TextureAtlas("Map1/SwingAnimSheet/SwingAnimSheet.atlas");
        swingAnim = new Animation<TextureRegion>(1f/30f, swingAtlas.getRegions());

        cardAtlas = new TextureAtlas("Map1/CardAnimSheet/CardAnimSheet.atlas");
        cardAnim = new Animation<TextureRegion>(1f/30f, cardAtlas.getRegions());
        cardElapsedTime = 0f;
        cardAnimStarted = false;

        BusAtlas = new TextureAtlas("Map1/BusAnimSheet/BusAnimSheet.atlas");
        BusAnim = new Animation<TextureRegion>(1f/30f, BusAtlas.getRegions());
        BusElapsedTime = 0f;
        BusAnimStarted = 0;

        diceAtlas = new TextureAtlas("Map1/DiceAnimSheet/DiceAnimSheet.atlas");
        diceAnim = new Animation<TextureRegion>(1f/30f, diceAtlas.getRegions());
        diceElapsedTime = 0f;
        diceAnimStarted = false;
        dice5 = new Texture("Map1/DiceSides/DiceSides5.png");
        dice4 = new Texture("Map1/DiceSides/DiceSides4.png");
        dice3 = new Texture("Map1/DiceSides/DiceSides3.png");
        dice2 = new Texture("Map1/DiceSides/DiceSides2.png");
        dice1 = new Texture("Map1/DiceSides/DiceSides1.png");

        turnSignAtlas = new TextureAtlas("Map1/TurnSignAnimSheet/TurnSignAnimSheet.atlas");
        turnSignAnim = new Animation<TextureRegion>(1f/30f, turnSignAtlas.getRegions());
        turnSignElapsedTime = 0;
        turnSignWhichPlayer = 1;
        turnSignYellowBackground = new Texture("Map1/TurnSign/yellow.png");
        turnSignGreenBackground = new Texture("Map1/TurnSign/green.png");
        turnSignBlueBackground = new Texture("Map1/TurnSign/blue.png");
        turnSignPinkBackground = new Texture("Map1/TurnSign/pink.png");

        font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
        font.getData().setScale(.3f,.3f);
    }

}
