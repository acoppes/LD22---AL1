package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.components.LinearVelocityLimitComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.components.TagComponent;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.GameResources;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.components.ControllerComponent;
import com.gemserk.games.ludumdare.al1.scripts.ExplodeWhenCollideScript;
import com.gemserk.games.ludumdare.al1.scripts.KeyboardControllerScript;
import com.gemserk.games.ludumdare.al1.scripts.MovementScript;
import com.gemserk.games.ludumdare.al1.scripts.StickControllerScript;
import com.gemserk.resources.ResourceManager;

public class MainParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	Input remoteInput;

	// public static class ShieldScript extends ScriptJavaImpl {
	//
	// @Override
	// public void update(World world, Entity e) {
	// ShieldComponent shieldComponent = Components.getShieldComponent(e);
	// shieldComponent.enabled = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
	// }
	//
	// }

	// public static class TeleportScript extends ScriptJavaImpl {
	//
	// Rectangle bounds = new Rectangle(-8.5f, -5.5f, 17f, 11f);
	// Vector2 position = new Vector2();
	//
	// @Override
	// public void update(World world, Entity e) {
	// Spatial spatial = Components.getSpatialComponent(e).getSpatial();
	//
	// position.set(spatial.getX(), spatial.getY());
	//
	// MathUtils2.truncate(position, bounds);
	//
	// spatial.setPosition(position.x, position.y);
	// }
	//
	// }

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Main) //
						.maskBits(Collisions.All) //
						.circleShape(0.4f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.mass(1f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new LinearVelocityLimitComponent(1f * 6f));

		entity.addComponent(new TagComponent(Tags.MainCharacter));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Al1);
		entity.addComponent(new SpriteComponent(sprite));
		entity.addComponent(new RenderableComponent(0));

		Libgdx2dCamera camera = parameters.get("camera");

		entity.addComponent(new CameraComponent(camera, new CameraImpl(0f, 0f, 1f, 0f)));

		entity.addComponent(new ControllerComponent(new Controller()));

		Input input = remoteInput;

//		Script controllerScript = injector.getInstance(FollowMouseMovementScript2.class);
		Script controllerScript = injector.getInstance(KeyboardControllerScript.class);
		if (Gdx.app.getType() == ApplicationType.Android) {
			controllerScript = new StickControllerScript(input);
		}

		entity.addComponent(new ScriptComponent( //
				controllerScript, //
				injector.getInstance(ExplodeWhenCollideScript.class), //
				injector.getInstance(MovementScript.class) //
		));

	}

}
