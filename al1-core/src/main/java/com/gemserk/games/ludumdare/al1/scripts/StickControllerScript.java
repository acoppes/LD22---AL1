package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.componentsengine.input.ButtonMonitor;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class StickControllerScript extends ScriptJavaImpl {

	private final Vector2 tmp = new Vector2();
	private final Input input;
	
	int touch = 0;

	private boolean moving = false;
	private ButtonMonitor pointerDownMonitor;

	public StickControllerScript(Input input) {
		this.input = input;
		
		pointerDownMonitor = LibgdxInputMappingBuilder.pointerDownButtonMonitor(input, 0);
	}

	@Override
	public void update(World world, Entity e) {
		pointerDownMonitor.update();
		
		int x = input.getX();
		int y = Gdx.graphics.getHeight() - input.getY();

		tmp.set(0f, 0f);

		Controller controller = Components.getControllerComponent(e).controller;
		
		if (pointerDownMonitor.isPressed()) {

			tmp.set(100, 100);
			tmp.sub(x, y);

			if (tmp.len() < 80f) {
				moving = true;
			} else {
				tmp.set(0,0);
			}
			
		}
		
		if (pointerDownMonitor.isReleased())
			moving = false;

		// if (!input.isTouched())
		// moving = false;

		if (moving) {

			tmp.set(100, 100);
			tmp.sub(x, y);

			if (tmp.len() > 40f) {
				tmp.nor();
				tmp.mul(40f);
			}

			tmp.mul(-1f * 0.1f * 0.5f);

		}

		controller.direction.set(tmp);
	}

}