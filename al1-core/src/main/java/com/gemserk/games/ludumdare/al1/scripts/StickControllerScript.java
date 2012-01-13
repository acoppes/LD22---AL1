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

	public Vector2 stickPosition = new Vector2();
	public boolean moving = false;
	public float radius = 80f;

	private ButtonMonitor pointerDownMonitor;

	public StickControllerScript(Input input) {
		this.input = input;
		pointerDownMonitor = LibgdxInputMappingBuilder.pointerDownButtonMonitor(input, 0);
		stickPosition.set(100, 100);
	}

	@Override
	public void update(World world, Entity e) {
		pointerDownMonitor.update();

		int x = input.getX();
		int y = Gdx.graphics.getHeight() - input.getY();

		tmp.set(0f, 0f);

		Controller controller = Components.getControllerComponent(e).controller;

		if (pointerDownMonitor.isPressed()) {
			stickPosition.set(x, y);
			moving = true;
		}

		if (pointerDownMonitor.isReleased())
			moving = false;

		if (moving) {

			tmp.set(stickPosition.x, stickPosition.y);
			tmp.sub(x, y);

			if (tmp.len() > radius * 0.5f) {
				tmp.nor();
				tmp.mul(radius * 0.5f);
			}

			tmp.mul(-1f * 0.1f * 0.5f);

		}

		controller.direction.set(tmp);
	}

}