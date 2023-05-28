package com.thechinczyk.game.screens;
import com.thechinczyk.game.GameObject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


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
        //SpaceInvaders game;

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

        SpaceInvadersMenu (SpriteBatch batch, DayParkMap map) {
            this.spriteBatch = batch;
            this.menuTexture = new Texture("SpaceInvadersMiniGame/MainMenuBackground.png");
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
            this.isStarted = false;
            this.isLoaded = false;
            this.map = map;
            //this.game = new SpaceInvaders(spriteBatch, menu, this);
        }

        public void loadTextures() {

            buttonStartHovered = new Texture("SpaceInvadersMiniGame/ButtonSTARTon.png");
            buttonStart = new GameObject(buttonStartHovered, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f + (642 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
            buttonStartHoveredSprite = spriteInit(this.buttonStartHovered, Gdx.graphics.getWidth()/4f + (760 / (1920/(Gdx.graphics.getWidth()/2f))), Gdx.graphics.getHeight()/4f + + (256 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));

            buttonHTPHovered = new Texture("SpaceInvadersMiniGame/ButtonHTPon.png");
            buttonHTP = new GameObject(buttonHTPHovered, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f + (850 / (1920/(Gdx.graphics.getWidth()/2f))), 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
            buttonHTPHoveredSprite = spriteInit(this.buttonHTPHovered, Gdx.graphics.getWidth()/4f + (760 / (1920/(Gdx.graphics.getWidth()/2f))), Gdx.graphics.getHeight()/4f + (30 / (1920/(Gdx.graphics.getWidth()/2f))) , 400 / (1920/(Gdx.graphics.getWidth()/2f)), 200 / (1920/(Gdx.graphics.getWidth()/2f)));
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
                    //if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
                    //    isStarted = true;
                    // uruchomienie mini-gry
                    }
                }

            /*if (this.isStarted) {
                if (!this.isLoaded)
                    game.loadTextures();
                game.Update();
            } */

        }

        public void resetAfterQuit() {
            this.isButtonStartHovered = false;
            this.isButtonHTPHovered = false;
        }

        public void Draw () {
            spriteBatch.draw(this.menuTexture, Gdx.graphics.getWidth()/4f, Gdx.graphics.getHeight()/4f, Gdx.graphics.getWidth()/2f, 1080 / (1920/(Gdx.graphics.getWidth()/2f)));
            Update();
        }
    }
}
