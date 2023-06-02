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

    public TextureAtlas bluePlayerAtlas;
    public Animation<TextureRegion> bluePlayerAnim;

    public TextureAtlas greenPlayerAtlas;
    public Animation<TextureRegion> greenPlayerAnim;

    public TextureAtlas pinkPlayerAtlas;
    public Animation<TextureRegion> pinkPlayerAnim;

    public Texture yellowBase3;
    public Texture yellowBase2;
    public Texture yellowBase1;
    public Texture yellowBase0;
    public Texture greenBase3;
    public Texture greenBase2;
    public Texture greenBase1;
    public Texture greenBase0;
    public Texture blueBase3;
    public Texture blueBase2;
    public Texture blueBase1;
    public Texture blueBase0;
    public Texture pinkBase3;
    public Texture pinkBase2;
    public Texture pinkBase1;
    public Texture pinkBase0;

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
    public Texture dice5;
    public Texture dice4;
    public Texture dice3;
    public Texture dice2;
    public Texture dice1;

    public TextureAtlas turnSignAtlas;
    public Animation<TextureRegion> turnSignAnim;
    public float turnSignElapsedTime;
    public short turnSignWhichPlayer;
    public Texture turnSignYellowBackground;
    public Texture turnSignGreenBackground;
    public Texture turnSignBlueBackground;
    public Texture turnSignPinkBackground;

    public Texture pawnCaptureBackground;
    public TextureAtlas pawnCapture1Atlas;
    public Animation<TextureRegion> pawnCapture1Anim;
    public float pawnCapture1ElapsedTime;
    public TextureAtlas pawnCapture2Atlas;
    public Animation<TextureRegion> pawnCapture2Anim;
    public float pawnCapture2ElapsedTime;
    public float pawnCaptureMainElapsedTime;

    public TextureAtlas timerAtlas;
    public Animation<TextureRegion> timerAnim;
    public float timerElapsedTime;

    GameTextures(){
        dayParkBackground = new Texture("Map1/TC_Map1_Main.png");
        dayParkTopground = new Texture("Map1/TC_Map1_TopLayer.png");


        yellowPlayerAtlas = new TextureAtlas("Map1/YellowPlayerAnimSheet/YellowPlayerAnimSheet.atlas");
        yellowPlayerAnim = new Animation<TextureRegion>(1f/30f, yellowPlayerAtlas.getRegions());

        bluePlayerAtlas = new TextureAtlas("Map1/BluePlayerAnimSheet/BluePlayerAnimSheet.atlas");
        bluePlayerAnim = new Animation<TextureRegion>(1f/30f, bluePlayerAtlas.getRegions());

        greenPlayerAtlas = new TextureAtlas("Map1/GreenPlayerAnimSheet/GreenPlayerAnimSheet.atlas");
        greenPlayerAnim = new Animation<TextureRegion>(1f/30f, greenPlayerAtlas.getRegions());

        pinkPlayerAtlas = new TextureAtlas("Map1/PinkPlayerAnimSheet/PinkPlayerAnimSheet.atlas");
        pinkPlayerAnim = new Animation<TextureRegion>(1f/30f, pinkPlayerAtlas.getRegions());

        yellowBase3= new Texture("Map1/Base/Base_Yellow3.png");
        yellowBase2= new Texture("Map1/Base/Base_Yellow2.png");
        yellowBase1= new Texture("Map1/Base/Base_Yellow1.png");
        yellowBase0= new Texture("Map1/Base/Base_Yellow0.png");
        greenBase3= new Texture("Map1/Base/Base_Green3.png");
        greenBase2= new Texture("Map1/Base/Base_Green2.png");
        greenBase1= new Texture("Map1/Base/Base_Green1.png");
        greenBase0= new Texture("Map1/Base/Base_Green0.png");
        blueBase3= new Texture("Map1/Base/Base_Blue3.png");
        blueBase2= new Texture("Map1/Base/Base_Blue2.png");
        blueBase1= new Texture("Map1/Base/Base_Blue1.png");
        blueBase0= new Texture("Map1/Base/Base_Blue0.png");
        pinkBase3= new Texture("Map1/Base/Base_Pink3.png");
        pinkBase2= new Texture("Map1/Base/Base_Pink2.png");
        pinkBase1= new Texture("Map1/Base/Base_Pink1.png");
        pinkBase0= new Texture("Map1/Base/Base_Pink0.png");

        iceCreamAtlas = new TextureAtlas("Map1/IceCreamAnimationSheet/myIceCreamAnimationSheet.atlas");
        iceCreamAnim = new Animation<TextureRegion>(1f/30f, iceCreamAtlas.getRegions());
        loopElapsedTime = 0f;
        swingAtlas = new TextureAtlas("Map1/SwingAnimSheet/SwingAnimSheet.atlas");
        swingAnim = new Animation<TextureRegion>(1f/30f, swingAtlas.getRegions());

        cardAtlas = new TextureAtlas("Map1/CardAnimSheet/CardAnimSheet.atlas");
        cardAnim = new Animation<TextureRegion>(1f/30f, cardAtlas.getRegions());
        cardElapsedTime = 0f;
        cardAnimStarted = false;

        yellowBusAtlas = new TextureAtlas("Map1/BusAnimSheet/BusAnimSheet.atlas");
        yellowBusAnim = new Animation<TextureRegion>(1f/30f, yellowBusAtlas.getRegions());
        yellowBusElapsedTime = 0f;
        yellowBusAnimStarted = false;

        diceAtlas = new TextureAtlas("Map1/DiceAnimSheet/DiceAnimSheet.atlas");
        diceAnim = new Animation<TextureRegion>(1f/30f, diceAtlas.getRegions());
        diceElapsedTime = 0f;
        diceAnimStarted = false;
        dice5 = new Texture("Map1/DiceSides/DiceSides5.png");
        dice4 = new Texture("Map1/DiceSides/DiceSides4.png");
        dice3 = new Texture("Map1/DiceSides/DiceSides3.png");
        dice2 = new Texture("Map1/DiceSides/DiceSides2.png");
        dice1 = new Texture("Map1/DiceSides/DiceSides1.png");

        turnSignAtlas = new TextureAtlas("Map1/TurnSignAnimSheet/TurnSignAnimSheet.atlas");
        turnSignAnim = new Animation<TextureRegion>(1f/30f, turnSignAtlas.getRegions());
        turnSignElapsedTime = 0;
        turnSignWhichPlayer = 0;
        turnSignYellowBackground = new Texture("Map1/TurnSign/yellow.png");
        turnSignGreenBackground = new Texture("Map1/TurnSign/green.png");
        turnSignBlueBackground = new Texture("Map1/TurnSign/blue.png");
        turnSignPinkBackground = new Texture("Map1/TurnSign/pink.png");

        pawnCaptureBackground = new Texture("Map1/TC_Map1_PawnCaptured_Background.png");
        pawnCapture1Atlas = new TextureAtlas("Map1/PawnCapturedAnim1Sheet/PawnCapturedAnim1Sheet.atlas");
        pawnCapture1Anim = new Animation<TextureRegion>(1f/30f, pawnCapture1Atlas.getRegions());
        pawnCapture1ElapsedTime = 0;
        pawnCapture2Atlas = new TextureAtlas("Map1/PawnCapturedAnim2Sheet/PawnCapturedAnim2Sheet.atlas");
        pawnCapture2Anim = new Animation<TextureRegion>(1f/30f, pawnCapture2Atlas.getRegions());
        pawnCapture2ElapsedTime = 0;
        pawnCaptureMainElapsedTime = 0;

        font = new BitmapFont(Gdx.files.internal("Fonts/BerlinSans.fnt"),false);
        font.getData().setScale(.3f,.3f);

        timerAtlas = new TextureAtlas("TimerAnimSheet/TimeAtlas.atlas");
        timerAnim = new Animation<TextureRegion>(1f/26f, timerAtlas.getRegions());
        timerElapsedTime = 0f;
    }
}
