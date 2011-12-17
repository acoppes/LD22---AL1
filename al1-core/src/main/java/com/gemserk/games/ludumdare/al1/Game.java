package com.gemserk.games.ludumdare.al1;

import com.gemserk.commons.gdx.GameState;
import com.gemserk.commons.gdx.Screen;
import com.gemserk.commons.gdx.ScreenImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.reflection.InjectorImpl;

public class Game extends com.gemserk.commons.gdx.Game {

	Screen playScreen;

	@Override
	public void create() {
		super.create();

		Injector injector = new InjectorImpl();

		GameState playGameState = injector.getInstance(PlayGameState.class);

		playScreen = new ScreenImpl(playGameState);
		
		setScreen(playScreen);
	}

}
