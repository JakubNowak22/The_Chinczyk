package com.thechinczyk.game.screens;

import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.thechinczyk.game.MyTheChinczyk;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class DayParkMap implements Screen {

    MyTheChinczyk game;
    Random rand = new Random();
    int randNumber = 0;
    int diceRoll = 0;

    /**
     * pola specjalne :
     * wykżynik:
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

    enum ColorOfAllPlayers {None,Yellow, Green, Blue, Pink}
    ColorOfAllPlayers playerToKillRightNow;
    ColorOfAllPlayers playerKiller;
    boolean startCaptureAnim;
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
        gameTextures = new GameTextures();
        for (int i = 0; i < game.playerCount; i++) {
            Player player = new Player(i, startPlayerBase[i]);
            Players.add(player);
            if(i==0){
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
                player.playerColor= ColorOfAllPlayers.Pink;
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

        drawDice();

        drawBases();

        //Wyświetlanie tabliczek w rogu z kolorem aktualnego gracza
        drawWhichPlayersTurnUI();

        drawPlayers(playerNumberTurn);
        managePlayer(playerNumberTurn);

        //Obsługa animacji zbijania pionków
        drawPawnCaptureAnim(playerToKillRightNow, playerKiller);

        //Wyświetlenie górnej warstwy tła planszy (drzewa, latarnie itd.)
        game.batch.draw(gameTextures.dayParkTopground, 0, 0, 1920, 1080);

        game.batch.end();
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
        if (!throwDice) {
            drawDiceAnim();
        } else if (player.activePawn == 0 && randNumber == 6) {
            addPawn(player);
        } else if (player.activePawn >= 1 && player.activePawn <= 4) {
            if (!pawnChoose) {
                canAnyoneMove(player);
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
        Map<Integer, Integer> map = new TreeMap<>();
        putPlayerToMap(player, map, 0);
        putPlayerToMap(player, map, 1);
        putPlayerToMap(player, map, 2);
        putPlayerToMap(player, map, 3);
        Integer []a = map.values().toArray(new Integer[0]);
        //System.out.println(map.values());
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
            System.out.println("pierwszy");
            enforcedPlayer(player, first);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) &&
                player.pawns[second].active && a.length >= 2) {
            System.out.println("drugi");
            enforcedPlayer(player, second);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) &&
                player.pawns[third].active && a.length >= 3) {
            System.out.println("trzeci");
            enforcedPlayer(player, third);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4) &&
                player.pawns[fourth].active && a.length >= 4) {
            System.out.println("czwarty");
            enforcedPlayer(player, fourth);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N) && randNumber == 6) {
            if (canIAddPawn(player.playerBase)) {
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
            System.out.println("enforcedPlayer");
            manageParticularPawn(player, x);
            if(player.pawns[x].position >= 50){
                player.win();
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
            if(canIAddPawn(player.playerBase) && randNumber == 6){
                addPawn(player);
            }else{
                setPlayerNumberTurn();
            }
            resetFlags();
        }
    }

    private boolean canIAddPawn(int playerBase) {
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
        if(player.pawns[x].position + randNumber > 53 - player.numbersOfWinPawns){
            return false;
        }
        for (Pawn pawn : player.pawns) {
            if (pawn != player.pawns[x] && pawn.active) {
                if (pawn.position  == player.pawns[x].position  + randNumber) {
                    return false;
                }
            }
        }
        return true;
    }

    private void manageParticularPawn(Player player, int x) {
        player.pawns[x].positionAtMap = (player.pawns[x].positionAtMap + randNumber) % 49;
        player.pawns[x].position += randNumber;
        pawToChange = x;
        pawnChoose = true;
    }

    private void addPawn(Player player) {
        if (player.activePawn <= 4) {
            for (int i = 0; i < 4; i++) {
                if (!player.pawns[i].active) {
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
        randNumber = 0;
    }

    void drawPlayerPawns(Player player) {
        for (int i = 0; i < 4; i++) {
            if (player.pawns[i].active || player.pawns[i].win) {
                drawPawn(player, i);
            }
        }
    }

    private void drawPawn(Player player, int pawnNumber) {
        if(pawnNumber != -1) {
            if(player.moveAnimation==gameTextures.yellowPlayerAnim){
                if (player.pawns[pawnNumber].playerElapsedTime < 8.03f || player.pawns[pawnNumber].playerElapsedTime > 16.35f) {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                } else {
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
            } else if (player.moveAnimation==gameTextures.greenPlayerAnim) {
                if(player.pawns[pawnNumber].playerElapsedTime < 5.68f || player.pawns[pawnNumber].playerElapsedTime > 14.01f){
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                }
                else{
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
            }else if(player.moveAnimation==gameTextures.bluePlayerAnim){
                if(player.pawns[pawnNumber].playerElapsedTime < 8.35f){
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            840, 0, 1080, 1080);
                }
                else{
                    game.batch.draw(player.moveAnimation.getKeyFrame(player.pawns[pawnNumber].playerElapsedTime, false),
                            0, 0, 1080, 1080);
                }
            }else if(player.moveAnimation==gameTextures.pinkPlayerAnim){
                if(player.pawns[pawnNumber].playerElapsedTime < 6.68f || player.pawns[pawnNumber].playerElapsedTime > 14.69f){
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

    public void setPlayerNumberTurn() {
        if (playerNumberTurn < game.playerCount - 1) {
            playerNumberTurn++;
        } else {
            playerNumberTurn = 0;
        }
        //włączenie animacji zmiany tury graczy
        drawWhichPlayersTurnAnim();
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

    public void drawDiceAnim() {// losuje liczbe oraz wyświetla animację losowania
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && !gameTextures.diceAnimStarted) {
            gameTextures.diceAnimStarted = true;
        }
        if (gameTextures.diceAnimStarted) {
            /*if (!gameTextures.diceAnim.isAnimationFinished(gameTextures.diceElapsedTime)) {
                gameTextures.diceElapsedTime += Gdx.graphics.getDeltaTime();
                game.batch.draw(gameTextures.diceAnim.getKeyFrame(gameTextures.diceElapsedTime, false), 300, 0, 1000, 850);
            } else {
                gameTextures.diceAnimStarted = false;
                gameTextures.diceElapsedTime = 0;
                throwDice = true;
            }
            if(gameTextures.diceAnim.getKeyFrameIndex(gameTextures.diceElapsedTime) == 55){*/
            randNumber = rand.nextInt(6) + 1;
            diceRoll = randNumber;
            //System.out.println(randNumber);
            gameTextures.diceAnimStarted = false;
            gameTextures.diceElapsedTime = 0;
            throwDice = true;
            //}
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

            //Ustawianie odpowiedniej klatki animacji w zależności od koloru zbijanego pionka
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
    }
}

class Player {
    public int [] winsPosition = {0, 0, 0, 0};
    public int playerNumber;
    public int activePawn;
    public Animation<TextureRegion> moveAnimation;
    public int playerBase;
    public DayParkMap.ColorOfAllPlayers playerColor;
    public int numbersOfWinPawns;
    public int pawnsInBase;
    public ArrayList<Texture> baseOfPlayer = new ArrayList<>();
    public Pawn[] pawns = {new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase),
            new Pawn(playerBase)};

    public Player(int playerNumber, int playerBase) {
        this.playerNumber = playerNumber;
        this.playerBase = playerBase;
        activePawn = 0;
        numbersOfWinPawns = 0;
        pawnsInBase = 4;
    }

    public void win(){
        for (int i = 3; i >= 0; i--){
            System.out.println(i + " " + pawns[i].position);
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
        System.out.println("wins " + numbersOfWinPawns);
    }

    private void enforceWin(int i) {
        numbersOfWinPawns ++;
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

    public Pawn(int position) {
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

