package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;

import java.util.ArrayList;
import java.util.Random;

public class DayParkMap implements Screen {

    MyTheChinczyk game;
    int randNumber = 0;

    //elapsedtime player1 yellow 0
    //elapsedtime player2 green 2.3362615
    //elapsedtime player3 blue 8.000907
    //elapsedtime player4 pink 9.67303
    float[] startPlayerElapsedTime = {0f, 2.3362615f, 8.000907f, 9.67303f};
    int turnSignKeyFrame;
    boolean throwDice = false;
    int playerNumberTurn;
    int jumpAnimation = 0;
    public ArrayList<Player> Players = new ArrayList<>();
    /*public Player Player0 = new Player(0);
    public Player Player1 = new Player(0);
    public Player Player2 = new Player(0);
    public Player Player3 = new Player(0);*/

    public DayParkMap(MyTheChinczyk game) {
        this.game = game;
    }

    GameTextures gameTextures;

    @Override
    public void show() {
        gameTextures = new GameTextures();
        for (int i = 0; i < game.playerCount; i++) {
            Player player = new Player(i, startPlayerElapsedTime[i]);
            Players.add(player);
            if(i==0){
                player.playerAnimation = gameTextures.yellowPlayerAnim;
            }else{
                player.playerAnimation = gameTextures.bluePlayerAnim;
            }
        }
        //System.out.println(game.playerCount);
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
        drawPlayers(playerNumberTurn);
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


    void drawPlayers(int playerNumberTurn) {
        for (int i = 0; i < game.playerCount; i++) {
            Player player = Players.get(i);
            if (player.activePawn >= 1) {
                drawPlayerPawns(player);
            }
        }
    }

    public boolean flag = true;
    public int pawToChange = -1;
    public boolean pawnChoose = false;

    public void managePlayer(int playerNumberTurn) {
        Player player = Players.get(playerNumberTurn);
        if (!throwDice) {
            drawDiceAnim();
        } else if (player.activePawn == 0 && randNumber == 6) {
            addPawn(player);
        }else if (player.activePawn >= 1 && player.activePawn <= 4) {
            if(player.activePawn != 1) {
                manageParticularPawn(player);
                if(pawnChoose){
                    changeAnimationPawn(player, pawToChange);
                    //drawPawn(player, pawToChange);
                }
                //drawPlayerPawns(player);
            }else if (randNumber == 6 && player.pawns[0].position != 0) {
                addPawn(player);
            }else {
                pawToChange = 0;
                changeAnimationPawn(player, pawToChange);
                //drawPawn(player, pawToChange);
            }
        } else if (player.activePawn == 0) {
            setPlayerNumberTurn();
            throwDice = false;
        }/*else if(){

        }*/
    }

    private void addPawn(Player player) {
        player.activePawn ++;
        player.pawns[player.activePawn - 1].active = true;
        setPlayerNumberTurn();
        throwDice = false;
    }

    private void manageParticularPawn(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && player.pawns[0].active) {
            pawToChange = 0;
            pawnChoose = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && player.pawns[1].active) {
            pawToChange = 1;
            pawnChoose = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && player.pawns[2].active) {
            pawnChoose = true;
            pawToChange = 2;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && player.pawns[3].active) {
            pawToChange = 3;
            pawnChoose = true;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) &&
                randNumber == 6 && player.activePawn <= 4) {
            addPawn(player);
        }
    }

    private void changeAnimationPawn(Player player, int pawNumber) {
        if (randNumber >= 1 && pawNumber != -1) {
            if (flag) {
                player.pawns[pawNumber].playerElapsedTime += 3 * Gdx.graphics.getDeltaTime();
                flag = false;
            }
            if (gameTextures.yellowPlayerAnim.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10 != 0) {
                player.pawns[pawNumber].playerElapsedTime += Gdx.graphics.getDeltaTime();
            } else {
                player.pawns[pawNumber].position ++;
                randNumber--;
                flag = true;
            }
        } else {
            setPlayerNumberTurn();
            pawToChange = -1;
            throwDice = false;
            pawnChoose = false;
            randNumber = 0;
        }
    }
    void drawPlayerPawns(Player player){
        for (int i = 0; i < player.activePawn ;i++){
            drawPawn(player, i);
        }
    }
    private void drawPawn(Player player, int pawnNumber) {
        if(pawnNumber != -1) {
            if (player.pawns[pawnNumber].playerElapsedTime < 8.03f || player.pawns[pawnNumber].playerElapsedTime > 16.35f) {
                game.batch.draw(gameTextures.yellowPlayerAnim.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                        0, 0, 1080, 1080);
            } else {
                game.batch.draw(gameTextures.yellowPlayerAnim.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                        840, 0, 1080, 1080);
            }
        }
    }

    public void drawCardAnim(String message) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.C) && !gameTextures.cardAnimStarted) {
            //Wysunięcie karty
            gameTextures.cardAnimStarted = true;
        } else if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && gameTextures.cardAnim.isAnimationFinished(gameTextures.cardElapsedTime)) {
            //Zamknięcie karty
            gameTextures.cardAnimStarted = false;
            gameTextures.cardElapsedTime = 0;
        }
        if (gameTextures.cardAnimStarted) {
            gameTextures.cardElapsedTime += Gdx.graphics.getDeltaTime();
            game.batch.draw(gameTextures.cardAnim.getKeyFrame(gameTextures.cardElapsedTime, false), 304, 71, 1270, 938);
            if (gameTextures.cardElapsedTime > 1.5f) {
                //Pojawienie się tekstu w momencie gdy karta się obraca
                gameTextures.font.draw(game.batch, message, 750, 600);
            }
        }
    }

    void setPlayerNumberTurn() {
        if (playerNumberTurn < game.playerCount - 1) {
            playerNumberTurn++;
        } else {
            playerNumberTurn = 0;
        }
        //System.out.println(playerNumberTurn);
    }

    public void drawWhichPlayersTurnUI() {
        if (gameTextures.turnSignWhichPlayer == 1) {
            game.batch.draw(gameTextures.turnSignYellowBackground, 1190, 980, 300, 100);
        } else if (gameTextures.turnSignWhichPlayer == 2) {
            game.batch.draw(gameTextures.turnSignGreenBackground, 1190, 980, 300, 100);
        } else if (gameTextures.turnSignWhichPlayer == 3) {
            game.batch.draw(gameTextures.turnSignBlueBackground, 1190, 980, 300, 100);
        } else {
            game.batch.draw(gameTextures.turnSignPinkBackground, 1190, 980, 300, 100);
        }
    }

    public void changeWhichPlayersTurn() {
        turnSignKeyFrame = gameTextures.turnSignAnim.getKeyFrameIndex(gameTextures.turnSignElapsedTime);
        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            if (turnSignKeyFrame == 40 || turnSignKeyFrame == 73 || turnSignKeyFrame == 106) {
                gameTextures.turnSignWhichPlayer++;
            } else if (turnSignKeyFrame == 139) {
                gameTextures.turnSignWhichPlayer = 1;
                gameTextures.turnSignElapsedTime = 0;
            }
            gameTextures.turnSignElapsedTime += 3 * Gdx.graphics.getDeltaTime();
        }
        drawWhichPlayersTurnAnim();
    }

    public void drawWhichPlayersTurnAnim() {
        if ((turnSignKeyFrame > 0 && turnSignKeyFrame < 40) || (turnSignKeyFrame > 40 && turnSignKeyFrame < 73) || (turnSignKeyFrame > 73 && turnSignKeyFrame < 106) || (turnSignKeyFrame > 106 && turnSignKeyFrame < 139)) {
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if (turnSignKeyFrame != 40 && turnSignKeyFrame != 73 && turnSignKeyFrame != 106 && turnSignKeyFrame != 139) {
            game.batch.draw(gameTextures.turnSignAnim.getKeyFrame(gameTextures.turnSignElapsedTime, false), 753, 469, 420, 140);
        }
    }


    public void drawBackGround() {
        game.batch.draw(gameTextures.dayParkBackground, 0, 0, 1920, 1080);
    }

    public void drawIceCreamAndSwingAnim() {
        gameTextures.loopElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(gameTextures.iceCreamAnim.getKeyFrame(gameTextures.loopElapsedTime, true),
                0, 888, 179, 192);
        game.batch.draw(gameTextures.swingAnim.getKeyFrame(gameTextures.loopElapsedTime, true),
                1009, 137, 199, 95);
    }

    public void drawYellowBusAnim() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B) && !gameTextures.yellowBusAnimStarted) {
            gameTextures.yellowBusAnimStarted = true;
        } else if (gameTextures.yellowBusAnim.isAnimationFinished(gameTextures.yellowBusElapsedTime) && gameTextures.yellowBusAnimStarted) {
            gameTextures.yellowBusAnimStarted = false;
            gameTextures.yellowBusElapsedTime = 0;
        }
        if (gameTextures.yellowBusAnimStarted) {
            gameTextures.yellowBusElapsedTime += Gdx.graphics.getDeltaTime();
        }
        game.batch.draw(gameTextures.yellowBusAnim.getKeyFrame(gameTextures.yellowBusElapsedTime, false), 1015, 0, 905, 1080);
    }

    public void drawDiceAnim() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !gameTextures.diceAnimStarted) {
            gameTextures.diceAnimStarted = true;
        }
        if (gameTextures.diceAnimStarted) {
            if (!gameTextures.diceAnim.isAnimationFinished(gameTextures.diceElapsedTime)) {
                gameTextures.diceElapsedTime += Gdx.graphics.getDeltaTime();
                game.batch.draw(gameTextures.diceAnim.getKeyFrame(gameTextures.diceElapsedTime, false), 300, 0, 1000, 850);
            } else {
                Random rand = new Random();
                randNumber = 6;//rand.nextInt(6) + 1;
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
        gameTextures.yellowPlayerAtlas.dispose();
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

class Player {

    float playerElapsedTime;
    int playerNumber;
    int activePawn;
    Animation<TextureRegion> playerAnimation;

    Pawn[] pawns = {new Pawn(0),
            new Pawn(0),
            new Pawn(0),
            new Pawn(0)};
    int numbersOfWinPawns;

    public Player(int playerNumber, float playerElapsedTime) {
        this.playerElapsedTime = playerElapsedTime;
        this.playerNumber = playerNumber;
        activePawn = 0;
        numbersOfWinPawns = 0;
        pawns[0].playerElapsedTime = playerElapsedTime;
        pawns[1].playerElapsedTime = playerElapsedTime;
        pawns[2].playerElapsedTime = playerElapsedTime;
        pawns[3].playerElapsedTime = playerElapsedTime;
    }
}

class Pawn {
    public float playerElapsedTime;
    int position; // 49 yellow start = 0
    boolean active = false;

    public Pawn(int position) {
        this.position = position;
    }
}

