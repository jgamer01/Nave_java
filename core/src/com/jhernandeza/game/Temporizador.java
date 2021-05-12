package com.jhernandeza.game;

public class Temporizador {
        static int framesJuego;
        int alarma;
        int frecuencia;
        boolean repetir = true;
        boolean activo = true;

        Temporizador(int f){
            frecuencia = f;
            alarma = framesJuego + f;
        }

        Temporizador(int f, boolean r){
            frecuencia = f;
            alarma = framesJuego + f;
            repetir = r;
        }

        public boolean suena() {
            if(activo) {
                if (framesJuego == alarma) {
                    alarma = framesJuego + frecuencia;
                    if(!repetir) {
                        activo = false;
                    }
                    return true;
                }
            }
            return false;
        }

        public void activar() {
            activo = true;
            alarma = framesJuego + frecuencia;
        }
    }
