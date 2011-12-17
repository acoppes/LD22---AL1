package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.CameraComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.gdx.box2d.Contacts.Contact;
import com.gemserk.commons.gdx.camera.CameraImpl;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.scripts.FollowMouseMovementScript;
import com.gemserk.resources.ResourceManager;

public class ShieldTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	public static class RenderWhenShieldEnabledScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {

			Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

			if (target == null)
				return;

			ShieldComponent shieldComponent = Components.getShieldComponent(target);

			RenderableComponent renderableComponent = Components.getRenderableComponent(e);
			renderableComponent.setVisible(shieldComponent.enabled);

		}

	}

	public static class RemoveCollidingEnemiesScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {
			
			Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

			if (target == null)
				return;
			
			ShieldComponent shieldComponent = Components.getShieldComponent(target);
			if (!shieldComponent.enabled)
				return;

			PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
			Contacts contacts = physicsComponent.getContact();

			if (!contacts.isInContact())
				return;

			for (int i = 0; i < contacts.getContactCount(); i++) {

				Contact contact = contacts.getContact(i);
				Entity enemy = (Entity) contact.getOtherFixture().getBody().getUserData();

				enemy.delete();

			}

		}

	}

	@Override
	public void apply(Entity entity) {
		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Shield) //
						.maskBits(Collisions.Enemy) //
						.sensor() //
						.circleShape(1f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 2f, 2f)));

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Shield);
		entity.addComponent(new SpriteComponent(sprite));
		entity.addComponent(new RenderableComponent(5, false));

		Libgdx2dCamera camera = parameters.get("camera");

		entity.addComponent(new CameraComponent(camera, new CameraImpl(0f, 0f, 1f, 0f)));

		FollowMouseMovementScript followMouseMovementScript = injector.getInstance(FollowMouseMovementScript.class);

		entity.addComponent(new ScriptComponent(followMouseMovementScript, //
				injector.getInstance(RenderWhenShieldEnabledScript.class) //
//				injector.getInstance(RemoveCollidingEnemiesScript.class) //
		));
	}

}
