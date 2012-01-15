package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.GroupComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.artemis.utils.PhysicsUtils;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.gdx.box2d.Contacts.Contact;
import com.gemserk.commons.gdx.games.Physics;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.Events;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.StoreComponent;
import com.gemserk.resources.ResourceManager;

public class ParticlesCenterTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	public static class UpdatePositionScript extends ScriptJavaImpl {

		private final Vector2 position = new Vector2();

		@Override
		public void init(World world, Entity e) {
			super.init(world, e);
			PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
			physicsComponent.getPhysics().getBody().setActive(false);
		}

		@Override
		public void update(World world, Entity e) {
			ImmutableBag<Entity> particles = world.getGroupManager().getEntities(Groups.EnemyCharacter);
			Body body = Components.getPhysicsComponent(e).getPhysics().getBody();

			if (particles.size() < 3) {
				body.setTransform(0, 0, 0);
				body.setActive(false);
				return;
			}

			body.setActive(true);

			position.set(0f, 0f);

			for (int i = 0; i < particles.size(); i++) {
				Entity particle = particles.get(i);
				Spatial spatial = Components.getSpatialComponent(particle).getSpatial();

				position.add(spatial.getX(), spatial.getY());
			}

			position.mul(1f / particles.size());

			body.setTransform(position, 0f);
		}

	}

	public static class KillParticlesOnMainParticleContactScript extends ScriptJavaImpl {

		EventManager eventManager;

		@Override
		public void update(World world, Entity e) {

			Physics physics = Components.getPhysicsComponent(e).getPhysics();
			Body body = physics.getBody();
			if (!body.isActive())
				return;

			Contacts contacts = physics.getContact();
			if (!contacts.isInContact())
				return;

			for (int i = 0; i < contacts.getContactCount(); i++) {
				Contact contact = contacts.getContact(i);
				Entity otherEntity = (Entity) contact.getOtherFixture().getBody().getUserData();

				if (otherEntity == null)
					continue;

				// assuming the other entity is the main particle because the collision flags...

				// kill all particles

				ImmutableBag<Entity> particles = world.getGroupManager().getEntities(Groups.EnemyCharacter);
				eventManager.registerEvent(Events.ParticlesDestroyed, particles);
				
				for (int j = 0; j < particles.size(); j++) {
					Entity particle = particles.get(j);
					StoreComponent storeComponent = Components.getStoreComponent(particle);
					storeComponent.store.free(particle);
				}

			}

			PhysicsUtils.releaseContacts(contacts);

		}

	}

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.Main) //
						.sensor() //
						.circleShape(0.25f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

		entity.addComponent(new GroupComponent(Groups.ParticlesCenter));

		entity.addComponent(new ScriptComponent( //
				injector.getInstance(KillParticlesOnMainParticleContactScript.class), //
				injector.getInstance(UpdatePositionScript.class) //
		));
	}

}
