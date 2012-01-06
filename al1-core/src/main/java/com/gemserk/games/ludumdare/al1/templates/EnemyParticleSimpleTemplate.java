package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.animation4j.gdx.converters.LibgdxConverters;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.components.LinearVelocityLimitComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.GameResources;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.components.AliveComponent;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.FollowRandomTargetComponent;
import com.gemserk.games.ludumdare.al1.scripts.AliveTimeScript;
import com.gemserk.games.ludumdare.al1.scripts.BounceWhenCollideScript;
import com.gemserk.resources.ResourceManager;

public class EnemyParticleSimpleTemplate extends EntityTemplateImpl {

	Injector injector;
	BodyBuilder bodyBuilder;
	ResourceManager<String> resourceManager;
	
	Synchronizer synchronizer;

	public static class FixedMovementScript extends ScriptJavaImpl {

		private final Vector2 position = new Vector2();
		private final Vector2 force = new Vector2();

		@Override
		public void init(World world, Entity e) {
			FollowRandomTargetComponent followRandomTargetComponent = Components.getFollowRandomTargetComponent(e);
			followRandomTargetComponent.position.set(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
		}

		@Override
		public void update(World world, Entity e) {
			FollowRandomTargetComponent followRandomTargetComponent = Components.getFollowRandomTargetComponent(e);

			SpatialComponent spatialComponent = Components.getSpatialComponent(e);
			Spatial spatial = spatialComponent.getSpatial();
			
			SpriteComponent spriteComponent = Components.getSpriteComponent(e);
			spriteComponent.setUpdateRotation(false);
			
			Sprite sprite = spriteComponent.getSprite();
			sprite.setRotation(sprite.getRotation() + GlobalTime.getDelta() * -90f);

			position.set(spatial.getX(), spatial.getY());

			if (followRandomTargetComponent.position.dst(position) < 1f) {
				followRandomTargetComponent.position.set(MathUtils.random(-5f, 5f), MathUtils.random(-5f, 5f));
			} else {
				PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
				
				force.set(spatial.getX(), spatial.getY());
				force.sub(followRandomTargetComponent.position.x, followRandomTargetComponent.position.y);

				force.mul(-3f);

				physicsComponent.getBody().applyForceToCenter(force);
			}

		}

	}
	
	@Override
	public void apply(Entity entity) {
		Spatial spatial = parameters.get("spatial");

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						// .restitution(1f) //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.All) //
						.circleShape(0.25f)) //
				.type(BodyType.DynamicBody) //
				.position(spatial.getX(), spatial.getY()) //
				.angle(spatial.getAngle()) //
				.userData(entity) //
				.build();

		spatial.setSize(0.5f, 0.5f);

		entity.setGroup(Tags.EnemyCharacter);

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new LinearVelocityLimitComponent(MathUtils.random(2.5f, 5f)));

		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, spatial)));
		entity.addComponent(new ScriptComponent( //
				injector.getInstance(FixedMovementScript.class), //
				injector.getInstance(AliveTimeScript.class), //
				injector.getInstance(BounceWhenCollideScript.class) //
		));

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Al3);
		
		SpriteComponent spriteComponent = new SpriteComponent(sprite);

		synchronizer.transition(Transitions.transition(spriteComponent.getColor(), LibgdxConverters.color()) //
				.start(1f, 1f, 1f, 0f) //
				.end(0.5f, 1f, 1f, 1f, 1f) //
				.build());
		
		entity.addComponent(spriteComponent);
		entity.addComponent(new RenderableComponent(1));

		entity.addComponent(new AliveComponent(MathUtils.random(10f, 15f)));
		entity.addComponent(new FollowRandomTargetComponent());
	}
}
