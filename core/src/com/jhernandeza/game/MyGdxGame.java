package com.jhernandeza.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	Fondo fondo;
	Jugador jugador;
	List<Enemigo> enemigos;
	List<Disparo> disparosAEliminar;
	List<Enemigo> enemigosAEliminar;
	Temporizador temporizadorNuevoEnemigo;
	ScoreBoard scoreboard;
	boolean gameover;
	float tiempo = 0f;


	@Override
	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		font.getData().setScale(2f);

		inicializarJuego();
	}

	void inicializarJuego(){
		fondo = new Fondo();
		jugador = new Jugador();
		enemigos = new ArrayList<>();
		temporizadorNuevoEnemigo = new Temporizador(120);
		disparosAEliminar = new ArrayList<>();
		enemigosAEliminar = new ArrayList<>();
		scoreboard = new ScoreBoard();

		gameover = false;
	}

	void update() {
		Temporizador.framesJuego += 1;

		if (temporizadorNuevoEnemigo.suena()) enemigos.add(new Enemigo());

		if(!gameover) jugador.update();

		for (Enemigo enemigo : enemigos) enemigo.update();              // enemigos.forEach(Enemigo::update);

		for (Enemigo enemigo : enemigos) {
			for (Disparo disparo : jugador.disparos) {
				if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo.x, enemigo.y, enemigo.w, enemigo.h)) {
					disparosAEliminar.add(disparo);
					enemigosAEliminar.add(enemigo);
					jugador.puntos++;
					break;
				}
			}

			if (!gameover && !jugador.muerto && Utils.solapan(enemigo.x, enemigo.y, enemigo.w, enemigo.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
				jugador.morir();
				if (jugador.vidas == 0){
					gameover = true;
				}
			}

			if (enemigo.x < -enemigo.w) enemigosAEliminar.add(enemigo);
		}

		for (Disparo disparo : jugador.disparos)
			if (disparo.x > 640)
				disparosAEliminar.add(disparo);

		for (Disparo disparo : disparosAEliminar) jugador.disparos.remove(disparo);       // disparosAEliminar.forEach(disparo -> jugador.disparos.remove(disparo));
		for (Enemigo enemigo : enemigosAEliminar) enemigos.remove(enemigo);               // enemigosAEliminar.forEach(enemigo -> enemigos.remove(enemigo));
		disparosAEliminar.clear();
		enemigosAEliminar.clear();

		tiempo += 0.01f;
		if (tiempo >= 1f) {
			jugador.puntostiempo++;
			tiempo=0f;
		}

		if(gameover) {
			int result = scoreboard.update(jugador.puntos);
			scoreboard.pasarTiempo(jugador.puntostiempo);
			if(result == 1) {
				inicializarJuego();
			} else if (result == 2) {
				Gdx.app.exit();
			}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();

		batch.begin();
		fondo.render(batch);
		jugador.render(batch);
		for (Enemigo enemigo : enemigos) enemigo.render(batch);  // enemigos.forEach(e -> e.render(batch));
		font.draw(batch, "" + jugador.vidas, 590, 440);
		font.draw(batch, "" + jugador.puntos, 30, 440);
		font.draw(batch, "" + jugador.puntostiempo, 30, 50);
		if (gameover){
			scoreboard.Render(batch, font);
		}
		batch.end();
	}
}