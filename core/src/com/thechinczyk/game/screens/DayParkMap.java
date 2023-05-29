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


    /**
     * pola specjalne :
     * wykżynik:
     * - 16, 32, 43
     * znak zapytania:
     * - 3, 11, 20, 25, 37
     * <p>
     * start pionków :
     * - 0, 7, 24, 29
     */

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

    public DayParkMap(MyTheChinczyk game) {
        this.game = game;
    }

    GameTextures gameTextures;

    @Override
    public void show() {
        for (int i = 0; i < game.playerCount; i++) {
            if (i == 0) {
                Player player = new Player(i, startPlayerElapsedTime[i], 0);
                Players.add(player);
            }
            if (i == 1) {
                Player player = new Player(i, startPlayerElapsedTime[i], 7);
                Players.add(player);
            }
        }
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
        } else if (player.activePawn == 0 && randNumber == 1) {
            addPawn(player);
        } else if (player.activePawn >= 1 && player.activePawn <= 4) {
            if (!pawnChoose) {
                managePawns(player);
            } else {
                changeAnimationPawn(player, pawToChange);
            }
        } else if (player.activePawn == 0) {
            setPlayerNumberTurn();
            throwDice = false;
        }
    }

    private void managePawns(Player player) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && player.pawns[0].active) {
            if (canIMovePawn(player, 0)) {
                manageParticularPawn(player, 0);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && player.pawns[1].active) {
            if (canIMovePawn(player, 1)) {
                manageParticularPawn(player, 1);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && player.pawns[2].active) {
            if (canIMovePawn(player, 2)) {
                manageParticularPawn(player, 2);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) && player.pawns[3].active) {
            if (canIMovePawn(player, 3)) {
                manageParticularPawn(player, 3);
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && randNumber == 1) {
            if (canIAddPawn()) {
                addPawn(player);
            }
        }
    }

    private boolean canIAddPawn() {
        for (Player player : Players){
            for (Pawn pawn : player.pawns) {
                if (pawn.positionAtMap == player.playerBase) {
                    return false;
                }
            }
        }
        return true;
    }


    private boolean canIMovePawn(Player player, int x) {
        for (Pawn pawn : player.pawns) {
            if (pawn != player.pawns[x] && pawn.active) {
                if (pawn.positionAtMap == player.pawns[x].positionAtMap + randNumber) {
                    return false;
                }
            }
        }
        return true;
    }

    private void manageParticularPawn(Player player, int x) {
        player.pawns[x].positionAtMap = (player.pawns[x].positionAtMap + randNumber) % 53;
        pawToChange = x;
        pawnChoose = true;
    }
    private void addPawn(Player player) {
        if (player.activePawn <= 4) {
            for (int i = 0; i < 4; i++) {
                if (!player.pawns[i].active) {
                    player.activePawn++;
                    player.pawns[i].alive(player.playerBase);
                    break;
                }
            }
            setPlayerNumberTurn();
            throwDice = false;
            //System.out.println("number of players " + player.activePawn);
        }
    }

    private void killSomebody(Player player, int pawNumber) {
        for (Player playerToKill : Players) {
            if (playerToKill != player) {
                for (Pawn pawn : playerToKill.pawns) {
                    if (pawn.positionAtMap == player.pawns[pawNumber].positionAtMap) {
                        pawn.dead();
                        playerToKill.activePawn--;
                    }
                }
            }
        }
    }

    private void changeAnimationPawn(Player player, int pawNumber) {
        if (randNumber >= 1 && pawNumber != -1) {
            if (flag) {
                player.pawns[pawNumber].playerElapsedTime += 3 * Gdx.graphics.getDeltaTime();
                flag = false;
            }
            if (gameTextures.yellowPlayer1Anim.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10 != 0) {
                player.pawns[pawNumber].playerElapsedTime += Gdx.graphics.getDeltaTime();
            } else {
                randNumber--;
                flag = true;
            }
        } else {
            killSomebody(player, pawNumber);
            setPlayerNumberTurn();
            resetFlags();
        }
    }

    private void resetFlags() {
        pawToChange = -1;
        throwDice = false;
        pawnChoose = false;
        randNumber = 0;
    }




    void drawPlayerPawns(Player player) {
        for (int i = 0; i < 4; i++) {
            if (player.pawns[i].active) {
                drawPawn(player, i);
            }
        }
    }

    private void drawPawn(Player player, int pawnNumber) {
        if (pawnNumber != -1) {
            if (player.pawns[pawnNumber].playerElapsedTime < 8.03f || player.pawns[pawnNumber].playerElapsedTime > 16.35f) {
                game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                        0, 0, 1080, 1080);
            } else {
                game.batch.draw(gameTextures.yellowPlayer1Anim.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
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
                randNumber = 1;//rand.nextInt(6) + 1;
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

class Player {

    float playerElapsedTime;
    int playerNumber;
    int activePawn;
    int playerBase;

    Pawn[] pawns = {new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase)};
    int numbersOfWinPawns;

    public Player(int playerNumber, float playerElapsedTime, int playerBase) {
        this.playerElapsedTime = playerElapsedTime;
        this.playerNumber = playerNumber;
        this.playerBase = playerBase;
        pawns[0].setPlayerElapsedTime(playerElapsedTime);
        pawns[1].setPlayerElapsedTime(playerElapsedTime);
        pawns[2].setPlayerElapsedTime(playerElapsedTime);
        pawns[3].setPlayerElapsedTime(playerElapsedTime);
        activePawn = 0;
        numbersOfWinPawns = 0;
    }
}

class Pawn {
    public float playerElapsedTime;
    public float ELAPSED_TIME;//gdy będzie każda animacja tego nie będzie
    int positionAtMap; // zaczyna się od bazy czyli np zielony ma 7

    boolean active;

    public Pawn(int position) {
        this.positionAtMap = -1;
        this.active = false;
    }

    public void setPlayerElapsedTime(float i) {
        this.playerElapsedTime = i;
        this.ELAPSED_TIME = i;
    }

    public void alive(int position) {
        this.positionAtMap = position;
        this.active = true;
    }

    public void dead() {
        this.positionAtMap = -1;
        this.active = false;
        playerElapsedTime = ELAPSED_TIME;//będzie 0 przypisywane gdy dostanę każdą animację
    }
}

