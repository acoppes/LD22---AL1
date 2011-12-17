package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.games.ludumdare.al1.Components;

public class FollowMouseMovementScript extends ScriptJavaImpl {

	private final Vector2 position = new Vector2();

	@Override
	public void update(World world, Entity e) {
		int x = Gdx.input.getX();
		int y = Gdx.graphics.getHeight() - Gdx.input.getY();

		CameraComponent cameraComponent = Components.getCameraComponent(e);

		position.set(x, y);

		cameraComponent.getLibgdx2dCamera().unproject(position);

		SpatialComponent spatialComponent = Components.getSpatialComponent(e);

		spatialComponent.getSpatial().setPosition(position.x, position.y);

	}

}