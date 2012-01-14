package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.interpolator.function.InterpolationFunction;
import com.gemserk.animation4j.interpolator.function.InterpolationFunctions;
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

	final InterpolationFunction interpolationFunction = InterpolationFunctions.easeOut();

	public final Vector2 stickPosition = new Vector2();
	public final Vector2 touchPosition = new Vector2();

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

		touchPosition.set(pointerXCoordinateMonitor.getValue(), //
				Gdx.graphics.getHeight() - pointerYCoordinateMonitor.getValue());

		tmp.set(0f, 0f);

		Controller controller = Components.getControllerComponent(e).controller;
		controller.direction.set(0, 0);

		if (pointerDownMonitor.isPressed()) {
			stickPosition.set(touchPosition.x, touchPosition.y);
			moving = true;
		}

		if (pointerDownMonitor.isReleased())
			moving = false;

		if (!moving)
			return;

		tmp.set(stickPosition.x, stickPosition.y);
		tmp.sub(touchPosition.x, touchPosition.y);

		if (tmp.len() > radius) {

			newPosition.set(tmp);
			newPosition.nor().mul(radius);

			newPosition.sub(tmp);
			newPosition.add(stickPosition);

			stickPosition.set(newPosition);

			tmp.nor().mul(radius);
		}

		float t = tmp.len() / radius;
		float v = interpolationFunction.interpolate(t);

		tmp.nor().mul(-v);

		if (stickPosition.x + radius > Gdx.graphics.getWidth())
			stickPosition.x = Gdx.graphics.getWidth() - radius;

		if (stickPosition.x - radius < 0)
			stickPosition.x = 0 + radius;

		if (stickPosition.y + radius > Gdx.graphics.getHeight())
			stickPosition.y = Gdx.graphics.getHeight() - radius;

		if (stickPosition.y - radius < 0)
			stickPosition.y = 0 + radius;

		controller.direction.set(tmp);
	}

}