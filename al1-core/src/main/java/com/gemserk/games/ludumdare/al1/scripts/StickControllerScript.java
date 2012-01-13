package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.componentsengine.input.AnalogInputMonitor;
import com.gemserk.componentsengine.input.ButtonMonitor;
import com.gemserk.componentsengine.input.LibgdxInputMappingBuilder;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class StickControllerScript extends ScriptJavaImpl {

	private final Vector2 tmp = new Vector2();
	private final Vector2 newPosition = new Vector2();

	int touch = 0;

	public Vector2 stickPosition = new Vector2();
	public boolean moving = false;
	public float radius = 80f;

	private ButtonMonitor pointerDownMonitor;
	private AnalogInputMonitor pointerXCoordinateMonitor;
	private AnalogInputMonitor pointerYCoordinateMonitor;

	public StickControllerScript(Input input) {
		pointerDownMonitor = LibgdxInputMappingBuilder.anyPointerButtonMonitor(input);
		
		pointerXCoordinateMonitor = LibgdxInputMappingBuilder.anyPointerXCoordinateMonitor(input);
		pointerYCoordinateMonitor = LibgdxInputMappingBuilder.anyPointerYCoordinateMonitor(input);

		stickPosition.set(100, 100);
	}

	@Override
	public void update(World world, Entity e) {
		pointerDownMonitor.update();
		
		pointerXCoordinateMonitor.update();
		pointerYCoordinateMonitor.update();

		float x = pointerXCoordinateMonitor.getValue();
		float y = Gdx.graphics.getHeight() - pointerYCoordinateMonitor.getValue();

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

			if (tmp.len() > radius) {

				newPosition.set(tmp);
				newPosition.nor().mul(radius);

				newPosition.sub(tmp);
				newPosition.add(stickPosition);

				stickPosition.set(newPosition);
			}

			if (tmp.len() > radius * 0.75f) {
				tmp.nor();
				tmp.mul(radius * 0.75f);
			}

			tmp.mul(-1f * 0.1f * 0.2f);

		}

		controller.direction.set(tmp);
	}

}