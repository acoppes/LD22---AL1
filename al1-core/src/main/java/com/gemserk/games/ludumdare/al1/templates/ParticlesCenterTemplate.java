package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.gemserk.commons.gdx.games.Physics;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.gdx.graphics.ConvexHull2d;
import com.gemserk.commons.gdx.graphics.ConvexHull2dImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.Events;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.components.BombBuildComponent;
import com.gemserk.games.ludumdare.al1.components.BombBuildComponent.BombBuildState;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.ConvexHullComponent;
import com.gemserk.games.ludumdare.al1.components.RenderScriptComponent;
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

	public static class KillParticlesOnMainParticleContactScript extends ScriptJavaImpl {

		EventManager eventManager;

		@Override
		public void update(World world, Entity e) {

			BombBuildComponent bombBuildComponent = Components.getBombBuildComponent(e);
			if (!bombBuildComponent.shouldExplode)
				return;

			bombBuildComponent.shouldExplode = false;

			ImmutableBag<Entity> particles = world.getGroupManager().getEntities(Groups.EnemyCharacter);
			eventManager.registerEvent(Events.ParticlesDestroyed, particles);

			for (int j = 0; j < particles.size(); j++) {
				Entity particle = particles.get(j);
				StoreComponent storeComponent = Components.getStoreComponent(particle);
				storeComponent.store.free(particle);
			}

		}

	}
	
	public static class RecalculateConvexHullScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {
			ConvexHullComponent convexHullComponent = Components.getConvexHullComponent(e);
			ConvexHull2d convexHull2d = convexHullComponent.convexHull2d;

			ImmutableBag<Entity> particles = world.getGroupManager().getEntities(Groups.EnemyCharacter);

			for (int i = 0; i < particles.size(); i++) {
				Entity particle = particles.get(i);
				Spatial spatial = Components.getSpatialComponent(particle).getSpatial();
				convexHull2d.add(spatial.getX(), spatial.getY());
			}

			BombBuildComponent bombBuildComponent = Components.getBombBuildComponent(e);

			if (!convexHull2d.recalculate()) {
				bombBuildComponent.state = BombBuildState.None;
				return;
			}

			Entity mainCharacter = world.getTagManager().getEntity(Tags.MainCharacter);

			if (mainCharacter == null)
				return;

			Spatial spatial = Components.getSpatialComponent(mainCharacter).getSpatial();

			if (bombBuildComponent.state == BombBuildState.None) {

				if (convexHull2d.inside(spatial.getX(), spatial.getY())) {
					bombBuildComponent.state = BombBuildState.Inside;
					System.out.println("inside");
				}

			} else if (bombBuildComponent.state == BombBuildState.Inside) {

				if (!convexHull2d.inside(spatial.getX(), spatial.getY())) {
					bombBuildComponent.state = BombBuildState.None;
					bombBuildComponent.shouldExplode = false;
					System.out.println("outside");
				} else {

					Physics physics = Components.getPhysicsComponent(e).getPhysics();

					Contacts contacts = physics.getContact();
					if (contacts.isInContact()) {
						bombBuildComponent.state = BombBuildState.TouchedCenter;
						PhysicsUtils.releaseContacts(contacts);
						System.out.println("touched center");
					}

				}

			} else if (bombBuildComponent.state == BombBuildState.TouchedCenter) {

				if (!convexHull2d.inside(spatial.getX(), spatial.getY())) {
					bombBuildComponent.state = BombBuildState.None;
					bombBuildComponent.shouldExplode = true;
					System.out.println("outside, should explode!!");
				}

			}

		}

	}

	public static class RenderCenterScript extends ScriptJavaImpl {

		ShapeRenderer shapeRenderer;

		@Override
		public void update(World world, Entity e) {
			Physics physics = Components.getPhysicsComponent(e).getPhysics();
			Body body = physics.getBody();

			if (!body.isActive())
				return;

			BombBuildComponent bombBuildComponent = Components.getBombBuildComponent(e);

			Spatial spatial = Components.getSpatialComponent(e).getSpatial();

			shapeRenderer.setColor(1f, 1f, 0f, 1f);
			
			shapeRenderer.begin(ShapeType.FilledCircle);
			shapeRenderer.filledCircle(spatial.getX(), spatial.getY(), 0.1f, 20);
			shapeRenderer.end();

			ConvexHullComponent convexHullComponent = Components.getConvexHullComponent(e);
			ConvexHull2d convexHull2d = convexHullComponent.convexHull2d;

			if (convexHull2d.getPointsCount() < 3)
				return;

			if (bombBuildComponent.state != BombBuildState.TouchedCenter)
				return;
			
			// if (bombBuildComponent.state == BombBuildState.TouchedCenter)
			// shapeRenderer.setColor(1f, 0f, 0f, 1f);
			// else
			// shapeRenderer.setColor(0f, 0f, 1f, 1f);
			
			shapeRenderer.setColor(1f, 0f, 0f, 1f);
			
			shapeRenderer.begin(ShapeType.Line);
			for (int i = 0; i < convexHull2d.getPointsCount(); i++) {
				float x0 = convexHull2d.getX(i);
				float y0 = convexHull2d.getY(i);
				if (i + 1 == convexHull2d.getPointsCount()) {
					float x1 = convexHull2d.getX(0);
					float y1 = convexHull2d.getY(0);
					shapeRenderer.line(x0, y0, x1, y1);
					break;
				}
				float x1 = convexHull2d.getX(i + 1);
				float y1 = convexHull2d.getY(i + 1);
				shapeRenderer.line(x0, y0, x1, y1);
			}
			shapeRenderer.end();

		}

	}

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.Main) //
						.sensor() //
						.circleShape(0.1f)) //
				.type(BodyType.DynamicBody) //
				.position(0f, 0f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

		entity.addComponent(new GroupComponent(Groups.ParticlesCenter));

		entity.addComponent(new ScriptComponent( //
				injector.getInstance(KillParticlesOnMainParticleContactScript.class), //
				injector.getInstance(UpdatePositionScript.class), //
				injector.getInstance(RecalculateConvexHullScript.class) //
		));

		entity.addComponent(new RenderScriptComponent(injector.getInstance(RenderCenterScript.class)));
		entity.addComponent(new ConvexHullComponent(new ConvexHull2dImpl(10)));

		entity.addComponent(new BombBuildComponent(BombBuildState.None));
	}

}
