package com.jhernandeza.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClassJhernandeza extends ApplicationAdapter {

	static Random random = new Random();
	SpriteBatch batch;
	BitmapFont bitmapFont;
	Fondo fondo;
	Nave nave;
	List<Alien> aliens;
	List<Disparo> DisparosAEliminar = new ArrayList<>();
	List<Alien> AliensAEliminar = new ArrayList<>();
	Temporizador temporizadorNuevoAlien = new Temporizador(120);

	@Override
	public void create() {
		batch = new SpriteBatch();
		bitmapFont = new BitmapFont();
		aliens = new ArrayList<>();
		aliens.add(new Alien());


		fondo = new Fondo();
		nave = new Nave();




		temporizadorNuevoAlien = new Temporizador(120);
	}

	void update() {
		Temporizador.framesJuego += 1;
		if (temporizadorNuevoAlien.suena()) aliens.add(new Alien());

		nave.update();
		;

		for (Alien alien : aliens) {
			for (Disparo disparo : nave.disparos) {
				if (Utils.solapan(alien.x, alien.y, alien.w, alien.h, alien.x, alien.y, alien.w, alien.h)) {
					DisparosAEliminar.add(disparo);
					AliensAEliminar.add(alien);
					nave.puntos++;
					break;
				}
			}
			if (!nave.muerto && Utils.solapan(alien.x, alien.y, alien.w, alien.h, nave.x, nave.y, nave.w, nave.h)) {
				nave.morir();
			}

			if (alien.x < alien.w) AliensAEliminar.add(alien);
		}
		for (Disparo disparo : nave.disparos)
			if (disparo.x > 640)
				DisparosAEliminar.add(disparo);

		for (Disparo disparo : DisparosAEliminar) nave.disparos.remove(disparo);
		for (Alien alien : AliensAEliminar) aliens.remove(alien);
		DisparosAEliminar.clear();
		AliensAEliminar.clear();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update();
		batch.begin();
		fondo.render(batch);
		nave.render(batch);
		for (Alien alien : aliens) alien.render(batch);
		bitmapFont.draw(batch, "" + nave.vidas, 590, 440);
		bitmapFont.draw(batch, "" + nave.puntos, 30, 440);
		batch.end();
	}
}