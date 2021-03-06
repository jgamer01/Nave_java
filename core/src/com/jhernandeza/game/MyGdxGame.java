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

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	Fondo fondo;
	Jugador jugador;
	List<Enemigo> enemigos;
	List<Enemigo2> enemigos2;
	List<Disparo> disparosAEliminar;
	List<Enemigo> enemigosAEliminar;
	List<Enemigo2> enemigos2AEliminar;
	List<Corazon> corazones;
	Temporizador temporizadorNuevoEnemigo;
	Temporizador temporizadorNuevoEnemigo2;
	ScoreBoard scoreboard;
	boolean gameover;
	float tiempo = 0f;
	Corazon c1,c2,c3;


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
		enemigos2 = new ArrayList<>();
		temporizadorNuevoEnemigo = new Temporizador(120);
		temporizadorNuevoEnemigo2 = new Temporizador(120);
		disparosAEliminar = new ArrayList<>();
		enemigosAEliminar = new ArrayList<>();
		enemigos2AEliminar = new ArrayList<>();
		corazones = new ArrayList<>();
		scoreboard = new ScoreBoard();
		gameover = false;

		c1 = new Corazon(535, 400);
		corazones.add(c1);
		c2 = new Corazon(485, 400);
		corazones.add(c2);
		c3 = new Corazon(435, 400);
		corazones.add(c3);
	}

	void update() {
		Temporizador.framesJuego += 1;

		if (temporizadorNuevoEnemigo.suena()) enemigos.add(new Enemigo());
		if (temporizadorNuevoEnemigo2.suena()) enemigos2.add(new Enemigo2());


		if(!gameover) jugador.update();

		for (Enemigo enemigo : enemigos) enemigo.update();              // enemigos.forEach(Enemigo::update);
		for (Enemigo2 enemigo2 : enemigos2) enemigo2.update();

		for (Enemigo enemigo : enemigos) {
			for (Disparo disparo : jugador.disparos) {
				if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo.x, enemigo.y, enemigo.w, enemigo.h)) {
					disparosAEliminar.add(disparo);
					enemigosAEliminar.add(enemigo);
					jugador.puntos++;
				}
			}

			if (!gameover && !jugador.muerto && Utils.solapan(enemigo.x, enemigo.y, enemigo.w, enemigo.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
				jugador.morir();
				if (jugador.vidas == 2) {
					corazones.remove(2);
				} else if (jugador.vidas == 1) {
					corazones.remove(1);
				} else if (jugador.vidas == 0) {
					corazones.remove(0);
					gameover = true;
				}
			}

			if (enemigo.x < -enemigo.w) enemigosAEliminar.add(enemigo);
		}

		for (Enemigo2 enemigo2 : enemigos2) {
			for (Disparo disparo : jugador.disparos) {
				if (Utils.solapan(disparo.x, disparo.y, disparo.w, disparo.h, enemigo2.x, enemigo2.y, enemigo2.w, enemigo2.h)) {
					disparosAEliminar.add(disparo);
					enemigos2AEliminar.add(enemigo2);
					jugador.puntos++;
				}
			}

			if (!gameover && !jugador.muerto && Utils.solapan(enemigo2.x, enemigo2.y, enemigo2.w, enemigo2.h, jugador.x, jugador.y, jugador.w, jugador.h)) {
				jugador.morir();
				if (jugador.vidas == 2) {
					corazones.remove(2);
				} else if (jugador.vidas == 1) {
					corazones.remove(1);
				} else if (jugador.vidas == 0) {
					corazones.remove(0);
					gameover = true;
				}
			}

			if (enemigo2.x < -enemigo2.w) enemigos2AEliminar.add(enemigo2);
		}


		for (Disparo disparo : jugador.disparos)
			if (disparo.x > 640)
				disparosAEliminar.add(disparo);

		for (Disparo disparo : disparosAEliminar) jugador.disparos.remove(disparo);       // disparosAEliminar.forEach(disparo -> jugador.disparos.remove(disparo));
		for (Enemigo2 enemigo2 : enemigos2AEliminar) enemigos2.remove(enemigo2);               // enemigosAEliminar.forEach(enemigo -> enemigos.remove(enemigo));
		for (Enemigo enemigo : enemigosAEliminar) enemigos.remove(enemigo);
		disparosAEliminar.clear();
		enemigos2AEliminar.clear();
		enemigosAEliminar.clear();
		if (!gameover) {
			tiempo += 0.01f;
		}
		if (tiempo >= 1f) {
			jugador.puntostiempo++;
			tiempo=0f;
		}

		if(gameover) {
			int result = scoreboard.update(jugador.puntos);
			scoreboard.pasarTiempo(jugador.puntostiempo);
			if(result == 1) {
				tiempo = 0f;
				jugador.puntostiempo = 0;
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
		for (Enemigo2 enemigo2 : enemigos2) enemigo2.render(batch);

		for (Corazon corazon : corazones) corazon.render(batch);


		font.draw(batch, "" + jugador.vidas, 590, 440);
		font.draw(batch, "" + jugador.puntos, 30, 440);
		font.draw(batch, "" + jugador.puntostiempo, 30, 50);
		if (gameover){
			scoreboard.Render(batch, font);
		}
		batch.end();
	}
}