package com.thechinczyk.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class GameObject extends Rectangle{
    private Texture texture;
    public float alpha;
    public boolean soundPlayed;

    public GameObject(Texture texture, float x, float y, float width, float height){
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        alpha = 0;
        soundPlayed = false;
    }

    public Texture getTexture(){
        return this.texture;
    }
}
