package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.GameResources;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.GameResources.Sprites;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.ShieldComponent;
import com.gemserk.games.ludumdare.al1.scripts.ExplodeWhenCollideScript;
import com.gemserk.games.ludumdare.al1.scripts.FollowMouseMovementScript;
import com.gemserk.resources.ResourceManager;

public class MainParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	public static class ShieldScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {
			ShieldComponent shieldComponent = Components.getShieldComponent(e);
			shieldComponent.enabled = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
		}

	}

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

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Al1);
		entity.addComponent(new SpriteComponent(sprite));
		entity.addComponent(new RenderableComponent(0));

		Libgdx2dCamera camera = parameters.get("camera");

		entity.addComponent(new CameraComponent(camera, new CameraImpl(0f, 0f, 1f, 0f)));

		entity.addComponent(new ScriptComponent(injector.getInstance(FollowMouseMovementScript.class), //
				injector.getInstance(ExplodeWhenCollideScript.class)
		));

		// entity.addComponent(new ControllerComponent(new Controller()));
		// entity.addComponent(new ShieldComponent(new Container(100f, 100f)));

	}

}