package com.thechinczyk.game.screens;

import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import com.badlogic.gdx.utils.Align;

public class DayParkMap implements Screen {

    public static final float FIRST_TURN_SIGN_ELAPSED_TIME = 1.3338771F;
    public static final float TURN_SIGN_ELAPSED_TIME_OFFSET = 1.1738771f;
    MyTheChinczyk game;
    Random rand = new Random();
    int randNumber = 0;
    int diceRoll = 0;

    public Music ambient;
    public Music music;

    public Sound pawnSound;
    public Sound diceSound;
    public Sound cardSound;
    public Sound endGameSound;
    public Sound turnChangeSound;
    public Sound outOfBaseSound;
    public Sound captureSound;
    public Sound busSound;

    /**
     * pola specjalne :
     * wykrzynik:
     * - 16, 32, 43
     * znak zapytania:
     * - 3, 11, 20, 25, 37
     * start pionków :
     * - 0, 7, 24, 29
     */

    //elapsedtime player1 yellow 0
    //elapsedtime player2 green 2.3362615
    //elapsedtime player3 blue 8.000907
    //elapsedtime player4 pink 9.67303
    //float[] startPlayerElapsedTime = {0f, 2.3362615f, 8.000907f, 9.67303f};
    int[] startPlayerBase = {0, 7, 24, 29};
    int[] winsPlayer = {-1, -1, -1, -1};
    int winPlayerPosition = 0;
    boolean endGame = false;
    private boolean cardIsLoading;
    private int randEvent;
    private int randType;
    private boolean tempFlag;

    enum ColorOfAllPlayers {None,Yellow, Green, Blue, Pink}
    ColorOfAllPlayers playerToKillRightNow;
    ColorOfAllPlayers playerKiller;
    boolean startCaptureAnim;
    int turnSignKeyFrame;
    boolean throwDice = false;
    int playerNumberTurn;
    boolean changeTurn = true;
    boolean skipFirstAnimation = true;
    int count = 0;
    int jumpAnimation = 0;
    int whichPlayer = 0;
    int whichPawn = 0;
    public ArrayList<Player> Players = new ArrayList<>();

    boolean[] miniGamePlaying;
    boolean miniGameOutput;
    MiniGamesTypes miniGameType;
    MiniGame miniGame;
    boolean cardLoaded;
    MiniGameOutput miniGameResult;

    float prevAnimPlayerModulo;
    boolean firstAnimPlayerThisRound;

    RandomEvent[] randomEvents;
    int missedPlayers = 0;
    public DayParkMap(MyTheChinczyk game) {
        this.miniGamePlaying = new boolean[3];
        this.miniGameOutput = false;
        this.randomEvents = RandomEvent.createRandomEventsArray();
        this.miniGameType = MiniGamesTypes.NONE;
        this.game = game;
        this.miniGame = new MiniGame(game.batch, this);
        this.miniGameResult = MiniGameOutput.NONE;
        this.tempFlag = true;
        this.firstAnimPlayerThisRound = true;
        this.prevAnimPlayerModulo = 0;
        RandomEvent.setMap(this);
    }

    GameTextures gameTextures;

    @Override
    public void show() {
        gameTextures = new GameTextures(game);
        soundInit();

        for (int i = 0; i < game.playerCount; i++) {
            Player player = new Player(i, startPlayerBase[i]);
            Players.add(player);
            if (i == 0) {
                player.moveAnimation = gameTextures.yellowPlayerAnim;
                player.baseOfPlayer.add(gameTextures.yellowBase0);
                player.baseOfPlayer.add(gameTextures.yellowBase1);
                player.baseOfPlayer.add(gameTextures.yellowBase2);
                player.baseOfPlayer.add(gameTextures.yellowBase3);
                player.playerColor = ColorOfAllPlayers.Yellow;
            } else if (i==1) {
                player.moveAnimation = gameTextures.greenPlayerAnim;
                player.baseOfPlayer.add(gameTextures.greenBase0);
                player.baseOfPlayer.add(gameTextures.greenBase1);
                player.baseOfPlayer.add(gameTextures.greenBase2);
                player.baseOfPlayer.add(gameTextures.greenBase3);
                player.playerColor = ColorOfAllPlayers.Green;
            } else if(i==2){
                player.moveAnimation = gameTextures.bluePlayerAnim;
                player.baseOfPlayer.add(gameTextures.blueBase0);
                player.baseOfPlayer.add(gameTextures.blueBase1);
                player.baseOfPlayer.add(gameTextures.blueBase2);
                player.baseOfPlayer.add(gameTextures.blueBase3);
                player.playerColor = ColorOfAllPlayers.Blue;
            } else if(i==3){
                player.moveAnimation = gameTextures.pinkPlayerAnim;
                player.baseOfPlayer.add(gameTextures.pinkBase0);
                player.baseOfPlayer.add(gameTextures.pinkBase1);
                player.baseOfPlayer.add(gameTextures.pinkBase2);
                player.baseOfPlayer.add(gameTextures.pinkBase3);
                player.playerColor = ColorOfAllPlayers.Pink;
            }
        }
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

        //Powrót do menu głównego
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            quitGame();
        }

        drawDice();

        drawBases();

        drawPlayers(playerNumberTurn);
        managePlayer(playerNumberTurn);

        //Obsługa animacji tabliczek oznaczających nową turę
        changeWhichPlayersTurn();

        //Wyświetlanie tabliczek w rogu z kolorem aktualnego gracza
        drawWhichPlayersTurnUI();

        //Animacja autobusu
        drawBusAnim();

        //Obsługa animacji zbijania pionków
        drawPawnCaptureAnim(playerToKillRightNow, playerKiller);

        //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
        game.batch.draw(gameTextures.dayParkTopground, 0, 0, 1920, 1080);

        scoreBoard();

        game.batch.end();
    }

    public void quitGame(){
        music.stop();
        ambient.stop();
        game.gameScreen = 3;
        game.setScreen(game.MenuLoadingScreen);
    }

    public void soundInit(){
        pawnSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/pawnSound.mp3"));
        diceSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/diceSound.mp3"));
        cardSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/cardSound.mp3"));
        endGameSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/endGameSound.mp3"));
        turnChangeSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/turnChangeSound.mp3"));
        outOfBaseSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/outOfBaseSound.mp3"));
        captureSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/captureSound.mp3"));
        busSound = Gdx.audio.newSound(Gdx.files.internal("Map1/Sound/busSound.mp3"));

        ambient = Gdx.audio.newMusic(Gdx.files.internal("Map1/Sound/ambient.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("Map1/Sound/Podington Bear - Blue Highway.mp3"));
        ambient.setLooping(true);
        ambient.setVolume(0.4f);
        music.setLooping(true);
        music.setVolume(0.07f);
        ambient.play();
        music.play();
    }

    public void drawBases(){
        for (int i = 0; i < game.playerCount; i++) {
            Player player = Players.get(i);
            drawPlayerBase(player);
        }
    }

    public void drawPlayerBase(Player player){
        for (int i=0;i<4;i++) {
            if (player.pawnsInBase == i) {
                game.batch.draw(player.baseOfPlayer.get(i), 810, 365, 300, 350);
            }
        }
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
        if (player.win) {
            setPlayerNumberTurn();
        }
        if (!throwDice) {
            drawDiceAnim(player);
        } else if (player.activePawn == 0 && randNumber == 6) {
            addPawn(player);
        } else if (player.activePawn >= 1 && player.activePawn <= 4) {
            if (!pawnChoose) {
                canAnyoneMove(player);
                managePawns(player);
            } else if(!player.pawns[pawToChange].onTheBus){
                changeAnimationPawn(player, pawToChange);
            }else {
                changeAnimationBus(player, pawToChange);
            }
        } else if (player.activePawn == 0) {
            setPlayerNumberTurn();
            throwDice = false;
        }
    }

    private void managePawns(Player player) {
        Map<Integer, Integer> map = new TreeMap<>();
        putPlayerToMap(player, map, 0);
        putPlayerToMap(player, map, 1);
        putPlayerToMap(player, map, 2);
        putPlayerToMap(player, map, 3);
        Integer []a = map.values().toArray(new Integer[0]);
        int first = 0;
        int second = 0;
        int third = 0;
        int fourth = 0;
        int i = 0;
        for(int j = a.length - 1; j>=0 ;j--){
            if(i == 0){
                first = a[j];
                i++;
            }else if(i == 1){
                second = a[j];
                i++;
            }else if(i == 2){
                third = a[j];
                i++;
            }else if(i == 3){
                fourth = a[j];
                i++;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) &&
                player.pawns[first].active && a.length >= 1) {
            enforcedPlayer(player, first);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) &&
                player.pawns[second].active && a.length >= 2) {
            enforcedPlayer(player, second);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) &&
                player.pawns[third].active && a.length >= 3) {
            enforcedPlayer(player, third);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) &&
                player.pawns[fourth].active && a.length >= 4) {
            enforcedPlayer(player, fourth);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && randNumber == 6) {
            if (canIAddPawn(player, player.playerBase)) {
                addPawn(player);
            }
        }
    }

    private static void putPlayerToMap(Player player, Map<Integer, Integer> map, int x) {
        if(player.pawns[x].active && !player.pawns[x].win){
            map.put(player.pawns[x].position, x);
        }
    }

    private void enforcedPlayer(Player player, int x) {
        if (canIMovePawn(player, x)) {
            manageParticularPawn(player, x);
            if(player.pawns[x].position >= 50){
                if (!player.pawns[x].win) {
                    player.pawns[x].win = true;
                    player.numbersOfWinPawns++;
                }
            }
        }
    }

    private void canAnyoneMove(Player player){
        int numberOfFalse = 0;
        for (int i = 0; i < 4;i++){
            if(player.pawns[i].active){
                if(!canIMovePawn(player, i)){
                    numberOfFalse++;
                }
            }
        }
        if(numberOfFalse == player.activePawn){
            if(canIAddPawn(player, player.playerBase) && randNumber == 6){
                addPawn(player);
            }else{
                setPlayerNumberTurn();
            }
            resetFlags();
        }
    }

    private boolean canIAddPawn(Player p, int playerBase) {
        if(p.activePawn==4)
            return false;
        for (Player player : Players) {
            for (Pawn pawn : player.pawns) {
                if (pawn.positionAtMap == playerBase) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean canIMovePawn(Player player, int x) {
        if(player.pawns[x].win){
            return false;
        }
        if(player.pawns[x].position + randNumber + player.additionalMovement > 53){
            return false;
        }
        for (Pawn pawn : player.pawns) {
            if (pawn != player.pawns[x] && pawn.active) {
                if (pawn.position == player.pawns[x].position + randNumber + player.additionalMovement) {
                    return false;
                }
            }
        }
        return true;
    }

    private void manageParticularPawn(Player player, int x) {
        player.pawns[x].positionAtMap = (player.pawns[x].positionAtMap + randNumber + player.additionalMovement) % 49;
        player.pawns[x].position += randNumber + player.additionalMovement;
        pawToChange = x;
        pawnChoose = true;
    }

    public boolean isPlayerOnTheBus(Player player, int x){
        return player.pawns[x].positionAtMap == 32;
    }

    public void bus(Pawn pawn){
        pawn.onTheBus = true;
        pawn.positionAtMap += 6;
        pawn.position += 6;
        randNumber = 6;
    }

    private void addPawn(Player player) {
        if (player.activePawn <= 4) {
            for (int i = 0; i < 4; i++) {
                if (!player.pawns[i].active) {
                    outOfBaseSound.play(0.5f);
                    player.activePawn++;
                    player.pawns[i].alive(player.playerBase);
                    killSomebody(player, i);
                    player.pawnsInBase--;
                    break;
                }
            }
            setPlayerNumberTurn();
            throwDice = false;
        }
    }

    private void killSomebody(Player player, int pawNumber) {
        for (Player playerToKill : Players) {
            if (playerToKill != player) {
                for (Pawn pawn : playerToKill.pawns) {
                    if (pawn.positionAtMap == player.pawns[pawNumber].positionAtMap &&
                            player.pawns[pawNumber].position < 50 && pawn.position < 50 &&
                            player.pawns[pawNumber].position >= 0 && pawn.position >= 0) {
                        startCaptureAnim = true;
                        playerToKillRightNow = playerToKill.playerColor;
                        playerKiller = player.playerColor;
                        pawn.dead();
                        playerToKill.activePawn--;
                        playerToKill.pawnsInBase++;
                    }
                }
            }
        }
    }

    private void changeAnimationPawn(Player player, int pawNumber) {
        if (randNumber + player.additionalMovement>= 1 && pawNumber != -1 && !this.cardIsLoading) {
            System.out.print(player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10);
            if (flag) {
                pawnSound.play(0.7f);
                player.pawns[pawNumber].playerElapsedTime += 3 * Gdx.graphics.getDeltaTime();
                flag = false;
            }
            if (player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10 >= prevAnimPlayerModulo || firstAnimPlayerThisRound || prevAnimPlayerModulo == 0) {
                prevAnimPlayerModulo = player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10;
                this.prevAnimPlayerModulo = player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10;
                player.pawns[pawNumber].playerElapsedTime += Gdx.graphics.getDeltaTime();
            } else {
                this.prevAnimPlayerModulo = player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10;
                randNumber--;
                flag = true;
            }
            firstAnimPlayerThisRound = false;

            if (randNumber + player.additionalMovement == 0)
                player.additionalMovement = 0;
        } else {
            if(isPlayerOnTheBus(player, pawNumber)){
                bus(player.pawns[pawNumber]);
            }else {

                killSomebody(player, pawNumber);
                randomEventSystem(playerNumberTurn, pawNumber);
                if (this.miniGameOutput) {
                    RandomEvent.drawMiniGameOutput(player, player.pawns[pawNumber]);
                }
                if (!this.miniGameOutput && miniGamePlaying() && !this.cardIsLoading && this.randNumber <= 0) {
                    setPlayerNumberTurn();
                    resetFlags();
                }
                if(player.numbersOfWinPawns==4){
                    player.win = true;
                    winsPlayer[winPlayerPosition] = player.playerNumber;
                    winPlayerPosition ++;
                    if(winPlayerPosition == game.playerCount - 1){
                        endGame = true;
                    }
                }
            }
        }
    }

    private boolean miniGamePlaying() {
        return this.miniGamePlaying[0] || this.miniGamePlaying[1] || this.miniGamePlaying[2];
    }

    private void changeAnimationBus(Player player, int pawNumber){
        if (randNumber >= 1 && pawNumber != -1) {
            if (flag) {
                player.pawns[pawNumber].playerElapsedTime += 3 * Gdx.graphics.getDeltaTime();
                flag = false;
            }
            if (player.moveAnimation.getKeyFrameIndex(player.pawns[pawNumber].playerElapsedTime) % 10 != 0) {
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
        cardLoaded = false;
        tempFlag = true;
        randNumber = 0;
        this.firstAnimPlayerThisRound = true;
        this.prevAnimPlayerModulo = 0;
    }

    void drawPlayerPawns(Player player) {
        for (int i = 0; i < 4; i++) {
            if ((player.pawns[i].active || player.pawns[i].win) && !player.pawns[i].onTheBus) {
                drawPawn(player, i);
            }
        }
    }

    private void drawPawn(Player player, int pawnNumber) {
        if (pawnNumber != -1) {
            if (player.moveAnimation == gameTextures.yellowPlayerAnim) {
                if (player.pawns[pawnNumber].playerElapsedTime < 8.05f || player.pawns[pawnNumber].playerElapsedTime > 16.37f) {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                } else {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
            } else if (player.moveAnimation == gameTextures.greenPlayerAnim) {
                if (player.pawns[pawnNumber].playerElapsedTime < 5.7f || player.pawns[pawnNumber].playerElapsedTime > 14.03f) {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                } else {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
            }else if(player.moveAnimation==gameTextures.bluePlayerAnim){
                if(player.pawns[pawnNumber].playerElapsedTime < 8.37f){
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                } else {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                }
            }else if(player.moveAnimation==gameTextures.pinkPlayerAnim){
                if(player.pawns[pawnNumber].playerElapsedTime < 6.7f || player.pawns[pawnNumber].playerElapsedTime > 14.71f){
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
                else{
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                }
            }
        }
    }

    public void drawCardAnim(String message){
        if((!gameTextures.cardAnimStarted)) {
            //Wysunięcie karty
            gameTextures.cardAnimStarted = true;
            this.cardIsLoading = true;
            cardSound.play(0.9f);
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && gameTextures.cardAnim.isAnimationFinished(gameTextures.cardElapsedTime)){
            //Zamknięcie karty
            this.miniGameOutput = false;
            this.cardLoaded = true;
            this.cardIsLoading = false;
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
        if((this.miniGameType == MiniGamesTypes.SPACE_INVADERS || gameTextures.cardAnimStarted) && !this.miniGameOutput && !this.miniGamePlaying[1] && !this.miniGamePlaying[2] && !this.cardLoaded) {
            drawCardAnim(message);
        }
        if (this.miniGamePlaying[0] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[0]) {
                this.miniGame.loadTextures(MiniGamesTypes.SPACE_INVADERS);
                this.miniGame.isLoaded[0] = true;
            }
            this.miniGame.menuSpaceInvaders.Draw();
        }
    }

    public void drawMathMiniGameMenu(String message) {
        if((this.miniGameType == MiniGamesTypes.MATH || gameTextures.cardAnimStarted) && !this.miniGameOutput && !this.miniGamePlaying[0] && !this.miniGamePlaying[2] && !this.cardLoaded) {
            drawCardAnim(message);
        }
        if (this.miniGamePlaying[1] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[1]) {
                this.miniGame.loadTextures(MiniGamesTypes.MATH);
                this.miniGame.isLoaded[1] = true;
            }
            this.miniGame.menuMath.Draw();
        }
    }

    public void drawMemoryMiniGameMenu(String message) {
        if((this.miniGameType == MiniGamesTypes.MEMORY || gameTextures.cardAnimStarted) && !this.miniGameOutput && !this.miniGamePlaying[0] && !this.miniGamePlaying[1] && !this.cardLoaded) {
            drawCardAnim(message);
        }
        if (this.miniGamePlaying[2] && !this.gameTextures.cardAnimStarted) {
            if (!this.miniGame.isLoaded[2]) {
                this.miniGame.loadTextures(MiniGamesTypes.MEMORY);
                this.miniGame.isLoaded[2] = true;
            }
            this.miniGame.menuMemory.Draw();
        }
    }

    public void drawMiniGameTimer(int x, int y) {
        gameTextures.timerElapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(gameTextures.timerAnim.getKeyFrame(gameTextures.timerElapsedTime, true),
                x, y, 100, 100);
    }

    public void unlockMap() {
        for (int i = 0; i<3; i++)
            this.miniGamePlaying[i] = false;

        this.miniGameOutput = true;
    }

    public void setPlayerNumberTurn() {
        if(!endGame) {
            if (playerNumberTurn < game.playerCount - 1) {
                playerNumberTurn++;
            } else {
                playerNumberTurn = 0;
            }
            while (Players.get(playerNumberTurn).pausingRounds != 0) {
                this.missedPlayers++;
                Players.get(playerNumberTurn).pausingRounds --;

                if (playerNumberTurn < game.playerCount - 1) {
                    playerNumberTurn++;
                } else {
                    playerNumberTurn = 0;
                }
            }
            changeTurn = true;
        }
    }

    public void drawWhichPlayersTurnUI() {
        if (playerNumberTurn == 0) {
            game.batch.draw(gameTextures.turnSignYellowBackground, 1190, 980, 300, 100);
        } else if (playerNumberTurn == 1) {
            game.batch.draw(gameTextures.turnSignGreenBackground, 1190, 980, 300, 100);
        } else if (playerNumberTurn == 2) {
            game.batch.draw(gameTextures.turnSignBlueBackground, 1190, 980, 300, 100);
        } else {
            game.batch.draw(gameTextures.turnSignPinkBackground, 1190, 980, 300, 100);
        }
    }

    public void drawWhichPlayersTurnAnim() {
        if ((turnSignKeyFrame > 0 && turnSignKeyFrame < 40) ||
                (turnSignKeyFrame > 40 && turnSignKeyFrame < 73) ||
                (turnSignKeyFrame > 73 && turnSignKeyFrame < 106) ||
                (turnSignKeyFrame > 106 && turnSignKeyFrame < 139)) {
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        }
        if (turnSignKeyFrame != 40 && turnSignKeyFrame != 73 && turnSignKeyFrame != 106 && turnSignKeyFrame != 139) {
            game.batch.draw(gameTextures.turnSignAnim.getKeyFrame(gameTextures.turnSignElapsedTime, false), 753, 469, 420, 140);
        }
    }

    public void changeWhichPlayersTurn(){
        turnSignKeyFrame = gameTextures.turnSignAnim.getKeyFrameIndex(gameTextures.turnSignElapsedTime);
        if(changeTurn){
            turnChangeSound.play(0.3f);
            if(turnSignKeyFrame == 40 || turnSignKeyFrame == 73 || turnSignKeyFrame == 106){
                skipFirstAnimation = false;
            }
            else if(turnSignKeyFrame == 139){
                gameTextures.turnSignElapsedTime = 0;
            }

            this.randEvent = rand.nextInt(5);
            this.randType = rand.nextInt(2);

            setAnimationTimeForPlayersTurn();

            setAnimationForAppropriatePlayerCount();

            changeTurn=false;
        }
        drawWhichPlayersTurnAnim();
    }

    private void setAnimationTimeForPlayersTurn() {
        if(!skipFirstAnimation) {
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
            gameTextures.turnSignElapsedTime += Gdx.graphics.getDeltaTime();
        } else{
            gameTextures.turnSignElapsedTime = FIRST_TURN_SIGN_ELAPSED_TIME;
        }
    }

    private void setAnimationForAppropriatePlayerCount() {
        gameTextures.turnSignElapsedTime = this.playerNumberTurn* TURN_SIGN_ELAPSED_TIME_OFFSET + 0.2f;
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

    public void drawBusAnim(){
        for (int i = 0; i < game.playerCount; i++) {
            Player player = Players.get(i);
            for (int j = 0; j < player.activePawn; j++) {
                //WYBÓR KOLORU PIONKA WSIADAJĄCEGO DO AUTOBUSU
                if (player.playerNumber == 0 && player.pawns[j].onTheBus && gameTextures.BusAnimStarted == 0) {
                    //Żółty autobus
                    gameTextures.BusAnimStarted = 1;
                    setAppropriateVariablesInBusAnimation(i, j);
                } else if (player.playerNumber == 1 && player.pawns[j].onTheBus && gameTextures.BusAnimStarted == 0) {
                    //Zielony autobus
                    gameTextures.BusAnimStarted = 2;
                    gameTextures.BusElapsedTime = 0.5805f;
                    setAppropriateVariablesInBusAnimation(i, j);
                } else if (player.playerNumber == 2 && player.pawns[j].onTheBus && gameTextures.BusAnimStarted == 0) {
                    //Niebieski autobus
                    gameTextures.BusAnimStarted = 3;
                    gameTextures.BusElapsedTime = 1.1444f;
                    setAppropriateVariablesInBusAnimation(i, j);
                } else if (player.playerNumber == 3 && player.pawns[j].onTheBus && gameTextures.BusAnimStarted == 0) {
                    //Różowy autobus
                    gameTextures.BusAnimStarted = 4;
                    gameTextures.BusElapsedTime = 1.7144f;
                    setAppropriateVariablesInBusAnimation(i, j);
                } else if (gameTextures.BusAnim.isAnimationFinished(gameTextures.BusElapsedTime) && gameTextures.BusAnimStarted != 0) {
                    //Stan spoczynku animacji
                    gameTextures.BusAnimStarted = 0;
                    gameTextures.BusElapsedTime = 0;
                }
            }
        }

        //DZIELENIE ANIMACJI ZE WZGLĘDU NA KOLORY
        if (gameTextures.BusAnimStarted == 1) {
            //Żółty autobus
            setAppropriateAnimationForYellowBus();
        } else if (gameTextures.BusAnimStarted == 2) {
            //Zielony autobus
            setAppropriateAnimationForGreenBus();
        } else if (gameTextures.BusAnimStarted == 3) {
            //Niebieski autobus
            setAppropriateAnimationForBlueBus();
        } else if (gameTextures.BusAnimStarted == 4) {
            //Różowy autobus
            setAppropriateAnimationForPinkBus();
        }

        //Wyświetlenie animacji autobusu
        if (gameTextures.BusAnimStarted != 0) {
            gameTextures.BusElapsedTime += Gdx.graphics.getDeltaTime();
        }
        game.batch.draw(gameTextures.BusAnim.getKeyFrame(gameTextures.BusElapsedTime, false), 1526, 0, 394, 1080);
    }

    private void setAppropriateAnimationForPinkBus() {
        if (gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f) {
            //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
            gameTextures.BusElapsedTime = 11.3432f;
        } else if (gameTextures.BusElapsedTime > 13.71f) {
            Player player = Players.get(whichPlayer);
            player.pawns[whichPawn].onTheBus = false;
        }
    }

    private void setAppropriateAnimationForBlueBus() {
        if (gameTextures.BusElapsedTime > 1.68 && gameTextures.BusElapsedTime < 1.7) {
            //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
            gameTextures.BusElapsedTime = 2.3f;
        } else if (gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f) {
            //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
            gameTextures.BusElapsedTime = 8.9455f;
        } else if (gameTextures.BusElapsedTime > 11.30f) {
            //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
            setFlagAndElapseTimeToStopBus();
        }
    }

    private void setAppropriateAnimationForGreenBus() {
        if (gameTextures.BusElapsedTime > 1.11 && gameTextures.BusElapsedTime < 1.15) {
            //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
            gameTextures.BusElapsedTime = 2.3f;
        } else if (gameTextures.BusElapsedTime > 4.16f && gameTextures.BusElapsedTime < 4.2f) {
            //To przeskakuje do końcówki animacji z pionkiem o odpowiednim kolorze
            gameTextures.BusElapsedTime = 6.5477f;
        } else if (gameTextures.BusElapsedTime > 8.92f) {
            //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
            setFlagAndElapseTimeToStopBus();
        }
    }

    private void setAppropriateAnimationForYellowBus() {
        if (gameTextures.BusElapsedTime > 0.54 && gameTextures.BusElapsedTime < 0.6) {
            //Jak pionek wejdzie to to pomija animacje innych kolorów wchodzących do busa
            gameTextures.BusElapsedTime = 2.3f;
        } else if (gameTextures.BusElapsedTime > 6.51f) {
            //Jak animacja się skończy to to pomija końcówki animacji innych kolorów
            setFlagAndElapseTimeToStopBus();
        }
    }

    private void setFlagAndElapseTimeToStopBus() {
        Player player = Players.get(whichPlayer);
        player.pawns[whichPawn].onTheBus = false;
        gameTextures.BusElapsedTime = 100f;
    }

    private void setAppropriateVariablesInBusAnimation(int i, int j) {
        busSound.play(0.3f);
        whichPlayer = i;
        whichPawn = j;
    }

    public void drawDiceAnim(Player player) {// losuje liczbe oraz wyświetla animację losowania
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !gameTextures.diceAnimStarted) {
            gameTextures.diceAnimStarted = true;
            diceSound.play(0.5f);
        }
        if (gameTextures.diceAnimStarted) {
            if (!gameTextures.diceAnim.isAnimationFinished(gameTextures.diceElapsedTime)) {
                gameTextures.diceElapsedTime += Gdx.graphics.getDeltaTime();
                game.batch.draw(gameTextures.diceAnim.getKeyFrame(gameTextures.diceElapsedTime, false), 300, 0, 1000, 850);
            } else {
                gameTextures.diceAnimStarted = false;
                gameTextures.diceElapsedTime = 0;
                throwDice = true;
            }
            if(gameTextures.diceAnim.getKeyFrameIndex(gameTextures.diceElapsedTime) == 55){
                randNumber = rand.nextInt(6) + 1;
                diceRoll = randNumber;
            }
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

    public void drawPawnCaptureAnim(ColorOfAllPlayers playerToKill, ColorOfAllPlayers playerKiller){
        if(gameTextures.pawnCapture1ElapsedTime!=0){
            //Odtwarzanie animacji zbijającego aż się nie skończy dla danego koloru
            startAnimationForPlayerKiller(playerKiller);

            //Sprawdzanie czy już czas na odtawarzanie animacji zbijanego pionka
            if(((playerKiller == ColorOfAllPlayers.Yellow && gameTextures.pawnCapture1ElapsedTime > 0.34f) ||
                    (playerKiller == ColorOfAllPlayers.Green && gameTextures.pawnCapture1ElapsedTime > 1.008f) ||
                    (playerKiller == ColorOfAllPlayers.Blue && gameTextures.pawnCapture1ElapsedTime > 1.6754f) ||
                    (playerKiller == ColorOfAllPlayers.Pink && gameTextures.pawnCapture1ElapsedTime > 2.34f))
            ){
                gameTextures.pawnCapture2ElapsedTime += Gdx.graphics.getDeltaTime();
            }

            //Liczy czas ogólny dla całej animacji
            gameTextures.pawnCaptureMainElapsedTime += Gdx.graphics.getDeltaTime();

            //Wyświetlanie obydwu animacji i tła
            game.batch.draw(gameTextures.pawnCaptureBackground, 0, 0, 1920, 1080);
            if(!((playerToKill == ColorOfAllPlayers.Yellow && gameTextures.pawnCapture2ElapsedTime > 0.48f) ||
                    (playerToKill == ColorOfAllPlayers.Green && gameTextures.pawnCapture2ElapsedTime > 1.04f) ||
                    (playerToKill == ColorOfAllPlayers.Blue && gameTextures.pawnCapture2ElapsedTime > 1.47f) ||
                    (playerToKill == ColorOfAllPlayers.Pink && gameTextures.pawnCapture2ElapsedTime > 1.92f))){
                //Tutaj sprawdzam jeszcze czy animacja zbijanego się nie skończyła i jeśli tak to go nie wyświetlam już
                startCaptureAnim = false;
                game.batch.draw(gameTextures.pawnCapture2Anim.getKeyFrame(gameTextures.pawnCapture2ElapsedTime, false), 640, 415, 350, 250);
            }
            game.batch.draw(gameTextures.pawnCapture1Anim.getKeyFrame(gameTextures.pawnCapture1ElapsedTime, false), 850, 365, 400, 350);

            //Sprawdzam czy ogólny czas animacji dobiegł do końca.
            if(gameTextures.pawnCaptureMainElapsedTime > 1.2f){
                gameTextures.pawnCapture1ElapsedTime = 0;
                gameTextures.pawnCaptureMainElapsedTime = 0;
            }
        }
        else if(startCaptureAnim && gameTextures.pawnCapture1ElapsedTime==0){
            //Ustawianie odpowiedniej klatki animacji w zależności od koloru zbijającego pionka
            setAnimationForPlayerKiller(playerKiller);

            //Ustawianie odpowiedniej klatki animacji w zależności od koloru zbijanego pionka
            setAnimationForPlayerToKill(playerToKill);

            captureSound.play(0.5f);
        }
    }

    private void setAnimationForPlayerToKill(ColorOfAllPlayers playerToKill) {
        if(playerToKill == ColorOfAllPlayers.Yellow){
            gameTextures.pawnCapture2ElapsedTime = 0.01f;
        }
        else if(playerToKill == ColorOfAllPlayers.Green){
            gameTextures.pawnCapture2ElapsedTime = 0.6364f;
        }
        else if(playerToKill == ColorOfAllPlayers.Blue){
            gameTextures.pawnCapture2ElapsedTime = 1.07f;
        }
        else if(playerToKill == ColorOfAllPlayers.Pink){
            gameTextures.pawnCapture2ElapsedTime = 1.505f;
        }
    }

    private void setAnimationForPlayerKiller(ColorOfAllPlayers playerKiller) {
        if(playerKiller == ColorOfAllPlayers.Yellow){
            gameTextures.pawnCapture1ElapsedTime = 0.01f;
        }
        else if(playerKiller == ColorOfAllPlayers.Green){
            gameTextures.pawnCapture1ElapsedTime = 0.6767f;
        }
        else if(playerKiller == ColorOfAllPlayers.Blue){
            gameTextures.pawnCapture1ElapsedTime = 1.3408f;
        }
        else if(playerKiller == ColorOfAllPlayers.Pink){
            gameTextures.pawnCapture1ElapsedTime = 2.01f;
        }
    }

    private void startAnimationForPlayerKiller(ColorOfAllPlayers playerKiller) {
        if(playerKiller == ColorOfAllPlayers.Yellow && gameTextures.pawnCapture1ElapsedTime < 0.65f){
            gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        else if(playerKiller == ColorOfAllPlayers.Green && gameTextures.pawnCapture1ElapsedTime < 1.30f){
            gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        else if(playerKiller == ColorOfAllPlayers.Blue && gameTextures.pawnCapture1ElapsedTime < 1.98f){
            gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
        else if(playerKiller == ColorOfAllPlayers.Pink && gameTextures.pawnCapture1ElapsedTime < 2.64f){
            gameTextures.pawnCapture1ElapsedTime += Gdx.graphics.getDeltaTime();
        }
    }

    public void scoreBoard(){
        if(!gameTextures.scoreBoardFlag && endGame){
            //Włączenie tablicy
            gameTextures.scoreBoardFlag = true;
            endGameSound.play(0.5f);
        }
        else if(gameTextures.scoreBoardFlag && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            //Wyłączenie tablicy. Tu mozna podmienić to żeby zamiast chowania tablicy był powrót do menu głównego
            gameTextures.scoreBoardFlag = false;
            quitGame();
        }
        else if(gameTextures.scoreBoardFlag){
            System.out.println(winsPlayer[0] + " " + winsPlayer[1] + " " + winsPlayer[2] + " " + winsPlayer[3]);

            setMissingWinsPlayer();

            ColorOfAllPlayers first = Players.get(winsPlayer[0]).playerColor;
            ColorOfAllPlayers second = Players.get(winsPlayer[1]).playerColor;
            ColorOfAllPlayers third = ColorOfAllPlayers.None;
            ColorOfAllPlayers fourth = ColorOfAllPlayers.None;
            if(game.playerCount>=3) {
                third = Players.get(winsPlayer[2]).playerColor;
                if (game.playerCount==4) {
                    fourth = Players.get(winsPlayer[3]).playerColor;
                }
            }

            //Rysowanie tła
            game.batch.draw(gameTextures.scoreBoardBackground, 0, 0, 1920, 1080);

            //Wyświetlanie odpowiednich graczy na poszczególnych miejscach
            showPlayersOnTheirPositions(first, second, third, fourth);
        }
    }

    private void showPlayersOnTheirPositions(ColorOfAllPlayers first, ColorOfAllPlayers second, ColorOfAllPlayers third, ColorOfAllPlayers fourth) {
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

    private void setMissingWinsPlayer() {
        for (int i=0;i<4;i++){
            if(winsPlayer[i]==-1){
                int lastPlayer = 0;
                for (int j=0;j<game.playerCount;j++){
                    Player player = Players.get(j);
                    if(!player.win)
                        lastPlayer = j;
                }
                winsPlayer[i] = lastPlayer;
                break;
            }
        }
    }

    private void randomEventSystem(int playerNumber, int pawnNumber) {
        Player player = Players.get(playerNumber);
        if (tempFlag)
            tempFlag = false;

        Pawn pawn = player.pawns[pawnNumber];
        if (pawn.positionAtMap == RandomEvent.ICE_CREAM_SPECIAL_FIELD_NUMBER && !pawn.win) {
            drawCardAnim(randomEvents[RandomEvent.ICE_CREAM_EVENTS + this.randType].cardMessage);
            player.additionalMovement = randomEvents[RandomEvent.ICE_CREAM_EVENTS + this.randType].nextRoundMovement;
            player.pausingRounds = randomEvents[RandomEvent.ICE_CREAM_EVENTS + this.randType].roundsMissed;
        }
        else if (pawn.positionAtMap == RandomEvent.TRAMPOLINE_SPECIAL_FIELD_NUMBER  && !pawn.win) {
            drawCardAnim(randomEvents[RandomEvent.TRAMPOLINE_EVENTS + this.randType].cardMessage);
            player.additionalMovement = randomEvents[RandomEvent.TRAMPOLINE_EVENTS + this.randType].nextRoundMovement;
        }
        else if (RandomEvent.checkIsFieldSpecial(pawn.positionAtMap) && !this.miniGameOutput && !pawn.win) {
            int randomEvent = this.randEvent;
            if (randomEvents[randomEvent].miniGameType == MiniGamesTypes.SPACE_INVADERS) {
                if (!this.miniGamePlaying[0])
                {
                    this.miniGamePlaying[0] = true;
                    this.miniGameType = MiniGamesTypes.SPACE_INVADERS;
                }
                drawSpaceInvadersMiniGameMenu(randomEvents[randomEvent].cardMessage);
            }
            else if (randomEvents[randomEvent].miniGameType == MiniGamesTypes.MATH) {
                if (!this.miniGamePlaying[1])
                {
                    this.miniGamePlaying[1] = true;
                    this.miniGameType = MiniGamesTypes.MATH;
                }
                drawMathMiniGameMenu(randomEvents[randomEvent].cardMessage);
            }
            else if (randomEvents[randomEvent].miniGameType == MiniGamesTypes.MEMORY) {
                if (!this.miniGamePlaying[2])
                {
                    this.miniGamePlaying[2] = true;
                    this.miniGameType = MiniGamesTypes.MEMORY;
                }
                drawMemoryMiniGameMenu(randomEvents[randomEvent].cardMessage);
            }
            else if (randomEvents[randomEvent].miniGameType == MiniGamesTypes.NONE) {
                drawCardAnim(randomEvents[randomEvent].cardMessage);
                player.additionalMovement = randomEvents[randomEvent].nextRoundMovement;
                player.pausingRounds = randomEvents[randomEvent].roundsMissed;
                if (randomEvents[randomEvent].pawnToBase ) {
                    pawn.dead();
                    player.activePawn--;
                    player.pawnsInBase++;
                }
          }
        }
    }

    public void miniGameResultToRandomEvent(Player player, Pawn pawn) {
        if (this.miniGameResult == MiniGameOutput.BIG_WIN && this.miniGameOutput) {
            drawCardAnim(randomEvents[this.randType + RandomEvent.BIG_WIN_EVENTS].cardMessage);
            player.additionalMovement = randomEvents[this.randType + RandomEvent.BIG_WIN_EVENTS].nextRoundMovement;
        }
        else if (this.miniGameResult == MiniGameOutput.SMALL_WIN && this.miniGameOutput){
            drawCardAnim(randomEvents[this.randType + RandomEvent.SMALL_WIN_EVENTS].cardMessage);
            player.additionalMovement = randomEvents[this.randType + RandomEvent.SMALL_WIN_EVENTS].nextRoundMovement;
        }
        else if (this.miniGameResult == MiniGameOutput.LOSE && this.miniGameOutput) {
            drawCardAnim(randomEvents[this.randType + RandomEvent.LOSE_EVENTS].cardMessage);
            player.pausingRounds = randomEvents[this.randType + RandomEvent.LOSE_EVENTS].roundsMissed;
            if (randomEvents[this.randType + RandomEvent.LOSE_EVENTS].pawnToBase && !this.cardIsLoading) {
                pawn.dead();
                player.activePawn--;
                player.pawnsInBase++;
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

        gameTextures.yellowPlayerAtlas.dispose();
        gameTextures.pinkPlayerAtlas.dispose();
        gameTextures.bluePlayerAtlas.dispose();
        gameTextures.greenPlayerAtlas.dispose();

        gameTextures.diceAtlas.dispose();
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

        gameTextures.font.dispose();
        gameTextures.turnSignAtlas.dispose();

        gameTextures.turnSignBlueBackground.dispose();
        gameTextures.turnSignPinkBackground.dispose();
        gameTextures.turnSignGreenBackground.dispose();
        gameTextures.turnSignYellowBackground.dispose();

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
}

class Player {
    public int[] winsPosition = {0, 0, 0, 0};
    public int playerNumber;
    public int activePawn;
    public Animation<TextureRegion> moveAnimation;
    public int playerBase;
    public DayParkMap.ColorOfAllPlayers playerColor;
    public int numbersOfWinPawns;
    public boolean win;
    public int pawnsInBase;
    public int additionalMovement, pausingRounds;
    public ArrayList<Texture> baseOfPlayer = new ArrayList<>();

    public Pawn[] pawns = {new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase)};

    public Player(int playerNumber, int playerBase) {
        this.win = false;
        this.playerNumber = playerNumber;
        this.playerBase = playerBase;
        this.additionalMovement = 0;
        this.pausingRounds = 0;
        activePawn = 0;
        numbersOfWinPawns = 0;
        pawnsInBase = 4;
    }

    public void win(){
        for (int i = 3; i >= 0; i--){
            System.out.println(i + " " + pawns[i].position);
            checkWin(i);
        }
        for (int i = 0; i < 4; i++){
            checkWin(i);
        }
        System.out.println("wins " + numbersOfWinPawns);
    }

    private void checkWin(int i) {
        if(pawns[i].position == 53 && !pawns[i].win && winsPosition[0] == 0){
            enforceWin(i);
        }
        if(pawns[i].position == 52 && !pawns[i].win && winsPosition[0] == 1){
            enforceWin(i);
        }
        if(pawns[i].position == 51 && !pawns[i].win && winsPosition[0] == 1 && winsPosition[1] == 1){
            enforceWin(i);
        }
        if(pawns[i].position == 50 && !pawns[i].win && winsPosition[0] == 1 && winsPosition[1] == 1 && winsPosition[2] == 1){
            enforceWin(i);
        }
    }

    private void enforceWin(int i) {
        System.out.println("Win pawn " + i);
        numbersOfWinPawns++;
        winsPosition[i] = 1;
        pawns[i].win = true;
    }

}

class Pawn {
    public float playerElapsedTime;
    public int positionAtMap; // zaczyna się od bazy czyli np zielony ma 7 max 49
    public int position; // uzywane do okreslania czy gracz dotarl do bazy max 53
    boolean active;
    boolean win;
    boolean onTheBus;

    public Pawn(int position) {
        this.onTheBus = false;
        this.win = false;
        this.positionAtMap = -1;
        this.position = -1;
        this.active = false;
        this.playerElapsedTime = 0;
    }

    public void alive(int positionAtMap) {
        this.positionAtMap = positionAtMap;
        this.position = 0;
        this.active = true;
    }

    public void dead() {
        this.positionAtMap = -1;
        this.position = -1;
        this.active = false;
        this.playerElapsedTime = 0;
    }
}
