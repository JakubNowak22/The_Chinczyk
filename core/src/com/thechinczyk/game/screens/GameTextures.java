package com.thechinczyk.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class GameTextures{
    BitmapFont font;
    public Texture dayParkBackground;
    public Texture dayParkTopground;


    public TextureAtlas yellowPlayerAtlas;
    public Animation<TextureRegion> yellowPlayerAnim;
    //public float yellowPlayerElapsedTime;

    //public int yellowPlayerAnimStarted;

    public TextureAtlas bluePlayerAtlas;
    public Animation<TextureRegion> bluePlayerAnim;
    //public float bluePlayerElapsedTime;
    //public int bluePlayerAnimStarted;

    public TextureAtlas greenPlayerAtlas;
    public Animation<TextureRegion> greenPlayerAnim;
    //public float greenPlayerElapsedTime;

    public TextureAtlas pinkPlayerAtlas;
    public Animation<TextureRegion> pinkPlayerAnim;
    //public float pinkPlayerElapsedTime;

    public TextureAtlas iceCreamAtlas;
    public Animation<TextureRegion> iceCreamAnim;
    public float loopElapsedTime;
    public TextureAtlas swingAtlas;
    public Animation<TextureRegion> swingAnim;

    public TextureAtlas cardAtlas;
    public Animation<TextureRegion> cardAnim;
    public float cardElapsedTime;
    public boolean cardAnimStarted;

    public TextureAtlas yellowBusAtlas;
    public Animation<TextureRegion> yellowBusAnim;
    public float yellowBusElapsedTime;
    public boolean yellowBusAnimStarted;

    public TextureAtlas diceAtlas;
    public Animation<TextureRegion> diceAnim;
    public float diceElapsedTime;
    public boolean diceAnimStarted;

    public TextureAtlas turnSignAtlas;
    public Animation<TextureRegion> turnSignAnim;
    public float turnSignElapsedTime;
    public short turnSignWhichPlayer;
    public Texture turnSignYellowBackground;
    public Texture turnSignGreenBackground;
    public Texture turnSignBlueBackground;
    public Texture turnSignPinkBackground;

    GameTextures(){
        dayParkBackground = new Texture("Map1/TC_Map1_Main.png");
        dayParkTopground = new Texture("Map1/TC_Map1_TopLayer.png");


        yellowPlayerAtlas = new TextureAtlas("Map1/YellowPlayerAnimSheet/YellowPlayerAnimSheet.atlas");
        yellowPlayerAnim = new Animation<TextureRegion>(1f/30f, yellowPlayerAtlas.getRegions());
        //yellowPlayerElapsedTime = 0f;
        //yellowPlayerAnimStarted = 0;

        bluePlayerAtlas = new TextureAtlas("Map1/BluePlayerAnimSheet/BluePlayerAnimSheet.atlas");
        bluePlayerAnim = new Animation<TextureRegion>(1f/30f, bluePlayerAtlas.getRegions());
        //bluePlayerElapsedTime = 0f;
        //bluePlayerAnimStarted = 0;

        greenPlayerAtlas = new TextureAtlas("Map1/GreenPlayerAnimSheet/GreenPlayerAnimSheet.atlas");
        greenPlayerAnim = new Animation<TextureRegion>(1f/30f, greenPlayerAtlas.getRegions());
        //greenPlayer1ElapsedTime = 0f;

        pinkPlayerAtlas = new TextureAtlas("Map1/PinkPlayerAnimSheet/PinkPlayerAnimSheet.atlas");
        pinkPlayerAnim = new Animation<TextureRegion>(1f/30f, pinkPlayerAtlas.getRegions());
        //pinkPlayer1ElapsedTime = 0f;

        iceCreamAtlas = new TextureAtlas("Map1/IceCreamAnimationSheet/myIceCreamAnimationSheet.atlas");
        iceCreamAnim = new Animation<TextureRegion>(1f/30f, iceCreamAtlas.getRegions());
        loopElapsedTime = 0f;
        swingAtlas = new TextureAtlas("Map1/SwingAnimSheet/SwingAnimSheet.atlas");
        swingAnim = new Animation<TextureRegion>(1f/30f, swingAtlas.getRegions());

        cardAtlas = new TextureAtlas("Map1/CardAnimSheet/CardAnimSheet.atlas");
        cardAnim = new Animation<TextureRegion>(1f/30f, cardAtlas.getRegions());
        cardElapsedTime = 0f;
        cardAnimStarted = false;

        yellowBusAtlas = new TextureAtlas("Map1/YellowBusAnimSheet/YellowBusAnimSheet.atlas");
        yellowBusAnim = new Animation<TextureRegion>(1f/30f, yellowBusAtlas.getRegions());
        yellowBusElapsedTime = 0f;
        yellowBusAnimStarted = false;

        diceAtlas = new TextureAtlas("Map1/DiceAnimSheet/DiceAnimSheet.atlas");
        diceAnim = new Animation<TextureRegion>(1f/30f, diceAtlas.getRegions());
        diceElapsedTime = 0f;
        diceAnimStarted = false;

        turnSignAtlas = new TextureAtlas("Map1/TurnSignAnimSheet/TurnSignAnimSheet.atlas");
        turnSignAnim = new Animation<TextureRegion>(1f/30f, turnSignAtlas.getRegions());
        turnSignElapsedTime = 0;
        turnSignWhichPlayer = 1;
        turnSignYellowBackground = new Texture("Map1/TurnSign/yellow.png");
        turnSignGreenBackground = new Texture("Map1/TurnSign/green.png");
        turnSignBlueBackground = new Texture("Map1/TurnSign/blue.png");
        turnSignPinkBackground = new Texture("Map1/TurnSign/pink.png");

        font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
        font.getData().setScale(.3f,.3f);
    }

}