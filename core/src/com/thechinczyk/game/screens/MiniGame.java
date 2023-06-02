package com.thechinczyk.game.screens;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.thechinczyk.game.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.thechinczyk.game.MyTheChinczyk;

import java.util.Random;
import static com.thechinczyk.game.screens.MainMenu.spriteInit;

abstract class MiniGameMainMenu {
    SpriteBatch spriteBatch;
    DayParkMap map;
    MiniGameGameplay game;
    MiniGameEndMenu endMenu;

    Texture menuTexture;

    GameObject buttonStart;
    Texture buttonStartHovered;
    Sprite buttonStartHoveredSprite;
    boolean isButtonStartHovered;

    GameObject buttonHTP;
    Texture buttonHTPHovered;
    Sprite buttonHTPHoveredSprite;
    boolean isButtonHTPHovered;

    boolean isStarted;
    boolean isEnding;
    boolean instructionDisplay;
    boolean isInstructionJustDisplayed;

    MiniGameMainMenu(SpriteBatch batch, DayParkMap map) {
        this.spriteBatch = batch;
        this.isButtonStartHovered = false;
        this.isButtonHTPHovered = false;
        this.isStarted = false;
        this.isEnding = false;
        this.map = map;
    }
    public void resetAfterQuit(MiniGamesTypes type) {
        this.isButtonStartHovered = false;
        this.isButtonHTPHovered = false;
        this.isEnding = false;
        this.map.unlockMap(type);
        this.isStarted = false;
    }

}
enum MiniGamesTypes {NONE, SPACE_INVADERS, MATH, MEMORY}

abstract class MiniGameGameplay {
    BitmapFont fontGreen, fontYellow;
    static Random random = new Random();
    SpriteBatch spriteBatch;
    Texture gameTexture;
    MiniGameMainMenu menu;
    int timeSeconds;
    float timer;

    MiniGameGameplay (SpriteBatch spriteBatch, MiniGameMainMenu menu, int miniGameTime) {
        this.fontGreen = new BitmapFont(Gdx.files.internal("Fonts/GreenBerlinSans.fnt"),false);
        this.fontGreen.getData().setScale(.75f,.75f);
        this.fontYellow = new BitmapFont(Gdx.files.internal("Fonts/YellowBerlinSans.fnt"),false);
        this.fontYellow.getData().setScale(.66f,.66f);
        this.spriteBatch = spriteBatch;
        this.menu = menu;
        this.timer = 0;
        this.timeSeconds = miniGameTime;
    }

    abstract public void Draw();

    public boolean timerUpdate(float deltatime) {
        timer += deltatime;
        if(timer > 1){
            timer-=1;
            timeSeconds --;
            return true;
        }
        return false;
    }
}

abstract class MiniGameEndMenu {
    GameObject buttonEnd;
    Texture buttonEndHovered;
    Sprite buttonEndHoveredSprite;
    boolean isButtonEndHovered;

    MiniGameGameplay game;
    MiniGameMainMenu menu;
    SpriteBatch spriteBatch;

    Texture menuTexture;

    abstract public void Draw();

    MiniGameEndMenu (SpriteBatch batch, MiniGameMainMenu menu, MiniGameGameplay game) {
        this.spriteBatch = batch;
        this.menu = menu;
        this.game = game;
    }
}

public class MiniGame {
    public boolean[] isLoaded;
    public SpaceInvadersMenu menuSpaceInvaders;
    public MathMiniGameMenu menuMath;
    public MemoryMiniGameMenu menuMemory;
    private static Sprite cardSprite;
    static SpriteBatch spriteBatch;
    MiniGamesTypes type;

    public MiniGame(SpriteBatch spriteBatchParam, DayParkMap map) {
        this.isLoaded = new boolean[3];
        spriteBatch = spriteBatchParam;
        this.menuSpaceInvaders = new SpaceInvadersMenu(spriteBatch, map);
        this.menuMath = new MathMiniGameMenu(spriteBatch, map);
        this.menuMemory = new MemoryMiniGameMenu(spriteBatch, map);
    }

    public static Vector2 getMiniGameMousePosition(MyTheChinczyk game) {
        Vector3 cursorPosition3 = game.viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        return new Vector2(cursorPosition3.x, cursorPosition3.y);
    }

    public void loadTextures(MiniGamesTypes type) {
        this.type = type;
        if (type == MiniGamesTypes.SPACE_INVADERS)
            menuSpaceInvaders.loadTextures();
        else if (type == MiniGamesTypes.MATH)
            menuMath.loadTextures();
        else if (type == MiniGamesTypes.MEMORY)
            menuMemory.loadTextures();
        Texture cardTexture = new Texture("Map1/CardAnimSheet/CardAnimSheet46.png");
        cardSprite = spriteInit(cardTexture, 435, -115, 1024,1024);
    }

    public static void displayInstructionHTP(String instruction, DayParkMap map, float scale) {
        cardSprite.draw(spriteBatch);
        map.gameTextures.font.getData().setScale(scale, scale);
        map.gameTextures.font.draw(spriteBatch, instruction, 660, 710, 600, Align.center, true);
        map.gameTextures.font.getData().setScale(0.3f, 0.3f);
    }

    public static void MiniGameMainMenuUpdate(MiniGameMainMenu mainMenu, String instruction, MiniGamesTypes type) {
        Vector2 cursorPosition = getMiniGameMousePosition(mainMenu.map.game);
        if (!mainMenu.isStarted) {
            mainMenu.isButtonStartHovered = (mainMenu.buttonStart.contains(cursorPosition) && !mainMenu.isButtonHTPHovered);
            mainMenu.isButtonHTPHovered = mainMenu.buttonHTP.contains(cursorPosition);

            if (mainMenu.isButtonHTPHovered) {
                mainMenu.buttonHTPHoveredSprite.draw(spriteBatch);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    mainMenu.instructionDisplay = true;
                    mainMenu.isInstructionJustDisplayed = true;
                }

            } else if (mainMenu.isButtonStartHovered && !mainMenu.instructionDisplay) {
                mainMenu.buttonStartHoveredSprite.draw(spriteBatch);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (type == MiniGamesTypes.SPACE_INVADERS)
                        mainMenu.game = new SpaceInvaders(spriteBatch, mainMenu);
                    else if (type == MiniGamesTypes.MATH)
                        mainMenu.game = new MathMiniGame(spriteBatch, mainMenu);
                    else
                        mainMenu.game = new MemoryMiniGame(spriteBatch, mainMenu);
                    mainMenu.map.gameTextures.timerElapsedTime = 0f;
                    mainMenu.isStarted = true;
                }
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && mainMenu.instructionDisplay && !mainMenu.isInstructionJustDisplayed) {
                mainMenu.instructionDisplay = false;
                mainMenu.isButtonHTPHovered = false;
            }
            if (mainMenu.instructionDisplay) {
                MiniGame.displayInstructionHTP(instruction, mainMenu.map, 0.25f);
                mainMenu.instructionDisplay = true;
                mainMenu.isInstructionJustDisplayed = false;
            }

        }
        if (mainMenu.isStarted) {
            mainMenu.game.Draw();
        }
    }

    public static void MiniGameEndMenuUpdate (MiniGameMainMenu mainMenu, MiniGameEndMenu endMenu, int outputNumber, MiniGamesTypes type) {
        Vector2 cursorPosition = getMiniGameMousePosition(mainMenu.map.game);
        endMenu.isButtonEndHovered = endMenu.buttonEnd.contains(cursorPosition);
        if (endMenu.isButtonEndHovered) {
            endMenu.buttonEndHoveredSprite.draw(spriteBatch);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                mainMenu.map.miniGameOutput[outputNumber] = true;
                mainMenu.resetAfterQuit(type);
            }
        }
    }

    static class SpaceInvadersMenu extends MiniGameMainMenu {
        private Texture backTexture;

        SpaceInvadersMenu(SpriteBatch batch, DayParkMap map) {
            super(batch, map);
        }

        public void loadTextures() {
            this.menuTexture = new Texture("SpaceInvadersMiniGame/MainMenuBackground.png");
            this.backTexture = new Texture("SpaceInvadersMiniGame/MiniGame_Edge.png");

            buttonStartHovered = new Texture("SpaceInvadersMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, 860, 398, 200, 100);
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, 860, 398, 200, 100);

            buttonHTPHovered = new Texture("SpaceInvadersMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, 860, 285, 200, 100);
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, 860, 285, 200, 100);
        }

        public void Update() {
            MiniGameMainMenuUpdate(this, "In this mini-game, you control rocket. You move horizontal with " +
                    "LEFT and RIGHT ARROW. Your target is to make moving UFOs, which " +
                    "are your enemies, disappear, by shooting them with bullets. Use ARROW UP " +
                    "to shoot. You have 10 seconds to eliminate as many enemies you can.", MiniGamesTypes.SPACE_INVADERS);
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.backTexture, 430, 220, 1060, 640);
                spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
                Update();
            }
            else
                this.endMenu.Draw();
        }
    }

    static class SpaceInvadersEndMenu extends MiniGameEndMenu{
        int result;
        private Texture backTexture;

        SpaceInvadersEndMenu (SpriteBatch batch, int result, MiniGameMainMenu menu, SpaceInvaders game) {
            super(batch, menu, game);
            this.result = result;
            this.loadTextures();
        }

        public void loadTextures() {
            this.backTexture = new Texture("SpaceInvadersMiniGame/MiniGame_Edge.png");
            this.menuTexture = new Texture("SpaceInvadersMiniGame/EndMenuBackground.png");

            buttonEndHovered = new Texture("SpaceInvadersMiniGame/ButtonEXIT_on.png");
            buttonEnd = new GameObject(buttonEndHovered, 860, 285, 200, 100);
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, 860, 285 , 200, 100);
        }

        public void Update() {
            MiniGameEndMenuUpdate(menu, this, 0, MiniGamesTypes.SPACE_INVADERS);
        }

        public void Draw() {
            spriteBatch.draw(this.backTexture, 430, 220, 1060, 640);
            spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
            if (this.result < 10)
                this.game.fontGreen.draw(this.spriteBatch, Integer.toString(this.result), 940, 450);
            else
                this.game.fontGreen.draw(this.spriteBatch, Integer.toString(this.result), 930, 450);
            Update();
        }
    }

    static class SpaceInvaders extends MiniGameGameplay{

        static class Player {
            public static final int MAX_BULLETS = 3;
            private float position;
            private final float[] bulletPosition;
            public Sprite spritePlayer;
            public Sprite[] spriteBullet;
            private final SpriteBatch spriteBatch;
            public final float Y_POS = 270;
            private final Vector2 BULLET_SIZE = new Vector2(10, 20);
            private final int ROCKET_SIZE = 100;
            private final boolean[] showBullet;
            private final float[] positionWhenShoot;
            private int nextBullet;
            private int lastShoot;

            public Player(SpriteBatch batch) {
                this.spriteBatch = batch;
                this.spritePlayer = new Sprite(new Texture("SpaceInvadersMiniGame/PlayerRocket.png"));
                this.spritePlayer.setSize(ROCKET_SIZE, ROCKET_SIZE);
                this.spriteBullet = new Sprite[MAX_BULLETS];
                this.bulletPosition = new float[MAX_BULLETS];
                this.showBullet = new boolean[MAX_BULLETS];
                this.positionWhenShoot = new float[MAX_BULLETS];
                for (int i = 0; i < MAX_BULLETS; i++) {
                    this.spriteBullet[i] = new Sprite(new Texture("SpaceInvadersMiniGame/Bullet.png"));
                    this.spriteBullet[i].setSize(BULLET_SIZE.x, BULLET_SIZE.y);
                    this.spriteBullet[i].rotate90(true);
                    this.bulletPosition[i] = Y_POS;
                    this.showBullet[i] = false;
                }
                this.position = 910;
                this.nextBullet = 0;
            }

            public void Update(float deltatime) {
                float PLAYER_MOVEMENT_OFFSET = 300;
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    this.position -= deltatime * PLAYER_MOVEMENT_OFFSET;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    this.position += deltatime * PLAYER_MOVEMENT_OFFSET;

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && this.nextBullet < 3) {
                    this.positionWhenShoot[this.nextBullet] = this.position + ROCKET_SIZE/2f - BULLET_SIZE.x/2f;
                    this.bulletPosition[this.nextBullet] = Y_POS;
                    this.showBullet[this.nextBullet] = true;
                    this.lastShoot = this.nextBullet;
                    this.nextBullet = 3;
                }

                float MAX_X_POS = 1340;
                float MIN_X_POS = 480;
                if (this.position > MAX_X_POS)
                    this.position = MAX_X_POS;
                else if (this.position < MIN_X_POS)
                    this.position = MIN_X_POS;

                for (int i = 0; i<3; i++) {
                    if (this.showBullet[i]) {
                        float BULLET_MOVEMENT_OFFSET = 500;
                        this.bulletPosition[i] += deltatime * BULLET_MOVEMENT_OFFSET;
                        if (this.nextBullet == 3 && this.lastShoot == i && this.bulletPosition[i] >= 462)
                            this.nextBullet = i==2 ? 0 : (i+1);


                        float MAX_Y_POS = 810;
                        if (this.bulletPosition[i] >= MAX_Y_POS)
                            this.showBullet[i] = false;
                        spriteBullet[i].setPosition(this.positionWhenShoot[i], this.bulletPosition[i]);
                    }
                }
                spritePlayer.setPosition(this.position, Y_POS);
            }

            public void Draw() {
                Update(Gdx.graphics.getDeltaTime());
                spritePlayer.draw(this.spriteBatch);
                for (int i = 0; i<3; i++) {
                    if (this.showBullet[i])
                        spriteBullet[i].draw(this.spriteBatch);
                }
            }
        }

        static class Enemy {
            private final Vector2 position;
            public Sprite spriteEnemy;
            private final SpriteBatch spriteBatch;
            private final int ENEMY_SIZE = 60;
            private final float ENEMY_POSITION_OFFSET = 10;
            public final float MIN_Y_POS = 540 - ENEMY_SIZE + ENEMY_POSITION_OFFSET;
            private boolean toDraw;
            private static boolean leftDir, dirToChange;

            public Enemy(SpriteBatch batch, int rowNumber, int columnNumber, boolean toDraw) {
                this.spriteBatch = batch;
                this.spriteEnemy = new Sprite(new Texture("SpaceInvadersMiniGame/Enemy.png"));
                this.spriteEnemy.setSize((487/321f)*ENEMY_SIZE,ENEMY_SIZE);
                float MIN_X_POS = 480 + 2.1f * ENEMY_SIZE;
                this.position = new Vector2(MIN_X_POS + ((487/321f)*ENEMY_SIZE + ENEMY_POSITION_OFFSET)*columnNumber, MIN_Y_POS + (ENEMY_SIZE + ENEMY_POSITION_OFFSET)*rowNumber);
                this.toDraw = toDraw;
                spriteEnemy.setPosition(this.position.x, this.position.y);
            }

            public void Update (float deltatime) {
                float ENEMY_MOVEMENT_SPEED = 100;
                if (Enemy.leftDir)
                    this.position.x -= deltatime * ENEMY_MOVEMENT_SPEED;
                else
                    this.position.x += deltatime * ENEMY_MOVEMENT_SPEED;

                spriteEnemy.setPosition(this.position.x, this.position.y);

                if (this.position.x >= 1440 - (487/321f)*ENEMY_SIZE || this.position.x <= 480)
                    Enemy.dirToChange = true;
            }

            public void Draw () {
                if (this.toDraw) {
                    Update(Gdx.graphics.getDeltaTime());
                    spriteEnemy.draw(this.spriteBatch);
                }
            }
        }

        Player rocket;
        Enemy[][] enemiesArray;
        private int eliminatedEnemies;

        SpaceInvaders(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, 10);
            this.rocket = new Player(this.spriteBatch);
            this.gameTexture = new Texture("SpaceInvadersMiniGame/Background.jpg");
            this.eliminatedEnemies = 0;
            this.menu.map.gameTextures.timerElapsedTime = 0f;
            this.enemiesArray = new Enemy[4][7];
            int enemiesLeft = 14, fieldsLeft = 28;
            for (int i = 0; i<4; i++) {
                for (int j = 0; j < 7; j++) {
                    if (random.nextInt(fieldsLeft) < enemiesLeft) {
                        enemiesArray[i][j] = new Enemy(this.spriteBatch, i, j, true);
                        enemiesLeft--;
                    } else
                        enemiesArray[i][j] = new Enemy(this.spriteBatch, i, j, false);

                    fieldsLeft--;
                }
            }
        }

        public void Draw() {
            spriteBatch.draw(this.gameTexture, 480, 270, 960, 540);
            this.Update(Gdx.graphics.getDeltaTime());
            fontGreen.draw(this.spriteBatch, Integer.toString(this.eliminatedEnemies), 500, 780);
            if (this.timeSeconds > 9)
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1344, 767);
            else
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1354, 767);
            this.menu.map.drawMiniGameTimer(1320, 690);
        }

        public void Update(float deltatime) {
            this.rocket.Draw();
            for (int i = 0; i<4; i++) {
                for (int j = 0; j<7; j++) {
                    for (int k = 0; k<3; k++) {
                        if ((this.rocket.spriteBullet[k].getBoundingRectangle().overlaps(this.enemiesArray[i][j].spriteEnemy.getBoundingRectangle()) && this.enemiesArray[i][j].toDraw && this.rocket.showBullet[k])) {
                            this.enemiesArray[i][j].toDraw = false;
                            this.rocket.showBullet[k] = false;
                            this.eliminatedEnemies++;
                        }
                    }
                    this.enemiesArray[i][j].Draw();
                }
            }

            if(random.nextInt(50) == 0 || Enemy.dirToChange) {
                Enemy.leftDir = !Enemy.leftDir;
                Enemy.dirToChange = false;
            }
            timerUpdate(deltatime);

            if (this.eliminatedEnemies == 14 || this.timeSeconds == 0) {
                this.menu.isStarted = false;
                this.menu.endMenu = new SpaceInvadersEndMenu(this.spriteBatch, this.eliminatedEnemies, this.menu, this);
                this.menu.isEnding = true;
            }
        }
    }

    static class MathMiniGameMenu extends MiniGameMainMenu{
        MathMiniGameMenu(SpriteBatch batch, DayParkMap map) {
            super(batch, map);
        }

        public void loadTextures() {
            this.menuTexture = new Texture("MathMiniGame/MainMenuBackground.png");

            buttonStartHovered = new Texture("MathMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, 860, 412, 200, 100);
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, 860, 414, 200, 100);

            buttonHTPHovered = new Texture("MathMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, 860, 307, 200, 100);
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, 860, 307, 200, 100);
        }

        public void Update() {
            MiniGameMainMenuUpdate(this,"In this mini-game, you get random mathematical operation. You " +
                    "have to type exact result, unless you get division - " +
                    "it is integer division (e.g 5/2 = 2). " +
                    "You have 15 seconds to solve it, but if you finish earlier, " +
                    "you can use ENTER to end mini-game. If the answer is correct and " +
                    "you finished under 10 seconds, your reward will be better.", MiniGamesTypes.MATH);
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
                Update();
            }
            else
               this.endMenu.Draw();
        }
    }

    static class MathMiniGame extends MiniGameGameplay{

        int number1, number2, operation;
        int[] number1Array, number2Array;
        int expectedResult, playerResult;
        int numberCounter;

        private Texture addSign, subSign, mulSign, divSign, eqSign;
        private final Texture[] numbers;
        private Sprite signSprite, eqSignSprite;
        private final Sprite[] number1Sprite, number2Sprite, resultSprite;
        private final int NUMBER_SIZE = 100;


        MathMiniGame(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, 15);
            this.numberCounter = 0;
            this.operation = random.nextInt(4);
            this.number1 = 100 + random.nextInt(1000);
            if (this.operation == 0) {
                this.number2 = 100 + random.nextInt(1000);
                this.expectedResult = this.number1 + this.number2;
            }
            else if (this.operation == 1) {
                this.number2 = 1 + random.nextInt(this.number1-2);
                this.expectedResult = this.number1 - this.number2;
            }
            else if (this.operation == 2) {
                this.number2 = 2 + random.nextInt(20);
                this.expectedResult = this.number1 * this.number2;
            }
            else {
                this.number2 = 2 + random.nextInt(20);
                this.expectedResult = this.number1 / this.number2;
            }

            this.number1Array = this.intToArray(this.number1);
            this.number2Array = this.intToArray(this.number2);
            this.number1Sprite = new Sprite[4];
            this.number2Sprite = new Sprite[4];
            this.resultSprite = new Sprite[5];
            this.numbers = new Texture[10];
            this.loadTextures();
        }

        public void loadTextures() {
            this.gameTexture = new Texture("MathMiniGame/Background.png");
            this.eqSign = new Texture("MathMiniGame/EqualSign.png");
            this.addSign = new Texture("MathMiniGame/AddSign.png");
            this.subSign = new Texture("MathMiniGame/SubtractSign.png");
            this.mulSign = new Texture("MathMiniGame/MultiplySign.png");
            this.divSign = new Texture("MathMiniGame/DivideSign.png");
            this.numbers[0] = new Texture("MathMiniGame/Zero.png");
            this.numbers[1] = new Texture("MathMiniGame/One.png");
            this.numbers[2] = new Texture("MathMiniGame/Two.png");
            this.numbers[3] = new Texture("MathMiniGame/Three.png");
            this.numbers[4] = new Texture("MathMiniGame/Four.png");
            this.numbers[5] = new Texture("MathMiniGame/Five.png");
            this.numbers[6] = new Texture("MathMiniGame/Six.png");
            this.numbers[7] = new Texture("MathMiniGame/Seven.png");
            this.numbers[8] = new Texture("MathMiniGame/Eight.png");
            this.numbers[9] = new Texture("MathMiniGame/Nine.png");
            this.texturesToSprites();
        }

        public void texturesToSprites() {
            int SIZE = 200;
            if (this.operation == 0)
                this.signSprite = spriteInit(this.addSign, 960 - SIZE / 2f, 640 - SIZE / 2f, SIZE, SIZE);
            else if (this.operation == 1)
                this.signSprite = spriteInit(this.subSign, 960 - SIZE / 2f, 640 - SIZE / 2f, SIZE, SIZE);
            else if (this.operation == 2)
                this.signSprite = spriteInit(this.mulSign, 960 - SIZE / 2f, 640 - SIZE / 2f, SIZE, SIZE);
            else
                this.signSprite = spriteInit(this.divSign, 960 - SIZE / 2f, 640 - SIZE / 2f, SIZE, SIZE);

            this.eqSignSprite = spriteInit(this.eqSign, 960 - SIZE / 2f, 540 - SIZE / 2f, SIZE, SIZE);

            for (int i = 0; i < 4; i++) {
                this.number1Sprite[i] = spriteInit(this.numbers[number1Array[i]], 960 - (4 - i) * (NUMBER_SIZE / 2f + 10) - 50, 700 - SIZE / 2f, NUMBER_SIZE / 2f, NUMBER_SIZE);
                this.number2Sprite[i] = spriteInit(this.numbers[number2Array[i]], 960 + i * (NUMBER_SIZE / 2f + 10) + 50, 700 - SIZE / 2f, NUMBER_SIZE / 2f, NUMBER_SIZE);
            }
        }

        public void refreshResultSprites() {
            for (int i = 0; i<this.numberCounter; i++) {
                this.resultSprite[i] = spriteInit(this.numbers[(this.playerResult/(i==0 ? 1 : (int)Math.pow(10,i)))%10], 960 + (2 - i) * (NUMBER_SIZE / 2f + 10) - NUMBER_SIZE/4f, 380, NUMBER_SIZE / 2f, NUMBER_SIZE);
            }
        }

        public int[] intToArray(int number) {
            int[] array = new int[4];
            array[3] = number%10;
            array[2] = (number/10)%10;
            array[1] = (number/100)%10;
            array[0] = number/1000;
            return array;
        }

        public void Draw() {
            spriteBatch.draw(this.gameTexture, 480, 270, 960, 540);
            this.Update(Gdx.graphics.getDeltaTime());
            if (this.timeSeconds > 9)
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1314, 737);
            else
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1324, 737);
            this.menu.map.drawMiniGameTimer(1290, 660);
            this.eqSignSprite.draw(this.spriteBatch);
            this.signSprite.draw(this.spriteBatch);
            for (int i = 0; i<4; i++) {
                if (this.number1/Math.pow(10, 3-i) >= 1)
                    this.number1Sprite[i].draw(this.spriteBatch);
                if (this.number2/Math.pow(10, 3-i) >= 1)
                    this.number2Sprite[i].draw(this.spriteBatch);
            }
            for (int i = 0; i<this.numberCounter; i++)
                this.resultSprite[i].draw(this.spriteBatch);
        }

        public void Update(float deltatime) {
            timerUpdate(deltatime);
            if (numberCounter < 5) {
                if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1))) {
                    this.playerResult *= 10;
                    this.playerResult += 1;
                    this.numberCounter++;
                } else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2))) {
                    this.playerResult *= 10;
                    this.playerResult += 2;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3))) {
                    this.playerResult *= 10;
                    this.playerResult += 3;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4))) {
                    this.playerResult *= 10;
                    this.playerResult += 4;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5))) {
                    this.playerResult *= 10;
                    this.playerResult += 5;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6))) {
                    this.playerResult *= 10;
                    this.playerResult += 6;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7))) {
                    this.playerResult *= 10;
                    this.playerResult += 7;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8))) {
                    this.playerResult *= 10;
                    this.playerResult += 8;
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9))) {
                    this.playerResult *= 10;
                    this.playerResult += 9;
                    this.numberCounter++;
                }
                else if (((Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0))) && this.numberCounter >0) {
                    this.numberCounter++;
                    this.playerResult *= 10;
                }
            }
            if ((Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) && numberCounter != 0) {
                    this.playerResult /= 10;
                    this.numberCounter--;
            }
            refreshResultSprites();

            if (this.timeSeconds == 0  || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                this.menu.isStarted = false;
                this.menu.endMenu = new MathMiniGameEndMenu(this.spriteBatch, this.expectedResult == this.playerResult, this.timeSeconds,  this.menu ,this);
                this.menu.isEnding = true;
            }

        }
    }

    static class MathMiniGameEndMenu extends MiniGameEndMenu {
        boolean result;
        int timer;

        MathMiniGameEndMenu (SpriteBatch batch, boolean result, int timer, MiniGameMainMenu menu, MathMiniGame game) {
            super(batch, menu, game);
            this.result = result;
            this.timer = timer;
            this.loadTextures();
        }

        public void loadTextures() {
            if (this.result)
                this.menuTexture = new Texture("MathMiniGame/BackgroundEndGameRight.png");
            else
                this.menuTexture = new Texture("MathMiniGame/BackgroundEndGameWrong.png");

            buttonEndHovered = new Texture("MathMiniGame/ButtonEXIT_on.png");
            buttonEnd = new GameObject(buttonEndHovered, 860, 308, 200, 100);
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, 860, 308 , 200, 100);
        }

        public void Update() {
            MiniGameEndMenuUpdate(menu, this, 1, MiniGamesTypes.MATH);
        }

        public void Draw() {
            spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
            Update();
        }
    }

    static class MemoryMiniGameMenu extends MiniGameMainMenu{
        MemoryMiniGameMenu(SpriteBatch batch, DayParkMap map) {
            super(batch, map);
        }

        public void loadTextures() {
            this.menuTexture = new Texture("MemoryMiniGame/MainMenuBackground.png");

            buttonStartHovered = new Texture("MemoryMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, 860, 398, 200, 100);
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, 860, 398, 200, 100);

            buttonHTPHovered = new Texture("MemoryMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, 860, 290, 200, 100);
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, 860, 290, 200, 100);
        }

        public void Update() {
            MiniGameMainMenuUpdate(this, "In this mini-game, you will be shown path of four colors. You have " +
                    "seconds to remember it, and then, you have 10 seconds to repeat " +
                    "this path by using NUMBER KEYS and BACKSPACE. If the answer is " +
                    "correct and you finished in under 5 seconds, " +
                    "your reward will be better.", MiniGamesTypes.MEMORY);
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
                Update();
            }
            else
                this.endMenu.Draw();
        }
    }

    static class MemoryMiniGame extends MiniGameGameplay{
        Texture gameTexture1, gameTexture2;
        int[] generatedNumbers, playerNumbers;
        int numberCounter;

        private final Texture[] colors;
        private Texture emptyColor;
        private final Sprite[] gameColors;
        private final int SIZE = 150;
        private final int OFFSET = 30;

        MemoryMiniGame(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, 15);
            this.numberCounter = 0;
            this.generatedNumbers = new int[4];
            this.playerNumbers = new int[4];
            for (int i = 0; i<4; i++)
                this.generatedNumbers[i] = random.nextInt(8);

            this.gameColors = new Sprite[4];
            this.colors = new Texture[9];
            this.loadTextures();
        }

        public void loadTextures() {
            this.gameTexture1 = new Texture("MemoryMiniGame/Background1.png");
            this.gameTexture2 = new Texture("MemoryMiniGame/Background2.png");
            this.emptyColor = new Texture("MemoryMiniGame/ColorEmpty.png");
            this.colors[0] = new Texture("MemoryMiniGame/ColorBlue.png");
            this.colors[1] = new Texture("MemoryMiniGame/ColorBrown.png");
            this.colors[2] = new Texture("MemoryMiniGame/ColorCyan.png");
            this.colors[3] = new Texture("MemoryMiniGame/ColorGreen.png");
            this.colors[4] = new Texture("MemoryMiniGame/ColorPink.png");
            this.colors[5] = new Texture("MemoryMiniGame/ColorPurple.png");
            this.colors[6] = new Texture("MemoryMiniGame/ColorRed.png");
            this.colors[7] = new Texture("MemoryMiniGame/ColorWhite.png");
            this.colors[8] = new Texture("MemoryMiniGame/ColorYellow.png");
            this.texturesToSprites();
        }

        public void texturesToSprites() {
            for (int i = 0; i < 4; i++) {
                this.gameColors[i] = spriteInit(this.colors[this.generatedNumbers[i]], 960 - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3*SIZE/2f, SIZE, SIZE);
            }
        }

         public void refreshResultSprite(int color) {
            if (color == -1)
                this.gameColors[this.numberCounter] = spriteInit(this.emptyColor, 960 - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3*SIZE/2f, SIZE, SIZE);
            else
                this.gameColors[this.numberCounter] = spriteInit(this.colors[color], 960 - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3*SIZE/2f, SIZE, SIZE);

             this.playerNumbers[this.numberCounter] = color;
        }

        public void Draw() {
            if (timeSeconds > 10)
                spriteBatch.draw(this.gameTexture1, 480, 270, 960, 540);
            else
                spriteBatch.draw(this.gameTexture2, 480, 270, 960, 540);

            this.Update(Gdx.graphics.getDeltaTime());
            if (this.timeSeconds > 9)
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1344, 767);
            else
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1354, 767);
            this.menu.map.drawMiniGameTimer(1320, 690);
            for (int i = 0; i < 4; i++) {
                this.gameColors[i].draw(this.spriteBatch);
            }
        }

        public void Update(float deltatime) {
            if (timerUpdate(deltatime) && timeSeconds == 10) {
                for (int i = 0; i<4; i++)
                    this.gameColors[i] = spriteInit(this.emptyColor, 960 - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3*SIZE/2f, SIZE, SIZE);
            }

            if (numberCounter < 4 && timeSeconds <= 10) {
                if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_1))) {
                    refreshResultSprite(0);
                    this.numberCounter++;
                } else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2))) {
                    refreshResultSprite(1);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_3))) {
                    refreshResultSprite(2);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4))) {
                    refreshResultSprite(3);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5))) {
                    refreshResultSprite(4);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6))) {
                    refreshResultSprite(5);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7))) {
                    refreshResultSprite(6);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8))) {
                    refreshResultSprite(7);
                    this.numberCounter++;
                }
                else if ((Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9))) {
                    refreshResultSprite(8);
                    this.numberCounter++;
                }
            }
            if (this.numberCounter > 0 && Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                this.numberCounter--;
                refreshResultSprite(-1);
            }

            if (this.timeSeconds == 0 || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                boolean check = true;
                for (int i = 0; i<4; i++) {
                    if (this.playerNumbers[i]!=this.generatedNumbers[i]) {
                        check = false;
                        break;
                    }
                }
                this.menu.isStarted = false;
                this.menu.endMenu = new MemoryMiniGameEndMenu(this.spriteBatch, check,  this.menu,this);
                this.menu.isEnding = true;
            }
        }
    }

    static class MemoryMiniGameEndMenu extends MiniGameEndMenu{
        boolean result;

        MemoryMiniGameEndMenu (SpriteBatch batch, boolean result, MiniGameMainMenu menu, MemoryMiniGame game) {
            super(batch, menu, game);
            this.result = result;
            this.loadTextures();
        }

        public void loadTextures() {
            if (this.result)
                this.menuTexture = new Texture("MemoryMiniGame/BackgroundEndGameRight.png");
            else
                this.menuTexture = new Texture("MemoryMiniGame/BackgroundEndGameWrong.png");

            buttonEndHovered = new Texture("MemoryMiniGame/ButtonEXITon.png");
            buttonEnd = new GameObject(buttonEndHovered, 860, 289, 200, 100);
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, 860, 289 , 200, 100);
        }

        public void Update() {
            MiniGameEndMenuUpdate(menu, this, 2, MiniGamesTypes.MEMORY);
        }

        public void Draw() {
            spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
            Update();
        }
    }
}
