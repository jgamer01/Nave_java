package com.jhernandeza.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Disparo {
    static Texture texture = new Texture("bala.png");
    float x, y, w, h, v;

    Disparo(float xNave, float yNave) {
        w = 8 * 6;
        h = 3 * 10;
        x = xNave;
        y = yNave-h/2;
        v = 12;
    }

    void update() {
        x += v;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, w, h);
    }
}