package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class StickControllerScript extends ScriptJavaImpl {

	private final Vector2 tmp = new Vector2();
	private final Input input;
	
	public StickControllerScript(Input input) {
		this.input = input;
	}

	@Override
	public void update(World world, Entity e) {
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		tmp.set(0f, 0f);

		Controller controller = Components.getControllerComponent(e).controller;
		
		if (input.isTouched()) {
			tmp.set(100, 100);
			tmp.sub(x, y);
			
			if (tmp.len() > 25f) {
				tmp.nor();
				tmp.mul(25f);
			}
			
			tmp.mul(-1f * 0.1f);
			
			
		}

		controller.direction.set(tmp);
	}

}