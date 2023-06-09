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
    public void resetAfterQuit() {
        this.isButtonStartHovered = false;
        this.isButtonHTPHovered = false;
        this.isEnding = false;
        this.map.unlockMap();
        this.isStarted = false;
    }

}

abstract class MiniGameGameplay {
    public static final float YELLOW_FONT_SCALE = .66f;
    public static final float GREEN_FONT_SCALE = .75f;
    BitmapFont fontGreen, fontYellow;
    static Random random = new Random();
    SpriteBatch spriteBatch;
    Texture gameTexture;
    MiniGameMainMenu menu;
    int timeSeconds;
    float timer;

    MiniGameGameplay (SpriteBatch spriteBatch, MiniGameMainMenu menu, int miniGameTime) {
        this.fontGreen = new BitmapFont(Gdx.files.internal("Fonts/GreenBerlinSans.fnt"),false);
        this.fontGreen.getData().setScale(GREEN_FONT_SCALE, GREEN_FONT_SCALE);
        this.fontYellow = new BitmapFont(Gdx.files.internal("Fonts/YellowBerlinSans.fnt"),false);
        this.fontYellow.getData().setScale(YELLOW_FONT_SCALE, YELLOW_FONT_SCALE);
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
    public static final int MINI_GAMES_NUMBER = 3;
    public static final float DEFAULT_FONT_SCALE = 0.3f;
    public boolean[] isLoaded;
    public SpaceInvadersMenu menuSpaceInvaders;
    public MathMiniGameMenu menuMath;
    public MemoryMiniGameMenu menuMemory;
    private static Sprite cardSprite;
    static SpriteBatch spriteBatch;
    MiniGamesTypes type;

    public MiniGame(SpriteBatch spriteBatchParam, DayParkMap map) {
        this.isLoaded = new boolean[MINI_GAMES_NUMBER];
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
        map.gameTextures.font.getData().setScale(DEFAULT_FONT_SCALE, DEFAULT_FONT_SCALE);
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

    public static void MiniGameEndMenuUpdate (MiniGameMainMenu mainMenu, MiniGameEndMenu endMenu) {
        Vector2 cursorPosition = getMiniGameMousePosition(mainMenu.map.game);
        endMenu.isButtonEndHovered = endMenu.buttonEnd.contains(cursorPosition);
        if (endMenu.isButtonEndHovered) {
            endMenu.buttonEndHoveredSprite.draw(spriteBatch);
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                mainMenu.map.miniGameOutput = true;
                mainMenu.resetAfterQuit();
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
        public static final int GOOD_RESULT = 10;
        public static final int BEST_RESULT = 14;
        int result;
        private Texture backTexture;

        SpaceInvadersEndMenu (SpriteBatch batch, int result, MiniGameMainMenu menu, SpaceInvaders game) {
            super(batch, menu, game);
            this.result = result;
            if (result < GOOD_RESULT)
                this.menu.map.miniGameResult = MiniGameOutput.LOSE;
            else if (result < BEST_RESULT)
                this.menu.map.miniGameResult = MiniGameOutput.SMALL_WIN;
            else
                this.menu.map.miniGameResult = MiniGameOutput.BIG_WIN;
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
            MiniGameEndMenuUpdate(menu, this);
        }

        public void Draw() {
            spriteBatch.draw(this.backTexture, 430, 220, 1060, 640);
            spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
            if (this.result < GOOD_RESULT)
                this.game.fontGreen.draw(this.spriteBatch, Integer.toString(this.result), 940, 450);
            else
                this.game.fontGreen.draw(this.spriteBatch, Integer.toString(this.result), 930, 450);
            Update();
        }
    }

    static class SpaceInvaders extends MiniGameGameplay{

        static class Player {
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

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && this.nextBullet < MAX_BULLETS) {
                    this.positionWhenShoot[this.nextBullet] = this.position + ROCKET_SIZE/2f - BULLET_SIZE.x/2f;
                    this.bulletPosition[this.nextBullet] = Y_POS;
                    this.showBullet[this.nextBullet] = true;
                    this.lastShoot = this.nextBullet;
                    this.nextBullet = MAX_BULLETS;
                }

                float MAX_X_POS = 1340;
                float MIN_X_POS = 480;
                if (this.position > MAX_X_POS)
                    this.position = MAX_X_POS;
                else if (this.position < MIN_X_POS)
                    this.position = MIN_X_POS;

                for (int i = 0; i< MAX_BULLETS; i++) {
                    if (this.showBullet[i]) {
                        float BULLET_MOVEMENT_OFFSET = 500;
                        this.bulletPosition[i] += deltatime * BULLET_MOVEMENT_OFFSET;
                        if (this.nextBullet == MAX_BULLETS && this.lastShoot == i && this.bulletPosition[i] >= 462)
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
                for (int i = 0; i< MAX_BULLETS; i++) {
                    if (this.showBullet[i])
                        spriteBullet[i].draw(this.spriteBatch);
                }
            }
        }

        static class Enemy {
            public static final float WIDTH_FACTOR_ENEMY = (487 / 321f);
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
                this.spriteEnemy.setSize(WIDTH_FACTOR_ENEMY *ENEMY_SIZE,ENEMY_SIZE);
                float MIN_X_POS = 480 + 2.1f * ENEMY_SIZE;
                this.position = new Vector2(MIN_X_POS + (WIDTH_FACTOR_ENEMY *ENEMY_SIZE + ENEMY_POSITION_OFFSET)*columnNumber, MIN_Y_POS + (ENEMY_SIZE + ENEMY_POSITION_OFFSET)*rowNumber);
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

                if (this.position.x >= 1440 - WIDTH_FACTOR_ENEMY *ENEMY_SIZE || this.position.x <= 480)
                    Enemy.dirToChange = true;
            }

            public void Draw () {
                if (this.toDraw) {
                    Update(Gdx.graphics.getDeltaTime());
                    spriteEnemy.draw(this.spriteBatch);
                }
            }
        }


        public static final int MINI_GAME_TIME = 10;
        public static final int ENEMIES_ROWS = 4;
        public static final int ENEMIES_COLUMNS = 7;
        public static final int MAX_BULLETS = 3;
        public static final int CHANGE_DIRECTION_PROBABILITY = 50;
        Player rocket;
        Enemy[][] enemiesArray;
        private int eliminatedEnemies;

        SpaceInvaders(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, MINI_GAME_TIME);
            this.rocket = new Player(this.spriteBatch);
            this.gameTexture = new Texture("SpaceInvadersMiniGame/Background.jpg");
            this.eliminatedEnemies = 0;
            this.menu.map.gameTextures.timerElapsedTime = 0f;
            this.enemiesArray = new Enemy[ENEMIES_ROWS][ENEMIES_COLUMNS];
            int enemiesLeft = (ENEMIES_COLUMNS*ENEMIES_ROWS)/2, fieldsLeft = ENEMIES_COLUMNS*ENEMIES_ROWS;
            for (int i = 0; i< ENEMIES_ROWS; i++) {
                for (int j = 0; j < ENEMIES_COLUMNS; j++) {
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
            for (int i = 0; i< ENEMIES_ROWS; i++) {
                for (int j = 0; j< ENEMIES_COLUMNS; j++) {
                    for (int k = 0; k<MAX_BULLETS ; k++) {
                        if ((this.rocket.spriteBullet[k].getBoundingRectangle().overlaps(this.enemiesArray[i][j].spriteEnemy.getBoundingRectangle()) && this.enemiesArray[i][j].toDraw && this.rocket.showBullet[k])) {
                            this.enemiesArray[i][j].toDraw = false;
                            this.rocket.showBullet[k] = false;
                            this.eliminatedEnemies++;
                        }
                    }
                    this.enemiesArray[i][j].Draw();
                }
            }

            if(random.nextInt(CHANGE_DIRECTION_PROBABILITY) == 0 || Enemy.dirToChange) {
                Enemy.leftDir = !Enemy.leftDir;
                Enemy.dirToChange = false;
            }
            timerUpdate(deltatime);

            if (this.eliminatedEnemies == (ENEMIES_COLUMNS*ENEMIES_ROWS)/2 || this.timeSeconds == 0) {
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

        public static final int MINI_GAME_TIME = 15;
        public static final int OPERATIONS_NUMBER = 4;
        public static final int MAX_NUMBER_LENGTH = 4;
        public static final int MAX_RESULT_LENGTH = 5;
        public static final int NUMBER1_MINIMUM_VALUE = 100;
        public static final int NUMBER1_RANGE = 1000;
        public static final int NUMBER2_MINIMUM_VALUE_OPERATION_1 = 100;
        public static final int NUMBER2_MINIMUM_VALUE_OPERATIONS_3_4 = 2;
        public static final int NUMBER2_RANGE_OPERATION_1 = 100;
        public static final int NUMBER2_RANGE_OPERATIONS_3_4 = 20;
        public static final int NUMBERS_NUMBER = 10;
        public static final int MAX_ONE_DIGIT_NUMBER = 9;
        public static final int ZERO_INPUT_CODE = 7;
        public static final int ZERO_NUMPAD_INPUT_CODE = 144;
        int number1, number2, operation;
        int[] number1Array, number2Array;
        int expectedResult, playerResult;
        int numberCounter;

        private Texture addSign, subSign, mulSign, divSign, eqSign;
        private final Texture[] numbers;
        private Sprite signSprite, eqSignSprite;
        private final Sprite[] number1Sprite, number2Sprite, resultSprite;
        private final int NUMBER_SIZE = 100;
        private final static String[] TEXTURE_NAMES = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        private static final int MULTIPLY_FACTOR = 10;
        
        MathMiniGame(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, MINI_GAME_TIME);
            this.numberCounter = 0;
            this.operation = random.nextInt(OPERATIONS_NUMBER);
            this.number1 = NUMBER1_MINIMUM_VALUE + random.nextInt(NUMBER1_RANGE);
            if (this.operation == 0) {
                this.number2 = NUMBER2_MINIMUM_VALUE_OPERATION_1 + random.nextInt(NUMBER2_RANGE_OPERATION_1);
                this.expectedResult = this.number1 + this.number2;
            }
            else if (this.operation == 1) {
                this.number2 = 1 + random.nextInt(this.number1-2);
                this.expectedResult = this.number1 - this.number2;
            }
            else if (this.operation == 2) {
                this.number2 = NUMBER2_MINIMUM_VALUE_OPERATIONS_3_4 + random.nextInt(NUMBER2_RANGE_OPERATIONS_3_4);
                this.expectedResult = this.number1 * this.number2;
            }
            else {
                this.number2 = NUMBER2_MINIMUM_VALUE_OPERATIONS_3_4 + random.nextInt(NUMBER2_RANGE_OPERATIONS_3_4);
                this.expectedResult = this.number1 / this.number2;
            }

            this.number1Array = this.intToArray(this.number1);
            this.number2Array = this.intToArray(this.number2);
            this.number1Sprite = new Sprite[MAX_NUMBER_LENGTH];
            this.number2Sprite = new Sprite[MAX_NUMBER_LENGTH];
            this.resultSprite = new Sprite[MAX_RESULT_LENGTH];
            this.numbers = new Texture[NUMBERS_NUMBER];
            this.loadTextures();
        }

        public void loadTextures() {
            this.gameTexture = new Texture("MathMiniGame/Background.png");
            this.eqSign = new Texture("MathMiniGame/EqualSign.png");
            this.addSign = new Texture("MathMiniGame/AddSign.png");
            this.subSign = new Texture("MathMiniGame/SubtractSign.png");
            this.mulSign = new Texture("MathMiniGame/MultiplySign.png");
            this.divSign = new Texture("MathMiniGame/DivideSign.png");
            for (int i = 0; i<NUMBERS_NUMBER; i++)
                this.numbers[i] = new Texture("MathMiniGame/" + TEXTURE_NAMES[i] + ".png");
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

            for (int i = 0; i < MAX_NUMBER_LENGTH; i++) {
                this.number1Sprite[i] = spriteInit(this.numbers[number1Array[i]], 960 - (MAX_NUMBER_LENGTH - i) * (NUMBER_SIZE / 2f + 10) - 50, 700 - SIZE / 2f, NUMBER_SIZE / 2f, NUMBER_SIZE);
                this.number2Sprite[i] = spriteInit(this.numbers[number2Array[i]], 960 + i * (NUMBER_SIZE / 2f + 10) + 50, 700 - SIZE / 2f, NUMBER_SIZE / 2f, NUMBER_SIZE);
            }
        }

        public void refreshResultSprites() {
            for (int i = 0; i<this.numberCounter; i++) {
                this.resultSprite[i] = spriteInit(this.numbers[(this.playerResult/(i==0 ? 1 : (int)Math.pow(10,i)))%10], 960 + (2 - i) * (NUMBER_SIZE / 2f + 10) - NUMBER_SIZE/4f, 380, NUMBER_SIZE / 2f, NUMBER_SIZE);
            }
        }

        public int[] intToArray(int number) {
            int[] array = new int[MAX_NUMBER_LENGTH];
            array[3] = number%10;
            array[2] = (number/10)%10;
            array[1] = (number/100)%10;
            array[0] = number/1000;
            return array;
        }

        public void Draw() {
            spriteBatch.draw(this.gameTexture, 480, 270, 960, 540);
            this.Update(Gdx.graphics.getDeltaTime());
            if (this.timeSeconds > MAX_ONE_DIGIT_NUMBER)
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1314, 737);
            else
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1324, 737);
            this.menu.map.drawMiniGameTimer(1290, 660);
            this.eqSignSprite.draw(this.spriteBatch);
            this.signSprite.draw(this.spriteBatch);
            for (int i = 0; i< MAX_NUMBER_LENGTH; i++) {
                if (this.number1/Math.pow(10, MAX_NUMBER_LENGTH - 1 -i) >= 1)
                    this.number1Sprite[i].draw(this.spriteBatch);
                if (this.number2/Math.pow(10, MAX_NUMBER_LENGTH - 1 -i) >= 1)
                    this.number2Sprite[i].draw(this.spriteBatch);
            }
            for (int i = 0; i<this.numberCounter; i++)
                this.resultSprite[i].draw(this.spriteBatch);
        }

        public void inputGamePlay() {
            if (numberCounter < MAX_RESULT_LENGTH) {
                for (int i = 1 ;i<NUMBERS_NUMBER; i++) {
                    if (Gdx.input.isKeyJustPressed(ZERO_INPUT_CODE+i) || Gdx.input.isKeyJustPressed(ZERO_NUMPAD_INPUT_CODE+i)) {
                        this.playerResult *= MULTIPLY_FACTOR;
                        this.playerResult += i;
                        this.numberCounter++;
                    }
                }
                if (((Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0))) && this.numberCounter >0) {
                    this.numberCounter++;
                    this.playerResult *= MULTIPLY_FACTOR;
                }
            }
            if ((Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) && numberCounter != 0) {
                this.playerResult /= MULTIPLY_FACTOR;
                this.numberCounter--;
            }
        }
        
        public void Update(float deltatime) {
            timerUpdate(deltatime);
            inputGamePlay();
            refreshResultSprites();

            if (this.timeSeconds == 0  || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                this.menu.isStarted = false;
                this.menu.endMenu = new MathMiniGameEndMenu(this.spriteBatch, this.expectedResult == this.playerResult, this.timeSeconds,  this.menu ,this);
                this.menu.isEnding = true;
            }

        }
    }

    static class MathMiniGameEndMenu extends MiniGameEndMenu {
        public static final int GREAT_TIME = 5;
        boolean result;
        int timer;

        MathMiniGameEndMenu (SpriteBatch batch, boolean result, int timer, MiniGameMainMenu menu, MathMiniGame game) {
            super(batch, menu, game);
            this.result = result;
            this.timer = timer;
            if (!result)
                this.menu.map.miniGameResult = MiniGameOutput.LOSE;
            else if (timer < GREAT_TIME)
                this.menu.map.miniGameResult = MiniGameOutput.SMALL_WIN;
            else
                this.menu.map.miniGameResult = MiniGameOutput.BIG_WIN;
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
            MiniGameEndMenuUpdate(menu, this);
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
        public static final int NO_COLOR_FLAG = -1;
        Texture gameTexture1, gameTexture2;
        int[] generatedNumbers, playerNumbers;
        int numberCounter;

        private final Texture[] colors;
        private Texture emptyColor;
        private final Sprite[] gameColors;
        private final int SIZE = 150;
        private final int OFFSET = 30;
        public static final int MINI_GAME_TIME = 15;
        public static final int GENERATED_COLORS_NUMBER = 4;
        public static final int COLORS_NUMBER = 9;
        private final static String[] TEXTURE_NAMES = {"Blue", "Brown", "Cyan", "Green", "Pink", "Purple", "Red", "White", "Yellow"};
        public final static int CHANGE_SCREEN_TIME = 10;
        public static final int MAX_ONE_DIGIT_NUMBER = 9;
        public static final int ZERO_INPUT_CODE = 7;
        public static final int ZERO_NUMPAD_INPUT_CODE = 144;

        MemoryMiniGame(SpriteBatch spriteBatch, MiniGameMainMenu menu) {
            super(spriteBatch, menu, MINI_GAME_TIME);
            this.numberCounter = 0;
            this.generatedNumbers = new int[GENERATED_COLORS_NUMBER];
            this.playerNumbers = new int[GENERATED_COLORS_NUMBER];
            for (int i = 0; i< GENERATED_COLORS_NUMBER; i++)
                this.generatedNumbers[i] = random.nextInt(COLORS_NUMBER-1);

            this.gameColors = new Sprite[GENERATED_COLORS_NUMBER];
            this.colors = new Texture[COLORS_NUMBER];
            this.loadTextures();
        }

        public void loadTextures() {
            this.gameTexture1 = new Texture("MemoryMiniGame/Background1.png");
            this.gameTexture2 = new Texture("MemoryMiniGame/Background2.png");
            this.emptyColor = new Texture("MemoryMiniGame/ColorEmpty.png");
            for (int i = 0; i<COLORS_NUMBER; i++)
                this.colors[i] = new Texture("MemoryMiniGame/Color" + TEXTURE_NAMES[i] + ".png");
            this.texturesToSprites();
        }

        public void texturesToSprites() {
            for (int i = 0; i < GENERATED_COLORS_NUMBER; i++) {
                this.gameColors[i] = spriteInit(this.colors[this.generatedNumbers[i]], 960 - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3 *SIZE/2f, SIZE, SIZE);
            }
        }

         public void refreshResultSprite(int color) {
            if (color == NO_COLOR_FLAG)
                this.gameColors[this.numberCounter] = spriteInit(this.emptyColor, 960 - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3 *SIZE/2f, SIZE, SIZE);
            else
                this.gameColors[this.numberCounter] = spriteInit(this.colors[color], 960 - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3 *SIZE/2f, SIZE, SIZE);

             this.playerNumbers[this.numberCounter] = color;
        }

        public void Draw() {
            if (timeSeconds > CHANGE_SCREEN_TIME)
                spriteBatch.draw(this.gameTexture1, 480, 270, 960, 540);
            else
                spriteBatch.draw(this.gameTexture2, 480, 270, 960, 540);

            this.Update(Gdx.graphics.getDeltaTime());
            if (this.timeSeconds > MAX_ONE_DIGIT_NUMBER)
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1344, 767);
            else
                this.fontYellow.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 1354, 767);
            this.menu.map.drawMiniGameTimer(1320, 690);
            for (int i = 0; i < GENERATED_COLORS_NUMBER; i++) {
                this.gameColors[i].draw(this.spriteBatch);
            }
        }

        public void inputGamePlay() {
            if (numberCounter < GENERATED_COLORS_NUMBER && timeSeconds <= CHANGE_SCREEN_TIME) {
                for (int i = 1 ;i<=COLORS_NUMBER; i++) {
                    if (Gdx.input.isKeyJustPressed(ZERO_INPUT_CODE+i) || Gdx.input.isKeyJustPressed(ZERO_NUMPAD_INPUT_CODE+i)) {
                        refreshResultSprite(i-1);
                        this.numberCounter++;
                    }
                }
            }
            if (this.numberCounter > 0 && Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
                this.numberCounter--;
                refreshResultSprite(NO_COLOR_FLAG);
            }
        }

        public void Update(float deltatime) {
            if (timerUpdate(deltatime) && timeSeconds == CHANGE_SCREEN_TIME) {
                for (int i = 0; i<GENERATED_COLORS_NUMBER; i++)
                    this.gameColors[i] = spriteInit(this.emptyColor, 960 - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, 540 - 3 *SIZE/2f, SIZE, SIZE);
            }
            inputGamePlay();

            if (this.timeSeconds == 0 || (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                boolean check = true;
                for (int i = 0; i<GENERATED_COLORS_NUMBER; i++) {
                    if (this.playerNumbers[i]!=this.generatedNumbers[i]) {
                        check = false;
                        break;
                    }
                }
                this.menu.isStarted = false;
                this.menu.endMenu = new MemoryMiniGameEndMenu(this.spriteBatch, check, timeSeconds, this.menu,this);
                this.menu.isEnding = true;
            }
        }
    }

    static class MemoryMiniGameEndMenu extends MiniGameEndMenu{
        public static final int GREAT_TIME = 5;
        boolean result;
        int timer;

        MemoryMiniGameEndMenu (SpriteBatch batch, boolean result, int timer, MiniGameMainMenu menu, MemoryMiniGame game) {
            super(batch, menu, game);
            this.result = result;
            this.timer = timer;
            if (!result)
                this.menu.map.miniGameResult = MiniGameOutput.LOSE;
            else if (timer < GREAT_TIME)
                this.menu.map.miniGameResult = MiniGameOutput.SMALL_WIN;
            else
                this.menu.map.miniGameResult = MiniGameOutput.BIG_WIN;
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
            MiniGameEndMenuUpdate(menu, this);
        }

        public void Draw() {
            spriteBatch.draw(this.menuTexture, 480, 270, 960, 540);
            Update();
        }
    }
}
