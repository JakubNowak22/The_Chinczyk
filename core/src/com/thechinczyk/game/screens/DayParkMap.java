package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DayParkMap implements Screen {

    MyTheChinczyk game;
    int turnSignKeyFrame;
    Random rand;
    double diceRoll;

    boolean[] miniGamePlaying;
    boolean[] miniGameOutput;
    MiniGamesTypes miniGameType;

    int yellowPawnsInBase;
    int bluePawnsInBase;
    int greenPawnsInBase;
    int pinkPawnsInBase;

    enum Players {None,Yellow, Green, Blue, Pink}
    MiniGame miniGame;

    public DayParkMap(MyTheChinczyk game){
        this.miniGamePlaying = new boolean[3];
        this.miniGameOutput = new boolean[3];
        this.miniGameType = MiniGamesTypes.NONE;
        this.game = game;
        this.miniGame = new MiniGame(game.batch, this);
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

        //Powrót do menu głównego
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.gameScreen = 3;
            game.setScreen(game.MenuLoadingScreen);
        }

        //Przykładowa obsługa animacji busa
        drawBusAnim();

        if (!this.miniGamePlaying[0] && !this.miniGamePlaying[1] && !this.miniGamePlaying[2]) {
            //Przykładowa obsługa zmiany ilości pionków w bazie
            if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS) && yellowPawnsInBase > 0) {
                yellowPawnsInBase--;
                bluePawnsInBase--;
                greenPawnsInBase--;
                pinkPawnsInBase--;
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) && yellowPawnsInBase < 4) {
                yellowPawnsInBase++;
                bluePawnsInBase++;
                greenPawnsInBase++;
                pinkPawnsInBase++;
            }
            drawBase();

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
            //Wyświetlenie liczby oczek
            drawDice();

            //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
            game.batch.draw(gameTextures.dayParkTopground, 0, 0, 1920, 1080);

            //Przykładowa obsługa animacji karty
            if (!this.miniGameOutput[0] && !this.miniGameOutput[1] && !this.miniGameOutput[2])
                drawCardAnim("Hello World!\nSample text, sample tex");

            //Obsługa animacji tabliczek oznaczających nową turę
            changeWhichPlayersTurn();

            //Wyświetlanie tabliczek w rogu z kolorem aktualnego gracza
            drawWhichPlayersTurnUI();

            //Przykładowa obsługa kostki
            drawDiceAnim();
        }
        getInputForMiniGame();

        //Wylosowanie mini-gry Math
        drawMathMiniGameMenu("Mini-Game!\nYour game will be...\nMATH MINI GAME");

        //Wylosowanie mini-gry Space Invaders
        drawSpaceInvadersMiniGameMenu("Mini-Game!\nYour game will be...\nSPACE INVADERS");

        //Wylosowanie mini-gry Memory
        drawMemoryMiniGameMenu("Mini-Game!\nYour game will be...\nMEMORY");

        //Obsługa animacji zbijania pionków
        drawPawnCaptureAnim(Players.Pink,Players.Green);

        //Obsługa tablicy z wynikami
        scoreBoard(Players.Pink,Players.Green,Players.Yellow,Players.Blue);

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
        if((Gdx.input.isKeyJustPressed(Input.Keys.C) && !gameTextures.cardAnimStarted) || (Gdx.input.isKeyJustPressed(Input.Keys.Z) && !gameTextures.cardAnimStarted && !this.miniGameOutput[0]) || this.miniGameOutput[0] || this.miniGameOutput[1] || this.miniGameOutput[2] || (Gdx.input.isKeyJustPressed(Input.Keys.M) && !gameTextures.cardAnimStarted && !this.miniGameOutput[1]) || (Gdx.input.isKeyJustPressed(Input.Keys.N) && !gameTextures.cardAnimStarted && !this.miniGameOutput[2])){
            //Wysunięcie karty
           // System.out.println("test2");
            gameTextures.cardAnimStarted = true;
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && gameTextures.cardAnim.isAnimationFinished(gameTextures.cardElapsedTime)){
            //Zamknięcie karty
            //System.out.println("test");
            this.miniGameOutput[0] = false;
            this.miniGameOutput[1] = false;
            this.miniGameOutput[2] = false;
            gameTextures.cardAnimStarted = false;
            gameTextures.cardElapsedTime = 0;
        }
        if(gameTextures.cardAnimStarted) {
            gameTextures.cardElapsedTime += Gdx.graphics.getDeltaTime();
            game.batch.draw(gameTextures.cardAnim.getKeyFrame(gameTextures.cardElapsedTime, false), 304, 71, 1270, 938);
            if(gameTextures.cardElapsedTime>1.5f){
                //Pojawienie się tekstu w momencie gdy karta się obraca
                gameTextures.font.draw(game.batch, message, 660, 740, 600, Align.center, true);
                }
            }
    }

    public void drawSpaceInvadersMiniGameMenu(String message) {
        if((this.miniGameType == MiniGamesTypes.SPACE_INVADERS || gameTextures.cardAnimStarted) && !this.miniGameOutput[0] && !this.miniGameOutput[1] && !this.miniGameOutput[2] && !this.miniGamePlaying[1] && !this.miniGamePlaying[2]) {
            drawCardAnim(message);
           // this.miniGamePlaying[0] = true;
        }
        if (this.miniGamePlaying[0] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[0]) {
                this.miniGame.loadTextures(MiniGamesTypes.SPACE_INVADERS);
                this.miniGame.isLoaded[0] = true;
            }
          //  this.miniGamePlaying = true;
            this.miniGame.menuSpaceInvaders.Draw();
        }

        if (this.miniGameOutput[0] && !this.miniGamePlaying[0]) {
            drawCardAnim("Text with reward/punishment\nthat player will receive1");
        }

        /*if (this.miniGameOutput && !this.gameTextures.cardAnimStarted)
            this.miniGameOutput = false; */
    }

    public void drawMathMiniGameMenu(String message) {
        if((this.miniGameType == MiniGamesTypes.MATH || gameTextures.cardAnimStarted) && !this.miniGameOutput[0] && !this.miniGameOutput[1] && !this.miniGameOutput[2] && !this.miniGamePlaying[0] && !this.miniGamePlaying[2]) {
            drawCardAnim(message);
           // System.out.println("yes");
           // this.miniGamePlaying[1] = true;
        }
        if (this.miniGamePlaying[1] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[1]) {
                this.miniGame.loadTextures(MiniGamesTypes.MATH);
                this.miniGame.isLoaded[1] = true;
            }
            //  this.miniGamePlaying = true;
            this.miniGame.menuMath.Draw();
        }

        if (this.miniGameOutput[1] && !this.miniGamePlaying[1]) {
            drawCardAnim("Text with reward/punishment\nthat player will receive2");
        }

        /*if (this.miniGameOutput && !this.gameTextures.cardAnimStarted)
            this.miniGameOutput = false; */
    }

    public void drawMemoryMiniGameMenu(String message) {
        if((this.miniGameType == MiniGamesTypes.MEMORY || gameTextures.cardAnimStarted) && !this.miniGameOutput[0] && !this.miniGameOutput[1] && !this.miniGameOutput[2] && !this.miniGamePlaying[0] && !this.miniGamePlaying[1]) {
            drawCardAnim(message);
        }
        if (this.miniGamePlaying[2] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[2]) {
                this.miniGame.loadTextures(MiniGamesTypes.MEMORY);
                this.miniGame.isLoaded[2] = true;
            }
            //  this.miniGamePlaying = true;
            this.miniGame.menuMemory.Draw();
        }

        if (this.miniGameOutput[2] && !this.miniGamePlaying[2]) {
            drawCardAnim("Text with reward/punishment\nthat player will receive3");
        }

        /*if (this.miniGameOutput && !this.gameTextures.cardAnimStarted)
            this.miniGameOutput = false; */
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

    public void drawMiniGameTimer(int x, int y) {
        gameTextures.timerElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(gameTextures.timerAnim.getKeyFrame(gameTextures.timerElapsedTime, true),
                x, y, 100, 100);
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

    public void drawPawnCaptureAnim(Players playerCaptured, Players playerCapturing){
        if(gameTextures.pawnCapture1ElapsedTime!=0){
            //Odtwarzanie animacji zbijającego aż się nie skończy dla danego koloru
            if(playerCapturing == Players.Yellow && gameTextures.pawnCapture1ElapsedTime < 0.65f){
                gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
            }
            else if(playerCapturing == Players.Green && gameTextures.pawnCapture1ElapsedTime < 1.3241f){
                gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
            }
            else if(playerCapturing == Players.Blue && gameTextures.pawnCapture1ElapsedTime < 1.98f){
                gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
            }
            else if(playerCapturing == Players.Pink && gameTextures.pawnCapture1ElapsedTime < 2.64f){
                gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
            }

            //Sprawdzanie czy już czas na odtawarzanie animacji zbijanego pionka
            if(((playerCapturing == Players.Yellow && gameTextures.pawnCapture1ElapsedTime > 0.34f) ||
                    (playerCapturing == Players.Green && gameTextures.pawnCapture1ElapsedTime > 1.008f) ||
                    (playerCapturing == Players.Blue && gameTextures.pawnCapture1ElapsedTime > 1.6754f) ||
                    (playerCapturing == Players.Pink && gameTextures.pawnCapture1ElapsedTime > 2.34f))
            ){
                gameTextures.pawnCapture2ElapsedTime += Gdx.graphics.getDeltaTime();
            }

            //Liczy czas ogólny dla całej animacji
            gameTextures.pawnCaptureMainElapsedTime += Gdx.graphics.getDeltaTime();

            //Wyświetlanie obydwu animacji i tła
            game.batch.draw(gameTextures.pawnCaptureBackground, 0, 0, 1920, 1080);
            if(!((playerCaptured == Players.Yellow && gameTextures.pawnCapture2ElapsedTime > 0.48f) ||
                    (playerCaptured == Players.Green && gameTextures.pawnCapture2ElapsedTime > 1.04f) ||
                    (playerCaptured == Players.Blue && gameTextures.pawnCapture2ElapsedTime > 1.47f) ||
                    (playerCaptured == Players.Pink && gameTextures.pawnCapture2ElapsedTime > 1.92f))){
                //Tutaj sprawdzam jeszcze czy animacja zbijanego się nie skończyła i jeśli tak to go nie wyświetlam już
                game.batch.draw(gameTextures.pawnCapture2Anim.getKeyFrame(gameTextures.pawnCapture2ElapsedTime, false), 640, 415, 350, 250);
            }
            game.batch.draw(gameTextures.pawnCapture1Anim.getKeyFrame(gameTextures.pawnCapture1ElapsedTime, false), 850, 365, 400, 350);

            //Sprawdzam czy ogólny czas animacji dobiegł do końca.
            if(gameTextures.pawnCaptureMainElapsedTime > 1.2f){
                gameTextures.pawnCapture1ElapsedTime = 0;
                gameTextures.pawnCaptureMainElapsedTime = 0;
            }
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.P) && gameTextures.pawnCapture1ElapsedTime==0){
            //Ustawianie odpowiedniej klatki animacji w zależności od koloru zbijającego pionka
            if(playerCapturing == Players.Yellow){
                gameTextures.pawnCapture1ElapsedTime = 0.01f;
            }
            else if(playerCapturing == Players.Green){
                gameTextures.pawnCapture1ElapsedTime = 0.6767f;
            }
            else if(playerCapturing == Players.Blue){
                gameTextures.pawnCapture1ElapsedTime = 1.3408f;
            }
            else if(playerCapturing == Players.Pink){
                gameTextures.pawnCapture1ElapsedTime = 2.01f;
            }

            //Ustawianie odpowiedniej klatki animacji w zależności od koloru zbijanego pionka
            if(playerCaptured == Players.Yellow){
                gameTextures.pawnCapture2ElapsedTime = 0.01f;
            }
            else if(playerCaptured == Players.Green){
                gameTextures.pawnCapture2ElapsedTime = 0.6364f;
            }
            else if(playerCaptured == Players.Blue){
                gameTextures.pawnCapture2ElapsedTime = 1.07f;
            }
            else if(playerCaptured == Players.Pink){
                gameTextures.pawnCapture2ElapsedTime = 1.505f;
            }
        }
    }

    public void scoreBoard(Players first, Players second, Players third, Players fourth){
        if(!gameTextures.scoreBoardFlag && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            //Włączenie tablicy
            gameTextures.scoreBoardFlag = true;
        }
        else if(gameTextures.scoreBoardFlag && Gdx.input.isKeyJustPressed(Input.Keys.S)){
            //Wyłączenie tablicy. Tu mozna podmienić to żeby zamiast chowania tablicy był powrót do menu głównego
            gameTextures.scoreBoardFlag = false;
        }
        else if(gameTextures.scoreBoardFlag){
            //Rysowanie tła
            game.batch.draw(gameTextures.scoreBoardBackground, 0, 0, 1920, 1080);

            //Wyświetlanie odpowiednich graczy na poszczególnych miejscach
            if(first.ordinal() >0){
                game.batch.draw(gameTextures.scoreBoard.get(first.ordinal()-1), 900, 585, 450, 150);
            }
            if(second.ordinal()>0){
                game.batch.draw(gameTextures.scoreBoard.get(second.ordinal()-1), 689, 480, 300, 100);
            }
            if(third.ordinal()>0){
                game.batch.draw(gameTextures.scoreBoard.get(third.ordinal()-1), 1040, 383, 300, 100);
            }
            if(fourth.ordinal()>0){
                game.batch.draw(gameTextures.scoreBoard.get(fourth.ordinal()-1), 759, 283, 300, 100);
            }
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
        //gameTextures.timerAtlas.dispose();

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

        gameTextures.pawnCaptureBackground.dispose();
        gameTextures.pawnCapture1Atlas.dispose();
        gameTextures.pawnCapture2Atlas.dispose();

        gameTextures.scoreBoardBackground.dispose();
        gameTextures.scoreBoardYellow.dispose();
        gameTextures.scoreBoardBlue.dispose();
        gameTextures.scoreBoardGreen.dispose();
        gameTextures.scoreBoardPink.dispose();
    }

    public void unlockMap(MiniGamesTypes type) {
        if (type == MiniGamesTypes.SPACE_INVADERS) {
            this.miniGamePlaying[0] = false;
            this.miniGameOutput[0] = true;
        }
        else if (type == MiniGamesTypes.MATH){
            this.miniGamePlaying[1] = false;
            this.miniGameOutput[1] = true;
        }
        else if (type == MiniGamesTypes.MEMORY) {
            this.miniGamePlaying[2] = false;
            this.miniGameOutput[2] = true;
        }

    }

    public void getInputForMiniGame() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            this.miniGamePlaying[0] = true;
            this.miniGameType = MiniGamesTypes.SPACE_INVADERS;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            this.miniGamePlaying[1] = true;
            this.miniGameType = MiniGamesTypes.MATH;
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            this.miniGamePlaying[2] = true;
            this.miniGameType = MiniGamesTypes.MEMORY;
        }
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

    public TextureAtlas timerAtlas;
    public Animation<TextureRegion> timerAnim;
    public float timerElapsedTime;

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

    public Texture pawnCaptureBackground;
    public TextureAtlas pawnCapture1Atlas;
    public Animation<TextureRegion> pawnCapture1Anim;
    public float pawnCapture1ElapsedTime;
    public TextureAtlas pawnCapture2Atlas;
    public Animation<TextureRegion> pawnCapture2Anim;
    public float pawnCapture2ElapsedTime;
    public float pawnCaptureMainElapsedTime;

    public Texture scoreBoardBackground;
    public Texture scoreBoardYellow;
    public Texture scoreBoardGreen;
    public Texture scoreBoardBlue;
    public Texture scoreBoardPink;
    public boolean scoreBoardFlag;
    List<Texture> scoreBoard = new ArrayList<Texture>();

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

        timerAtlas = new TextureAtlas("TimerAnimSheet/TimeAtlas.atlas");
        timerAnim = new Animation<TextureRegion>(1f/26f, timerAtlas.getRegions());
        timerElapsedTime = 0f;

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

        pawnCaptureBackground = new Texture("Map1/TC_Map1_PawnCaptured_Background.png");
        pawnCapture1Atlas = new TextureAtlas("Map1/PawnCapturedAnim1Sheet/PawnCapturedAnim1Sheet.atlas");
        pawnCapture1Anim = new Animation<TextureRegion>(1f/30f, pawnCapture1Atlas.getRegions());
        pawnCapture1ElapsedTime = 0;
        pawnCapture2Atlas = new TextureAtlas("Map1/PawnCapturedAnim2Sheet/PawnCapturedAnim2Sheet.atlas");
        pawnCapture2Anim = new Animation<TextureRegion>(1f/30f, pawnCapture2Atlas.getRegions());
        pawnCapture2ElapsedTime = 0;
        pawnCaptureMainElapsedTime = 0;

        scoreBoardBackground = new Texture("Map1/ScoreBoard/TC_Map1_ScoreBoard.png");
        scoreBoardYellow = new Texture("Map1/ScoreBoard/ScoreBoard_Yellow.png");
        scoreBoardGreen = new Texture("Map1/ScoreBoard/ScoreBoard_Green.png");
        scoreBoardBlue = new Texture("Map1/ScoreBoard/ScoreBoard_Blue.png");
        scoreBoardPink = new Texture("Map1/ScoreBoard/ScoreBoard_Pink.png");
        scoreBoardFlag = false;
        scoreBoard.add(scoreBoardYellow);
        scoreBoard.add(scoreBoardGreen);
        scoreBoard.add(scoreBoardBlue);
        scoreBoard.add(scoreBoardPink);

        font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
        font.getData().setScale(.3f,.3f);
    }

}
