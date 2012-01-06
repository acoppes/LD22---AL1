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

		Spatial spatial = Components.getSpatialComponent(e).getSpatial();
		Controller controller = Components.getControllerComponent(e).controller;
		CameraComponent cameraComponent = Components.getCameraComponent(e);

		direction.set(x, y);
		cameraComponent.getLibgdx2dCamera().unproject(direction);

		direction.sub(spatial.getX(), spatial.getY());
		// position.nor();

		if (direction.len() < 1f)
			direction.set(0f, 0f);

		controller.direction.set(direction);

		// SpatialComponent spatialComponent = Components.getSpatialComponent(e);
		// spatialComponent.getSpatial().setPosition(position.x, position.y);
	}

}