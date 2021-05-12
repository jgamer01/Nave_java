package com.jhernandeza.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Disparo {
    static Texture texture = new Texture("bala.png");

    float x, y, w, h, v;

    Disparo(float xNave, float yNave){
        w = 70;
        h = 70;
        x = xNave-w/2;
        y = yNave*0.8f;
        v = 4;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }

    void update(){
        y += v;
    }
}
