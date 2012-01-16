package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.GroupComponent;
import com.gemserk.commons.artemis.components.LinearVelocityLimitComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.RenderableComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;
import com.gemserk.commons.gdx.math.MathUtils2;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.GameResources;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.components.AliveComponent;
import com.gemserk.games.ludumdare.al1.components.AliveComponent.State;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.scripts.AliveTimeScript;
import com.gemserk.games.ludumdare.al1.scripts.BounceWhenCollideScript;
import com.gemserk.games.ludumdare.al1.scripts.FollowMainCharacterScript;
import com.gemserk.resources.ResourceManager;

public class EnemyParticleTemplate extends EntityTemplateImpl {

	Injector injector;
	BodyBuilder bodyBuilder;
	ResourceManager<String> resourceManager;

	public static class RandomizeFollowParticleScript extends ScriptJavaImpl {

		private final Vector2 position = new Vector2();
		private final Rectangle worldRectangle = new Rectangle(-7.5f, -5f, 15f, 10f);

		@Override
		public void init(World world, Entity e) {
			Entity mainCharacter = world.getTagManager().getEntity(Tags.MainCharacter);

			if (mainCharacter != null) {
				Spatial mainCharacterSpatial = Components.getSpatialComponent(mainCharacter).getSpatial();
				Spatial spatial = Components.getSpatialComponent(e).getSpatial();

				// EntityTemplate enemyParticleTemplate = spawnerComponent.entityTemplate;

				position.set(MathUtils.random(5f, 12f), 0f);
				position.rotate(MathUtils.random(0, 360f));

				position.add(mainCharacterSpatial.getX(), mainCharacterSpatial.getY());

				MathUtils2.truncate(position, worldRectangle);

				spatial.setPosition(position.x, position.y);
			}

			AliveComponent aliveComponent = Components.getAliveComponent(e);
			aliveComponent.time = MathUtils.random(15f, 30f);
			aliveComponent.spawnTime = 1f;
			aliveComponent.dyingTime = 1f;
			aliveComponent.state = State.Spawning;

			LinearVelocityLimitComponent linearVelocityComponent = Components.getLinearVelocityComponent(e);
			linearVelocityComponent.setLimit(0.6f * MathUtils.random(3.5f, 7.5f));
		}

	}

	@Override
	public void apply(Entity entity) {
		Spatial spatial = parameters.get("spatial");

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						// .restitution(1f) //
						.categoryBits(Collisions.Enemy) //
						.maskBits(Collisions.None) //
						.circleShape(0.2f)) //
				.type(BodyType.DynamicBody) //
				.position(spatial.getX(), spatial.getY()) //
				.angle(spatial.getAngle()) //
				.userData(entity) //
				.build();

		spatial.setSize(0.5f, 0.5f);

		// entity.setGroup(Groups.EnemyCharacter);
		entity.addComponent(new GroupComponent(Groups.EnemyCharacter));

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new LinearVelocityLimitComponent(1f));

		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, spatial)));
		entity.addComponent(new ScriptComponent( //
				injector.getInstance(RandomizeFollowParticleScript.class), //
				injector.getInstance(FollowMainCharacterScript.class), //
				injector.getInstance(AliveTimeScript.class), //
				injector.getInstance(BounceWhenCollideScript.class)//
		));

		Sprite sprite = resourceManager.getResourceValue(GameResources.Sprites.Al2);
		SpriteComponent spriteComponent = new SpriteComponent(sprite);

		entity.addComponent(new AliveComponent(1f));
		entity.addComponent(spriteComponent);
		entity.addComponent(new RenderableComponent(1));

	}
}
