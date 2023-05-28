package com.thechinczyk.game.screens;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.thechinczyk.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;


public class MiniGame {
    public boolean isLoaded;
    public SpaceInvadersMenu menuSpaceInvaders;
    SpriteBatch spriteBatch;

    public MiniGame(SpriteBatch spriteBatch, DayParkMap map) {
        this.isLoaded = false;
        this.spriteBatch = spriteBatch;
        this.menuSpaceInvaders = new SpaceInvadersMenu(this.spriteBatch, map);
    }

    static class SpaceInvadersMenu {
        SpriteBatch spriteBatch;
        DayParkMap map;
        SpaceInvaders game;

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

        SpaceInvadersMenu(SpriteBatch batch, DayParkMap map) {
            this.spriteBatch = batch;
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isStarted = false;
            this.isLoaded = false;
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

        //wykorzystać metodę z innej klasy
        public Sprite spriteInit(Texture texture, float x, float y, float width, float height) {
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(x, y);
            sprite.setSize(width, height);
            return sprite;
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
                    this.map.unlockMap();
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
            this.map.unlockMap();
            this.isStarted = false;
        }

        public void Draw() {
            spriteBatch.draw(this.backTexture, Gdx.graphics.getWidth() / 4f - 75, Gdx.graphics.getHeight() / 4f - 75, Gdx.graphics.getWidth() / 2f + 150, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)) + 150);
            spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
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

            public Player(SpriteBatch batch) {
                this.spriteBatch = batch;
                this.spritePlayer = new Sprite(new Texture("SpaceInvadersMiniGame/PlayerRocket.png"));
                this.spritePlayer.setSize(ROCKET_SIZE, ROCKET_SIZE);
                this.spritePlayer.setScale(SCALE);
                this.spriteBullet = new Sprite[MAX_BULLETS];
                this.bulletPosition = new float[MAX_BULLETS];
                for (int i = 0; i < MAX_BULLETS; i++) {
                    this.spriteBullet[i] = new Sprite(new Texture("SpaceInvadersMiniGame/Bullet.png"));
                    this.spriteBullet[i].setSize(BULLET_SIZE.x, BULLET_SIZE.y);
                    this.spriteBullet[i].rotate90(true);
                    this.spriteBullet[i].setScale(SCALE);
                    this.bulletPosition[i] = Y_POS;
                }
                this.position = Gdx.graphics.getWidth() / 2f - SCALE * 50;
            }

            public void Update(float deltatime) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    this.position -= deltatime * PLAYER_MOVEMENT_OFFSET;
                else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    this.position += deltatime * PLAYER_MOVEMENT_OFFSET;

                if (this.position > MAX_X_POS)
                    this.position = MAX_X_POS;
                else if (this.position < MIN_X_POS)
                    this.position = MIN_X_POS;

            }

            public void Draw() {
                Update(Gdx.graphics.getDeltaTime());
                spritePlayer.setPosition(this.position, Y_POS);
                spritePlayer.draw(this.spriteBatch);
            }
        }

        static Random random = new Random();
        SpriteBatch spriteBatch;
        Texture gameTexture;
        Player rocket;
        SpaceInvadersMenu menuSI;

        SpaceInvaders(SpriteBatch spriteBatch, SpaceInvadersMenu menuSI) {
            this.spriteBatch = spriteBatch;
            this.rocket = new Player(this.spriteBatch);
            this.gameTexture = new Texture("SpaceInvadersMiniGame/Background.jpg");
            this.menuSI = menuSI;
        }

        public void Draw() {
            spriteBatch.draw(this.gameTexture, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 4f, Gdx.graphics.getWidth() / 2f, 1080 / (1920 / (Gdx.graphics.getWidth() / 2f)));
            this.Update();
        }

        public void Update() {
            this.rocket.Draw();

            if ((Gdx.input.isKeyJustPressed(Input.Keys.X))) {
                menuSI.resetAfterQuit();
            }

        }
    }
}
