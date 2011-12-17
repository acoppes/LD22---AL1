package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.games.ludumdare.al1.Components;
import com.gemserk.games.ludumdare.al1.Tags;

public class FollowMainCharacterScript extends ScriptJavaImpl {

	private final Vector2 force = new Vector2();

	@Override
	public void update(World world, Entity e) {
		Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

		if (target == null)
			return;

		SpatialComponent spatialComponent = Components.getSpatialComponent(e);
		SpatialComponent targetSpatialComponent = Components.getSpatialComponent(target);

		Spatial spatial = spatialComponent.getSpatial();
		Spatial targetSpatial = targetSpatialComponent.getSpatial();

		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);

		force.set(spatial.getX(), spatial.getY());
		force.sub(targetSpatial.getX(), targetSpatial.getY());

		force.mul(-5f);

		physicsComponent.getBody().applyForceToCenter(force);
	}

}