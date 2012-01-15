package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.resources.ResourceManager;

public class SuperMidPointTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;
	Injector injector;
	ResourceManager<String> resourceManager;

	public static class MidPointScript extends ScriptJavaImpl {
		
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

			if (particles.size() < 2) {
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

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.None) //
						.maskBits(Collisions.None) //
						.sensor() //
						.circleShape(0.5f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

		entity.addComponent(new ScriptComponent(injector.getInstance(MidPointScript.class)));
	}

}
