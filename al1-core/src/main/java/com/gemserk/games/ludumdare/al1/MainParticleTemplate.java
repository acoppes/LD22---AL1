package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.components.TagComponent;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.scripts.ExplodeWhenCollideScript;
import com.gemserk.games.ludumdare.al1.scripts.FollowMouseMovementScript;
import com.gemserk.resources.ResourceManager;

public class MainParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Main) //
						.maskBits(Collisions.All) //
						.circleShape(0.5f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.mass(10f) //
				.userData(entity) //
				.build();


		entity.addComponent(new TagComponent(Tags.MainCharacter));
		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));
		
		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.MainParticle);
		entity.addComponent(new SpriteComponent(sprite));
		entity.addComponent(new RenderableComponent(0));

		Libgdx2dCamera camera = parameters.get("camera");

		entity.addComponent(new CameraComponent(camera, new CameraImpl(0f, 0f, 48f, 0f)));

		FollowMouseMovementScript followMouseMovementScript = injector.getInstance(FollowMouseMovementScript.class);
		ExplodeWhenCollideScript explodeWhenCollideScript = injector.getInstance(ExplodeWhenCollideScript.class);

		entity.addComponent(new ScriptComponent(followMouseMovementScript, explodeWhenCollideScript));

		// entity.addComponent(new ControllerComponent(new Controller()));

	}

}
