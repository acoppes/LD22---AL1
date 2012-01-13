package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.animation4j.gdx.converters.LibgdxConverters;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.box2d.Box2dUtils;
import com.gemserk.commons.gdx.games.Physics;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.components.AliveComponent;
import com.gemserk.games.ludumdare.al1.components.AliveComponent.State;
import com.gemserk.games.ludumdare.al1.components.Components;

public class AliveTimeScript extends ScriptJavaImpl {

	Synchronizer synchronizer;

	@Override
	public void init(World world, Entity e) {
		AliveComponent aliveComponent = Components.getAliveComponent(e);
		aliveComponent.state = State.Spawning;
		aliveComponent.spawnTime = 1f;
		aliveComponent.dyingTime = 1f;
		// start the transition here?

		SpriteComponent spriteComponent = Components.getSpriteComponent(e);
		synchronizer.transition(Transitions.transition(spriteComponent.getColor(), LibgdxConverters.color()) //
				.start(1f, 1f, 1f, 0f) //
				.end(aliveComponent.spawnTime, 1f, 1f, 1f, 1f) //
				.build());
	}

	@Override
	public void update(World world, Entity e) {
		AliveComponent aliveComponent = Components.getAliveComponent(e);

		if (aliveComponent.state == State.Spawning) {
			aliveComponent.spawnTime -= GlobalTime.getDelta();

			if (aliveComponent.spawnTime <= 0) {
				aliveComponent.state = State.Alive;
				Physics physics = Components.getPhysicsComponent(e).getPhysics();
				Box2dUtils.setFilter(physics.getBody(), Collisions.Enemy, Collisions.All, (short) 0);
			}

			return;
		} else if (aliveComponent.state == State.Alive) {

			aliveComponent.time -= GlobalTime.getDelta();

			if (aliveComponent.time <= 0f) {
				// e.delete();
				aliveComponent.state = State.Dying;

				SpriteComponent spriteComponent = Components.getSpriteComponent(e);

				synchronizer.transition(Transitions.transition(spriteComponent.getColor(), LibgdxConverters.color()) //
						.start(1f, 1f, 1f, 1f) //
						.end(aliveComponent.dyingTime, 1f, 1f, 1f, 0f) //
						.build());

				Physics physics = Components.getPhysicsComponent(e).getPhysics();
				Box2dUtils.setFilter(physics.getBody(), Collisions.Enemy, Collisions.None, (short) 0);
			}
		} else {
			aliveComponent.dyingTime -= GlobalTime.getDelta();
			if (aliveComponent.dyingTime <= 0f)
				e.delete();
		}
	}

}