package com.gemserk.games.ludumdare.al1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.converters.Converters;
import com.gemserk.animation4j.gdx.converters.LibgdxConverters;
import com.gemserk.commons.gdx.GameState;
import com.gemserk.commons.gdx.Screen;
import com.gemserk.commons.gdx.ScreenImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.reflection.InjectorImpl;

public class Game extends com.gemserk.commons.gdx.Game {

	public Screen playScreen;
	public Screen gameOverScreen;

	@Override
	public void create() {
		super.create();
		
		Converters.register(Color.class, LibgdxConverters.color());
		Converters.register(Vector2.class, LibgdxConverters.vector2());

		Injector injector = new InjectorImpl();
		
		injector.bind("game", this);

		GameState playGameState = injector.getInstance(PlayGameState.class);
		GameState gameOverState = injector.getInstance(GameOverGameState.class);

		playScreen = new ScreenImpl(playGameState);
		gameOverScreen = new ScreenImpl(gameOverState);
		
		setScreen(playScreen);
	}

}
