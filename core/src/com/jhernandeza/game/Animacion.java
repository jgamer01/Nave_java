package com.jhernandeza.game;

import com.badlogic.gdx.graphics.Texture;

import java.awt.*;

public class Animacion {
    Texture[] textures;
    int duracion;

    Animacion(int duracion, Texture... textures){
        this.textures = textures;
        this.duracion = duracion;
    }

    Texture ObtenerFrame(){
        int anim = Temporizador.framesJuego/duracion%textures.length;
        return textures[anim];
    }
}
