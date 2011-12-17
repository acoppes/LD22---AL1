package com.gemserk.games.ludumdare.al1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gemserk.commons.gdx.GameStateImpl;
import com.gemserk.commons.gdx.graphics.SpriteBatchUtils;
import com.gemserk.commons.gdx.screens.transitions.TransitionBuilder;
import com.gemserk.commons.reflection.Injector;

public class GameOverGameState extends GameStateImpl {

	Injector injector;
	Game game;

	SpriteBatch spriteBatch;
	BitmapFont font;

	@Override
	public void init() {

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();

	}

	@Override
	public void update() {

		if (Gdx.input.justTouched()) {
			new TransitionBuilder(game, game.playScreen) //
					.disposeCurrent() //
					.start();
		}

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.begin();
		SpriteBatchUtils.drawMultilineTextCentered(spriteBatch, font, "Game over, touch to play again", Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		spriteBatch.end();
	}

}
