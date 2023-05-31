package com.thechinczyk.game.screens;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thechinczyk.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

enum MiniGamesTypes {NONE, SPACE_INVADERS, MATH, MEMORY};

public class MiniGame {
    public boolean[] isLoaded;
    public SpaceInvadersMenu menuSpaceInvaders;
    public MathMiniGameMenu menuMath;
    public MemoryMiniGameMenu menuMemory;
    SpriteBatch spriteBatch;
    MiniGamesTypes type;

    public MiniGame(SpriteBatch spriteBatch, DayParkMap map) {
        this.isLoaded = new boolean[3];
        this.spriteBatch = spriteBatch;
        this.menuSpaceInvaders = new SpaceInvadersMenu(this.spriteBatch, map);
        this.menuMath = new MathMiniGameMenu(this.spriteBatch, map);
        this.menuMemory = new MemoryMiniGameMenu(this.spriteBatch, map);
    }

    public void loadTextures(MiniGamesTypes type) {
        this.type = type;
        if (type == MiniGamesTypes.SPACE_INVADERS)
            menuSpaceInvaders.loadTextures();
        else if (type == MiniGamesTypes.MATH)
            menuMath.loadTextures();
        else if (type == MiniGamesTypes.MEMORY)
            menuMemory.loadTextures();
    }


    //wykorzystać metodę z innej klasy
    public static Sprite spriteInit(Texture texture, float x, float y, float width, float height) {
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(x, y);
        sprite.setSize(width, height);
        return sprite;
    }

    static class SpaceInvadersMenu {
        SpriteBatch spriteBatch;
        DayParkMap map;
        SpaceInvaders game;
        SpaceInvadersEndMenu endMenu;

        private Texture menuTexture;
        private Texture backTexture;

        private GameObject buttonStart;
        private Texture buttonStartHovered;
        private Sprite buttonStartHoveredSprite;
        private boolean isButtonStartHovered;

        private GameObject buttonHTP;
        private Texture buttonHTPHovered;
        private Sprite buttonHTPHoveredSprite;
        private boolean isButtonHTPHovered;

        private boolean isStarted;
        private boolean isLoaded;
        private boolean isEnding;

        SpaceInvadersMenu(SpriteBatch batch, DayParkMap map) {
            this.spriteBatch = batch;
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isStarted = false;
            this.isLoaded = false;
            this.isEnding = false;
            this.map = map;
        }

        public void loadTextures() {
            this.menuTexture = new Texture("SpaceInvadersMiniGame/MainMenuBackground.png");
            this.backTexture = new Texture("SpaceInvadersMiniGame/MiniGame_Edge.png");

            buttonStartHovered = new Texture("SpaceInvadersMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (642 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + +(256 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));

            buttonHTPHovered = new Texture("SpaceInvadersMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (850 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + (30 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
        }

        public void Update() {
            if (!this.isStarted) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.S) && !this.isButtonStartHovered) {
                    this.isButtonStartHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    this.isButtonStartHovered = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H) && !this.isButtonHTPHovered) {
                    this.isButtonHTPHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    this.isButtonHTPHovered = false;
                } else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                    this.map.unlockMap(MiniGamesTypes.SPACE_INVADERS);
                    this.resetAfterQuit();
                }

                if (this.isButtonHTPHovered) {
                    buttonHTPHoveredSprite.draw(spriteBatch);
                    //if (Gdx.input.isKeyJustPressed(Input.Keys.G))
                    //Wprowadzenie Instrukcji do gry

                } else if (this.isButtonStartHovered) {
                    buttonStartHoveredSprite.draw(spriteBatch);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                        this.game = new SpaceInvaders(spriteBatch, this);
                        isStarted = true;
                    }
                }
            }

            if (this.isStarted) {
                game.Draw();
            }

        }

        public void resetAfterQuit() {
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isEnding = false;
            this.map.unlockMap(MiniGamesTypes.SPACE_INVADERS);
            this.isStarted = false;
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.backTexture, Gdx.graphics.getWidth() / 4f - 50, Gdx.graphics.getHeight() / 4f - 50, Gdx.graphics.getWidth() / 2f + 100, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)) + 100);
                spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
                Update();
            }
            else
                this.endMenu.Draw();
        }
    }

    static class SpaceInvadersEndMenu {
        SpriteBatch spriteBatch;
        int result;
        SpaceInvaders game;
        SpaceInvadersMenu menuSI;

        private Texture menuTexture, backTexture;

        private GameObject buttonEnd;
        private Texture buttonEndHovered;
        private Sprite buttonEndHoveredSprite;
        private boolean isButtonEndHovered;

        SpaceInvadersEndMenu (SpriteBatch batch, int result, SpaceInvadersMenu SImenu, SpaceInvaders game) {
            this.spriteBatch = batch;
            this.result = result;
            this.menuSI = SImenu;
            this.game = game;
            this.loadTextures();
        }

        public void loadTextures() {
            this.backTexture = new Texture("SpaceInvadersMiniGame/MiniGame_Edge.png");
            this.menuTexture = new Texture("SpaceInvadersMiniGame/EndMenuBackground.png");

            buttonEndHovered = new Texture("SpaceInvadersMiniGame/ButtonEXIT_on.png");
            buttonEnd = new GameObject(buttonEndHovered, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f + (850 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, Gdx.graphics.getWidth()/4f + (760 / (1920/(Gdx.graphics.getWidth()/2f))), Gdx.graphics.getHeight()/4f + (30 / (1920/(Gdx.graphics.getWidth()/2f))) , 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
        }

        public void Update() {
            spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f, Gdx.graphics.getWidth()/2f, 1080 / (1920/(Gdx.graphics.getWidth()/2f)));
            if (this.result < 10)
                this.game.font.draw(this.spriteBatch, Integer.toString(this.result), Gdx.graphics.getWidth()/2f - this.game.rocket.SCALE*20, Gdx.graphics.getHeight()/4f + this.game.rocket.SCALE*180);
            else
                this.game.font.draw(this.spriteBatch, Integer.toString(this.result), Gdx.graphics.getWidth()/2f - this.game.rocket.SCALE*30, Gdx.graphics.getHeight()/4f + this.game.rocket.SCALE*180);

            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !this.isButtonEndHovered) {
                this.isButtonEndHovered = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.isButtonEndHovered = false;
            }

            if (this.isButtonEndHovered) {
                buttonEndHoveredSprite.draw(spriteBatch);
                if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                    this.menuSI.map.miniGameOutput[0] = true;
                    menuSI.resetAfterQuit();
                }
            }
        }

        public void Draw() {
            spriteBatch.draw(this.backTexture, Gdx.graphics.getWidth() / 4f - 50, Gdx.graphics.getHeight() / 4f - 50, Gdx.graphics.getWidth() / 2f + 100, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)) + 100);
            Update();
        }
    }

    static class SpaceInvaders {

        static class Player {
            public static final int MAX_BULLETS = 3;
            private float position;
            private float[] bulletPosition;
            public Sprite spritePlayer;
            public Sprite[] spriteBullet;
            private SpriteBatch spriteBatch;
            public final float Y_POS = Gdx.graphics.getHeight() / 4f;
            private final Vector2 BULLET_SIZE = new Vector2(10, 20);
            private final int ROCKET_SIZE = 100;
            private final float SCALE = ((float) 1920 / Gdx.graphics.getWidth());
            private final float PLAYER_MOVEMENT_OFFSET = SCALE * 300;
            private final float MAX_X_POS = 3 * Gdx.graphics.getWidth() / 4f - ROCKET_SIZE * SCALE;
            private final float MIN_X_POS = Gdx.graphics.getWidth() / 4f;
            private boolean[] showBullet;
            private float[] positionWhenShoot;
            private int nextBullet;
            private int lastShoot;
            private final float BULLET_MOVEMENT_OFFSET = SCALE * 500;
            private final float MAX_Y_POS = 3*Gdx.graphics.getHeight()/4f;

            public Player(SpriteBatch batch) {
                this.spriteBatch = batch;
                this.spritePlayer = new Sprite(new Texture("SpaceInvadersMiniGame/PlayerRocket.png"));
                this.spritePlayer.setSize(ROCKET_SIZE, ROCKET_SIZE);
                this.spritePlayer.setScale(SCALE);
                this.spriteBullet = new Sprite[MAX_BULLETS];
                this.bulletPosition = new float[MAX_BULLETS];
                this.showBullet = new boolean[MAX_BULLETS];
                this.positionWhenShoot = new float[MAX_BULLETS];
                for (int i = 0; i < MAX_BULLETS; i++) {
                    this.spriteBullet[i] = new Sprite(new Texture("SpaceInvadersMiniGame/Bullet.png"));
                    this.spriteBullet[i].setSize(BULLET_SIZE.x, BULLET_SIZE.y);
                    this.spriteBullet[i].rotate90(true);
                    this.spriteBullet[i].setScale(SCALE);
                    this.bulletPosition[i] = Y_POS;
                    this.showBullet[i] = false;
                }
                this.position = Gdx.graphics.getWidth() / 2f - SCALE * 50;
                this.nextBullet = 0;
            }

            public void Update(float deltatime) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    this.position -= deltatime * PLAYER_MOVEMENT_OFFSET;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    this.position += deltatime * PLAYER_MOVEMENT_OFFSET;

                if (Gdx.input.isKeyPressed(Input.Keys.UP) && this.nextBullet < 3) {
                    this.positionWhenShoot[this.nextBullet] = this.position + ROCKET_SIZE*SCALE/2 - BULLET_SIZE.x*SCALE/2;
                    this.bulletPosition[this.nextBullet] = Y_POS;
                    this.showBullet[this.nextBullet] = true;
                    this.lastShoot = this.nextBullet;
                    this.nextBullet = 3;
                }

                if (this.position > MAX_X_POS)
                    this.position = MAX_X_POS;
                else if (this.position < MIN_X_POS)
                    this.position = MIN_X_POS;

                for (int i = 0; i<3; i++) {
                    if (this.showBullet[i]) {
                        this.bulletPosition[i] += deltatime * BULLET_MOVEMENT_OFFSET;
                        if (this.nextBullet == 3 && this.lastShoot == i && this.bulletPosition[i] >= 3*Gdx.graphics.getHeight()/7f)
                            this.nextBullet = i==2 ? 0 : (i+1);


                        if (this.bulletPosition[i] >= MAX_Y_POS)
                            this.showBullet[i] = false;
                    }
                }

            }

            public void Draw() {
                Update(Gdx.graphics.getDeltaTime());
                spritePlayer.setPosition(this.position, Y_POS);
                spritePlayer.draw(this.spriteBatch);
                for (int i = 0; i<3; i++) {
                    spriteBullet[i].setPosition(this.positionWhenShoot[i], this.bulletPosition[i]);
                    if (this.showBullet[i])
                        spriteBullet[i].draw(this.spriteBatch);
                }
            }
        }

        static class Enemy {
            private Vector2 position;
            public Sprite spriteEnemy;
            private SpriteBatch spriteBatch;
            private final float SCALE = ((float) 1920 / Gdx.graphics.getWidth());
            private final int ENEMY_SIZE = 60;
            private final float ENEMY_POSITION_OFFSET = 10*SCALE;
            public final float MIN_Y_POS = Gdx.graphics.getHeight()/2f - ENEMY_SIZE + ENEMY_POSITION_OFFSET;
            private boolean toDraw;
            private static boolean leftDir, dirToChange;
            private final float MIN_X_POS = Gdx.graphics.getWidth()/4f + 2.1f*ENEMY_SIZE;
            private final float ENEMY_MOVEMENT_SPEED = SCALE * 100;

            public Enemy(SpriteBatch batch, int rowNumber, int columnNumber, boolean toDraw) {
                this.spriteBatch = batch;
                this.spriteEnemy = new Sprite(new Texture("SpaceInvadersMiniGame/Enemy.png"));
                this.spriteEnemy.setSize((487/321f)*ENEMY_SIZE,ENEMY_SIZE);
                this.spriteEnemy.setScale(SCALE);
                this.position = new Vector2(MIN_X_POS + ((487/321f)*ENEMY_SIZE*SCALE + ENEMY_POSITION_OFFSET)*columnNumber, MIN_Y_POS + (ENEMY_SIZE + ENEMY_POSITION_OFFSET)*rowNumber);
                this.toDraw = toDraw;
                spriteEnemy.setPosition(this.position.x, this.position.y);
            }

            public void Update (float deltatime) {
                if (Enemy.leftDir)
                    this.position.x -= deltatime * ENEMY_MOVEMENT_SPEED;
                else
                    this.position.x += deltatime * ENEMY_MOVEMENT_SPEED;

                spriteEnemy.setPosition(this.position.x, this.position.y);

                if (this.position.x >= 3*Gdx.graphics.getWidth()/4f - (487/321f)*ENEMY_SIZE || this.position.x <= Gdx.graphics.getWidth()/4f)
                    Enemy.dirToChange = true;
            }

            public void Draw () {
                if (this.toDraw) {
                    Update(Gdx.graphics.getDeltaTime());
                    spriteEnemy.draw(this.spriteBatch);
                }
            }
        }

        static Random random = new Random();
        SpriteBatch spriteBatch;
        Texture gameTexture;
        Player rocket;
        SpaceInvadersMenu menuSI;
        int timeSeconds;
        float timer;
        BitmapFont font;
        Enemy[][] enemiesArray;
        private int eliminatedEnemies;


        SpaceInvaders(SpriteBatch spriteBatch, SpaceInvadersMenu menuSI) {
            this.font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
            this.font.getData().setScale(.5f,.5f);
            //zmiana koloru nie działa
            this.font.setColor(Color.WHITE);
            this.spriteBatch = spriteBatch;
            this.rocket = new Player(this.spriteBatch);
            this.gameTexture = new Texture("SpaceInvadersMiniGame/Background.jpg");
            this.menuSI = menuSI;
            this.timer = 0;
            this.timeSeconds = 1;
            this.eliminatedEnemies = 0;
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
            spriteBatch.draw(this.gameTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            this.Update(Gdx.graphics.getDeltaTime());
            this.font.draw(this.spriteBatch, Integer.toString(this.eliminatedEnemies), Gdx.graphics.getWidth()/4f + 20, 3*Gdx.graphics.getHeight()/4f - 20);
            this.font.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 3*Gdx.graphics.getWidth()/4f - 40, 3*Gdx.graphics.getHeight()/4f - 20);
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
            timer += deltatime;
            if(timer > 1){
                timer-=1;
                timeSeconds ++;
            }

            if (this.eliminatedEnemies == 14 || this.timeSeconds == 11) {
                //reset wszystkiego w grze, menu glownym i na planszy
                this.menuSI.isStarted = false;
                this.menuSI.endMenu = new SpaceInvadersEndMenu(this.spriteBatch, this.eliminatedEnemies, this.menuSI, this);
                this.menuSI.isEnding = true;
            }

            if ((Gdx.input.isKeyJustPressed(Input.Keys.X))) {
                menuSI.resetAfterQuit();
            }

        }
    }

    static class MathMiniGameMenu {
        SpriteBatch spriteBatch;
        DayParkMap map;
        MathMiniGame game;
        MathMiniGameEndMenu endMenu;

        private Texture menuTexture;

        private GameObject buttonStart;
        private Texture buttonStartHovered;
        private Sprite buttonStartHoveredSprite;
        private boolean isButtonStartHovered;

        private GameObject buttonHTP;
        private Texture buttonHTPHovered;
        private Sprite buttonHTPHoveredSprite;
        private boolean isButtonHTPHovered;

        private boolean isStarted;
        private boolean isLoaded;
        private boolean isEnding;

        MathMiniGameMenu(SpriteBatch batch, DayParkMap map) {
            this.spriteBatch = batch;
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isStarted = false;
            this.isLoaded = false;
            this.isEnding = false;
            this.map = map;
        }

        public void loadTextures() {
            this.menuTexture = new Texture("MathMiniGame/MainMenuBackground.png");

            buttonStartHovered = new Texture("MathMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (600 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + +(289 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));

            buttonHTPHovered = new Texture("MathMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (800 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + (75 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
        }

        public void Update() {
            if (!this.isStarted) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.S) && !this.isButtonStartHovered) {
                    this.isButtonStartHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    this.isButtonStartHovered = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H) && !this.isButtonHTPHovered) {
                    this.isButtonHTPHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    this.isButtonHTPHovered = false;
                } else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                    this.map.unlockMap(MiniGamesTypes.MATH);
                    this.resetAfterQuit();
                }

                if (this.isButtonHTPHovered) {
                    buttonHTPHoveredSprite.draw(spriteBatch);
                    //if (Gdx.input.isKeyJustPressed(Input.Keys.G))
                    //Wprowadzenie Instrukcji do gry

                } else if (this.isButtonStartHovered) {
                    buttonStartHoveredSprite.draw(spriteBatch);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                        this.game = new MathMiniGame(spriteBatch, this);
                        isStarted = true;
                    }
                }
            }

            if (this.isStarted) {
                game.Draw();
            }

        }

        public void resetAfterQuit() {
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isEnding = false;
            this.map.unlockMap(MiniGamesTypes.MATH);
            this.isStarted = false;
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
                Update();
            }
            else
               this.endMenu.Draw();
        }
    }

    static class MathMiniGame {

        static Random random = new Random();
        SpriteBatch spriteBatch;
        Texture gameTexture;
        MathMiniGameMenu menuMMG;
        int timeSeconds;
        float timer;
        BitmapFont font;
        boolean gameOutput;
        int number1, number2, operation;
        int[] number1Array, number2Array;
        int expectedResult, playerResult;
        int numberCounter;

        private Texture addSign, subSign, mulSign, divSign, eqSign;
        private Texture[] numbers;
        private Sprite signSprite, eqSignSprite;
        private Sprite[] number1Sprite, number2Sprite, resultSprite;
        private final int SIZE = 200, NUMBER_SIZE = 100;


        MathMiniGame(SpriteBatch spriteBatch, MathMiniGameMenu menuMMG) {
            this.font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
            this.font.getData().setScale(.5f,.5f);
            //zmiana koloru nie działa
            this.font.setColor(Color.WHITE);
            this.spriteBatch = spriteBatch;
            this.menuMMG = menuMMG;
            this.timer = 0;
            this.timeSeconds = 1;
            this.numberCounter = 0;
            this.operation = random.nextInt(3);
            this.number1 = 100 + random.nextInt(1000);
            if (this.operation == 0) {
                this.number2 = 100 + random.nextInt(1000);
                this.expectedResult = this.number1 + this.number2;
            }
            else if (this.operation == 1) {
                this.number2 = random.nextInt(this.number1);
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
            if (this.operation == 0)
                this.signSprite = spriteInit(this.addSign, Gdx.graphics.getWidth() / 2f - SIZE / 2f, Gdx.graphics.getHeight() / 2f + 100 - SIZE / 2f, SIZE, SIZE);
            else if (this.operation == 1)
                this.signSprite = spriteInit(this.subSign, Gdx.graphics.getWidth() / 2f - SIZE / 2f, Gdx.graphics.getHeight() / 2f + 100 - SIZE / 2f, SIZE, SIZE);
            else if (this.operation == 2)
                this.signSprite = spriteInit(this.mulSign, Gdx.graphics.getWidth() / 2f - SIZE / 2f, Gdx.graphics.getHeight() / 2f + 100 - SIZE / 2f, SIZE, SIZE);
            else
                this.signSprite = spriteInit(this.divSign, Gdx.graphics.getWidth() / 2f - SIZE / 2f, Gdx.graphics.getHeight() / 2f + 100 - SIZE / 2f, SIZE, SIZE);

            this.eqSignSprite = spriteInit(this.eqSign, Gdx.graphics.getWidth() / 2f - SIZE / 2f, Gdx.graphics.getHeight() / 2f - SIZE / 2f, SIZE, SIZE);

            for (int i = 0; i < 4; i++) {
                this.number1Sprite[i] = spriteInit(this.numbers[number1Array[i]], Gdx.graphics.getWidth() / 2f - (4 - i) * (NUMBER_SIZE / 2f + 10) - 50, Gdx.graphics.getHeight() / 2f - SIZE / 2f + 160, NUMBER_SIZE / 2f, NUMBER_SIZE);
                this.number2Sprite[i] = spriteInit(this.numbers[number2Array[i]], Gdx.graphics.getWidth() / 2f + i * (NUMBER_SIZE / 2f + 10) + 50, Gdx.graphics.getHeight() / 2f - SIZE / 2f + 160, NUMBER_SIZE / 2f, NUMBER_SIZE);
            }
        }

        public void refreshResultSprites() {
            for (int i = 0; i<this.numberCounter; i++) {
                this.resultSprite[i] = spriteInit(this.numbers[(this.playerResult/(i==0 ? 1 : (int)Math.pow(10,i)))%10], Gdx.graphics.getWidth() / 2f + (2 - i) * (NUMBER_SIZE / 2f + 10) - NUMBER_SIZE/4f, Gdx.graphics.getHeight() / 2f - 160, NUMBER_SIZE / 2f, NUMBER_SIZE);
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
            spriteBatch.draw(this.gameTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            this.Update(Gdx.graphics.getDeltaTime());
            this.font.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 3*Gdx.graphics.getWidth()/4f - 40, 3*Gdx.graphics.getHeight()/4f - 20);
            this.eqSignSprite.draw(this.spriteBatch);
            this.signSprite.draw(this.spriteBatch);
            for (int i = 0; i<4; i++) {
                this.number1Sprite[i].draw(this.spriteBatch);
                this.number2Sprite[i].draw(this.spriteBatch);
            }
            for (int i = 0; i<this.numberCounter; i++)
                this.resultSprite[i].draw(this.spriteBatch);
        }

        public void Update(float deltatime) {
            timer += deltatime;
            if(timer > 1){
                timer-=1;
                timeSeconds ++;
            }

            System.out.println(this.playerResult);
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

            if (/*this.timeSeconds == 11 */ (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                //reset wszystkiego w grze, menu glownym i na planszy
                this.menuMMG.isStarted = false;
                this.menuMMG.endMenu = new MathMiniGameEndMenu(this.spriteBatch, this.expectedResult == this.playerResult, this.timeSeconds,  this.menuMMG ,this);
                this.menuMMG.isEnding = true;
            }

            if ((Gdx.input.isKeyJustPressed(Input.Keys.X))) {
                menuMMG.resetAfterQuit();
            }

        }
    }

    static class MathMiniGameEndMenu {
        SpriteBatch spriteBatch;
        boolean result;
        int timer;
        MathMiniGame game;
        MathMiniGameMenu menuMMG;

        private Texture menuTexture;

        private GameObject buttonEnd;
        private Texture buttonEndHovered;
        private Sprite buttonEndHoveredSprite;
        private boolean isButtonEndHovered;

        MathMiniGameEndMenu (SpriteBatch batch, boolean result, int timer, MathMiniGameMenu menuMMG, MathMiniGame game) {
            this.spriteBatch = batch;
            this.result = result;
            this.timer = timer;
            this.menuMMG = menuMMG;
            this.game = game;
            this.loadTextures();
        }

        public void loadTextures() {
            if (this.result)
                this.menuTexture = new Texture("MathMiniGame/BackgroundEndGameRight.png");
            else
                this.menuTexture = new Texture("MathMiniGame/BackgroundEndGameWrong.png");

            buttonEndHovered = new Texture("MathMiniGame/ButtonEXIT_on.png");
            buttonEnd = new GameObject(buttonEndHovered, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f + (850 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, Gdx.graphics.getWidth()/4f + (760 / (1920/(Gdx.graphics.getWidth()/2f))), Gdx.graphics.getHeight()/4f + (30 / (1920/(Gdx.graphics.getWidth()/2f))) , 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
        }

        public void Update() {
            spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f, Gdx.graphics.getWidth()/2f, 1080 / (1920/(Gdx.graphics.getWidth()/2f)));
            /*if (this.result < 10)
                this.game.font.draw(this.spriteBatch, Integer.toString(this.result), Gdx.graphics.getWidth()/2f - this.game.rocket.SCALE*20, Gdx.graphics.getHeight()/4f + this.game.rocket.SCALE*180);
            else
                this.game.font.draw(this.spriteBatch, Integer.toString(this.result), Gdx.graphics.getWidth()/2f - this.game.rocket.SCALE*30, Gdx.graphics.getHeight()/4f + this.game.rocket.SCALE*180); */

            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !this.isButtonEndHovered) {
                this.isButtonEndHovered = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.isButtonEndHovered = false;
            }

            if (this.isButtonEndHovered) {
                buttonEndHoveredSprite.draw(spriteBatch);
                if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                    this.menuMMG.map.miniGameOutput[1] = true;
                    menuMMG.resetAfterQuit();
                }
            }
        }

        public void Draw() {
            Update();
        }
    }

    static class MemoryMiniGameMenu {
        SpriteBatch spriteBatch;
        DayParkMap map;
        MemoryMiniGame game;
        MemoryMiniGameEndMenu endMenu;

        private Texture menuTexture;

        private GameObject buttonStart;
        private Texture buttonStartHovered;
        private Sprite buttonStartHoveredSprite;
        private boolean isButtonStartHovered;

        private GameObject buttonHTP;
        private Texture buttonHTPHovered;
        private Sprite buttonHTPHoveredSprite;
        private boolean isButtonHTPHovered;

        private boolean isStarted;
        private boolean isLoaded;
        private boolean isEnding;

        MemoryMiniGameMenu(SpriteBatch batch, DayParkMap map) {
            this.spriteBatch = batch;
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isStarted = false;
            this.isLoaded = false;
            this.isEnding = false;
            this.map = map;
        }

        public void loadTextures() {
            this.menuTexture = new Texture("MemoryMiniGame/MainMenuBackground.png");

            buttonStartHovered = new Texture("MemoryMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (600 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + (257 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));

            buttonHTPHovered = new Texture("MemoryMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f + (800 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, Gdx.graphics.getWidth() / 4f + (760 / (1920 / (Gdx.graphics.getWidth() / 2f))), Gdx.graphics.getHeight() / 4f + (40 / (1920 / (Gdx.graphics.getWidth() / 2f))), 400 / (1920 / (Gdx.graphics.getWidth() / 2f)), 200 / (1920 / (Gdx.graphics.getWidth() / 2f)));
        }

        public void Update() {
            if (!this.isStarted) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.S) && !this.isButtonStartHovered) {
                    this.isButtonStartHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    this.isButtonStartHovered = false;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H) && !this.isButtonHTPHovered) {
                    this.isButtonHTPHovered = true;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.H)) {
                    this.isButtonHTPHovered = false;
                } else if (Gdx.input.isKeyPressed(Input.Keys.X)) {
                    this.map.unlockMap(MiniGamesTypes.MEMORY);
                    this.resetAfterQuit();
                }

                if (this.isButtonHTPHovered) {
                    buttonHTPHoveredSprite.draw(spriteBatch);
                    //if (Gdx.input.isKeyJustPressed(Input.Keys.G))
                    //Wprowadzenie Instrukcji do gry

                } else if (this.isButtonStartHovered) {
                    buttonStartHoveredSprite.draw(spriteBatch);
                    if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                        this.game = new MemoryMiniGame(spriteBatch, this);
                        isStarted = true;
                    }
                }
            }

            if (this.isStarted) {
                game.Draw();
            }

        }

        public void resetAfterQuit() {
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isEnding = false;
            this.map.unlockMap(MiniGamesTypes.MEMORY);
            this.isStarted = false;
        }

        public void Draw() {
            if (!isEnding) {
                spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
                Update();
            }
            else
                this.endMenu.Draw();
        }
    }

    static class MemoryMiniGame {

        static Random random = new Random();
        SpriteBatch spriteBatch;
        Texture gameTexture1, gameTexture2;
        MemoryMiniGameMenu menuMemory;
        int timeSeconds;
        float timer;
        BitmapFont font;
        boolean gameOutput;
        int[] generatedNumbers, playerNumbers;
        int numberCounter;

        private GameObject[][] colorsBoxGameObjects;
        private Sprite[][] colorsBoxSprites;
        private Texture[] colors;
        private Texture emptyColor;
        private Sprite signSprite, eqSignSprite;
        private Sprite[] chooseColor, gameColors;
        private final int SIZE = 150, OFFSET = 30, BOX_SIZE = 100;


        MemoryMiniGame(SpriteBatch spriteBatch, MemoryMiniGameMenu menuMemory) {
            this.font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
            this.font.getData().setScale(.5f,.5f);
            this.spriteBatch = spriteBatch;
            this.menuMemory = menuMemory;
            this.timer = 0;
            this.timeSeconds = 1;
            this.numberCounter = 0;
            this.generatedNumbers = new int[4];
            this.playerNumbers = new int[4];
            for (int i = 0; i<4; i++)
                this.generatedNumbers[i] = random.nextInt(8);

            this.gameColors = new Sprite[4];
            this.chooseColor = new Sprite[9];
            this.colorsBoxGameObjects = new GameObject[3][3];
            this.colorsBoxSprites = new Sprite[3][3];
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
                this.gameColors[i] = spriteInit(this.colors[this.generatedNumbers[i]], Gdx.graphics.getWidth() / 2f - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, Gdx.graphics.getHeight() / 2f - 3*SIZE/2f, SIZE, SIZE);
            }
            for (int w = 0; w<3; w++) {
                for (int k = 0; k<3; k++) {
                    this.colorsBoxSprites[w][k] = spriteInit(this.colors[w*3 + k], Gdx.graphics.getWidth() / 2f - (1-k) * BOX_SIZE - BOX_SIZE/2f,3*(Gdx.graphics.getHeight() / 4f) - w * BOX_SIZE - 60, BOX_SIZE, BOX_SIZE);
                    this.colorsBoxGameObjects[w][k] = new GameObject(this.colors[w*3 + k], Gdx.graphics.getWidth() / 2f - (1-k) * BOX_SIZE - BOX_SIZE/2f,3*(Gdx.graphics.getHeight() / 4f) - w * BOX_SIZE - 60, BOX_SIZE, BOX_SIZE);
                }
            }
        }


         public void refreshResultSprite(int color) {
            if (color == -1)
                this.gameColors[this.numberCounter] = spriteInit(this.emptyColor, Gdx.graphics.getWidth() / 2f - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, Gdx.graphics.getHeight() / 2f - 3*SIZE/2f, SIZE, SIZE);
            else
                this.gameColors[this.numberCounter] = spriteInit(this.colors[color], Gdx.graphics.getWidth() / 2f - (1 - this.numberCounter) * (SIZE+OFFSET) - SIZE - OFFSET/2f, Gdx.graphics.getHeight() / 2f - 3*SIZE/2f, SIZE, SIZE);

             this.playerNumbers[this.numberCounter] = color;
        }

        public void displayColorBox() {
            for (int w = 0; w<3; w++) {
                for (int k = 0; k<3; k++) {
                    this.colorsBoxSprites[w][k].draw(this.spriteBatch);
                }
            }
        }

        public void Draw() {
            if (timeSeconds < 5)
                spriteBatch.draw(this.gameTexture1, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            else {
                spriteBatch.draw(this.gameTexture2, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
                displayColorBox();
            }

            this.Update(Gdx.graphics.getDeltaTime());
            this.font.draw(this.spriteBatch, Integer.toString(this.timeSeconds), 3 * Gdx.graphics.getWidth() / 4f - 40, 3 * Gdx.graphics.getHeight() / 4f - 20);
            for (int i = 0; i < 4; i++) {
                this.gameColors[i].draw(this.spriteBatch);
            }
        }

        public void Update(float deltatime) {
            timer += deltatime;
            if(timer > 1){
                timer-=1;
                timeSeconds ++;
                if (timeSeconds == 5) {
                    for (int i = 0; i<4; i++)
                        this.gameColors[i] = spriteInit(this.emptyColor, Gdx.graphics.getWidth() / 2f - (1 - i) * (SIZE+OFFSET) - SIZE - OFFSET/2f, Gdx.graphics.getHeight() / 2f - 3*SIZE/2f, SIZE, SIZE);
                }
            }

            System.out.println(this.numberCounter);

            if (numberCounter < 4 && timeSeconds >= 5) {
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

            if (/*this.timeSeconds == 11 */ (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER))) {
                //reset wszystkiego w grze, menu glownym i na planszy
                boolean check = true;
                for (int i = 0; i<4; i++) {
                    if (this.playerNumbers[i]!=this.generatedNumbers[i]) {
                        check = false;
                        break;
                    }
                }
                this.menuMemory.isStarted = false;
                this.menuMemory.endMenu = new MemoryMiniGameEndMenu(this.spriteBatch, check,  this.menuMemory,this);
                this.menuMemory.isEnding = true;
            }

            if ((Gdx.input.isKeyJustPressed(Input.Keys.X))) {
                menuMemory.resetAfterQuit();
            }

        }
    }

    static class MemoryMiniGameEndMenu {
        SpriteBatch spriteBatch;
        boolean result;
        MemoryMiniGame game;
        MemoryMiniGameMenu menuMemory;

        private Texture menuTexture;

        private GameObject buttonEnd;
        private Texture buttonEndHovered;
        private Sprite buttonEndHoveredSprite;
        private boolean isButtonEndHovered;

        MemoryMiniGameEndMenu (SpriteBatch batch, boolean result, MemoryMiniGameMenu menuMemory, MemoryMiniGame game) {
            this.spriteBatch = batch;
            this.result = result;
            this.menuMemory = menuMemory;
            this.game = game;
            this.loadTextures();
        }

        public void loadTextures() {
            if (this.result)
                this.menuTexture = new Texture("MemoryMiniGame/BackgroundEndGameRight.png");
            else
                this.menuTexture = new Texture("MemoryMiniGame/BackgroundEndGameWrong.png");

            buttonEndHovered = new Texture("MemoryMiniGame/ButtonEXITon.png");
            buttonEnd = new GameObject(buttonEndHovered, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f + (850 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
            buttonEndHoveredSprite = spriteInit(this.buttonEndHovered, Gdx.graphics.getWidth()/4f + (760 / (1920/(Gdx.graphics.getWidth()/2f))), Gdx.graphics.getHeight()/4f + (38 / (1920/(Gdx.graphics.getWidth()/2f))) , 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
        }

        public void Update() {
            spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f, Gdx.graphics.getWidth()/2f, 1080 / (1920/(Gdx.graphics.getWidth()/2f)));

            if (Gdx.input.isKeyJustPressed(Input.Keys.X) && !this.isButtonEndHovered) {
                this.isButtonEndHovered = true;
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.isButtonEndHovered = false;
            }

            if (this.isButtonEndHovered) {
                buttonEndHoveredSprite.draw(spriteBatch);
                if (Gdx.input.isKeyPressed(Input.Keys.G)) {
                    this.menuMemory.map.miniGameOutput[2] = true;
                    menuMemory.resetAfterQuit();
                }
            }
        }

        public void Draw() {
            Update();
        }
    }
}
