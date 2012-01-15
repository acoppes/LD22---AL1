package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class FollowMouseMovementScript2 extends ScriptJavaImpl {

	private final Vector2 direction = new Vector2();

	@Override
	public void update(World world, Entity e) {
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();
		
		Controller controller = Components.getControllerComponent(e).controller;
		controller.direction.set(0,0);
		
		if (!Gdx.input.isTouched())
			return;

		Spatial spatial = Components.getSpatialComponent(e).getSpatial();
		CameraComponent cameraComponent = Components.getCameraComponent(e);

		direction.set(x, y);
		cameraComponent.getLibgdx2dCamera().unproject(direction);

		direction.sub(spatial.getX(), spatial.getY());

		if (direction.len() < 0.5f)
			direction.set(0f, 0f);
		else
			direction.nor();

		controller.direction.set(direction);
	}

}