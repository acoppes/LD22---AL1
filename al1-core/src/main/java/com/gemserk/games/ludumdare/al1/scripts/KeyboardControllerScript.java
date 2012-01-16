package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class KeyboardControllerScript extends ScriptJavaImpl {

	private final Vector2 direction = new Vector2();
	
	@Override
	public void init(World world, Entity e) {
		
	}

	@Override
	public void update(World world, Entity e) {
		Controller controller = Components.getControllerComponent(e).controller;
		controller.direction.set(0,0);
		
		direction.set(0,0);

		if (Gdx.input.isKeyPressed(Keys.LEFT)) 
			direction.x += -1f;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) 
			direction.x += 1f;
		if (Gdx.input.isKeyPressed(Keys.UP)) 
			direction.y += 1f;
		if (Gdx.input.isKeyPressed(Keys.DOWN)) 
			direction.y += -1f;
		
		direction.nor();

		controller.direction.set(direction);
	}

}