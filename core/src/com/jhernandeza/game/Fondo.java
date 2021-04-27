package com.jhernandeza.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fondo {
    Texture texture;

    public Fondo(){
        texture = new Texture("fondo.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, 0,0);
    }
}