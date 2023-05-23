package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;

import java.util.ArrayList;
import java.util.Random;

public class DayParkMap implements Screen {

    MyTheChinczyk game;
    int randNumber = 0;
    int turnSignKeyFrame;
    boolean throwDice = false;
    int playerNumberTurn;
    int jumpAnimation = 0;
    public ArrayList<Player> Players = new ArrayList<>();
    /*public Player Player0 = new Player(0);
    public Player Player1 = new Player(0);
    public Player Player2 = new Player(0);
    public Player Player3 = new Player(0);*/

    public DayParkMap(MyTheChinczyk game){
        this.game = game;
    }
    GameTextures gameTextures;
    @Override
    public void show() {
        for (int i = 0; i < game.playerCount; i++){
            Player player = new Player(i);
            Players.add(player);
        }
        //System.out.println(game.playerCount);
        gameTextures = new GameTextures();
        playerNumberTurn = 0;
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

        managePlayer(playerNumberTurn);
        //Przykładowa obsługa animacji busa z zółtym pionkiem

        //Przykład poruszania się pionkiem


        //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
        game.batch.draw(gameTextures.dayParkTopground, 0, 0, 1920, 1080);

        //Obsługa animacji tabliczek oznaczających nową turę
        //changeWhichPlayersTurn();

        //Wyświetlanie tabliczek w rogu z kolorem aktualnego gracza
        //drawWhichPlayersTurnUI();

        game.batch.end();
    }

    public boolean flag = true;
    public void managePlayer(int playerNumberTurn){
        Player player = Players.get(playerNumberTurn);
        if(player.activePawn == 1 && !throwDice){
            drawStaticPawn(player);
        }
        if(!throwDice){
            drawDiceAnim();
        }else if(player.activePawn == 0 && randNumber == 6){
            drawStaticPawn(player);
            player.activePawn = 1;
            throwDice = false;
        } else if (player.activePawn == 1) {
            changeAnimationPawn(player);
            drawStaticPawn(player);
        }else {
            throwDice = false;
        }
    }

    private void changeAnimationPawn(Player player) {
        if(randNumber >= 1) {
            if (flag) {
                //Tutaj dodaje żeby wymusić wykonanie pierwszej klatki
                player.playerElapsedTime += 3 * Gdx.graphics.getDeltaTime();
                flag = false;
            }
            if (gameTextures.yellowPlayer1Anim.getKeyFrameIndex(player.playerElapsedTime) % 10 != 0) {
                //To się wykonuje aż nie zrobi się 10 klatek, tyle trwa przesunięcie o jedno pole
                player.playerElapsedTime += Gdx.graphics.getDeltaTime();
            } else {
                randNumber--;
                flag = true;
            }
        }else {
            //setPlayerNumberTurn();
            throwDice = false;
            randNumber = 0;
        }
    }

    private void drawStaticPawn(Player player) {
        if (player.playerElapsedTime < 8.03f || player.playerElapsedTime > 16.35f) {
            game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(player.playerElapsedTime, false),
                    0, 0, 1080, 1080);
        } else {
            game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(player.playerElapsedTime, false),
                    840, 0, 1080, 1080);
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
    void setPlayerNumberTurn(){
        if(playerNumberTurn < game.playerCount - 1){
            playerNumberTurn ++;
        } else {
            playerNumberTurn = 0;
        }
        //System.out.println(playerNumberTurn);
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
            gameTextures.turnSignElapsedTime += 3*Gdx.graphics.getDeltaTime();
        }
        drawWhichPlayersTurnAnim();
    }

    public void drawWhichPlayersTurnAnim(){
        if((turnSignKeyFrame > 0 && turnSignKeyFrame <40) || (turnSignKeyFrame > 40 && turnSignKeyFrame <73) || (turnSignKeyFrame > 73 && turnSignKeyFrame <106) || (turnSignKeyFrame > 106 && turnSignKeyFrame <139)){
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if(turnSignKeyFrame != 40 && turnSignKeyFrame != 73 && turnSignKeyFrame != 106 && turnSignKeyFrame != 139){
            game.batch.draw(gameTextures.turnSignAnim.getKeyFrame(gameTextures.turnSignElapsedTime, false), 753, 469, 420, 140);
        }
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
    public void drawYellowBusAnim(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.B) && !gameTextures.yellowBusAnimStarted){
            gameTextures.yellowBusAnimStarted = true;
        }
        else if(gameTextures.yellowBusAnim.isAnimationFinished(gameTextures.yellowBusElapsedTime) && gameTextures.yellowBusAnimStarted){
            gameTextures.yellowBusAnimStarted = false;
            gameTextures.yellowBusElapsedTime = 0;
        }
        if(gameTextures.yellowBusAnimStarted) {
            gameTextures.yellowBusElapsedTime += Gdx.graphics.getDeltaTime();
        }
        game.batch.draw(gameTextures.yellowBusAnim.getKeyFrame(gameTextures.yellowBusElapsedTime, false), 1015, 0, 905, 1080);
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
                Random rand = new Random();
                randNumber = rand.nextInt(6) + 1;
                System.out.println(randNumber);
                gameTextures.diceAnimStarted = false;
                gameTextures.diceElapsedTime = 0;
                throwDice = true;
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
        gameTextures.yellowBusAtlas.dispose();
        gameTextures.yellowPlayer1Atlas.dispose();
        gameTextures.diceAtlas.dispose();
        gameTextures.font.dispose();
        gameTextures.turnSignAtlas.dispose();

        gameTextures.turnSignBlueBackground.dispose();
        gameTextures.turnSignPinkBackground.dispose();
        gameTextures.turnSignGreenBackground.dispose();
        gameTextures.turnSignYellowBackground.dispose();

        gameTextures.dayParkBackground.dispose();
        gameTextures.dayParkTopground.dispose();
    }
}
class Player{

    float playerElapsedTime = 0f;
    int playerNumber;
    int activePawn;

    int numbersOfWinPawns;
    public Player(int playerNumber){
        this.playerNumber = playerNumber;
        activePawn = 0;
        numbersOfWinPawns = 0;
    }
}

