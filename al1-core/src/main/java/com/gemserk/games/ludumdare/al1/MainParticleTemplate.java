package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.TagComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;

public class MainParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;

	public static class MovementScript extends ScriptJavaImpl {

		private final Vector2 position = new Vector2();

		Libgdx2dCamera camera;

		public MovementScript(Libgdx2dCamera camera) {
			this.camera = camera;
		}

		@Override
		public void update(World world, Entity e) {
			int x = Gdx.input.getX();
			int y = Gdx.graphics.getHeight() - Gdx.input.getY();

			if (!Gdx.input.isTouched())
				return;

			position.set(x, y);

			camera.unproject(position);

			SpatialComponent spatialComponent = Components.getSpatialComponent(e);

			spatialComponent.getSpatial().setPosition(position.x, position.y);

			// PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
		}

	}

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.circleShape(0.5f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.mass(10000f).build();

		entity.addComponent(new TagComponent(Tags.MainCharacter));
		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

		Libgdx2dCamera camera = parameters.get("camera");
		entity.addComponent(new ScriptComponent(new MovementScript(camera)));

		// entity.addComponent(new ControllerComponent(new Controller()));

	}

}
