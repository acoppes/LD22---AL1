package com.gemserk.games.ludumdare.al1.scripts;

import java.util.ArrayList;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gemserk.animation4j.gdx.converters.LibgdxConverters;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.GlobalTime;
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
		aliveComponent.spawnTime = 0.5f;
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
				setFilter(physics.getBody(), Collisions.Enemy, Collisions.All);
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
				setFilter(physics.getBody(), Collisions.Enemy, Collisions.None);
			}
		} else {
			aliveComponent.dyingTime -= GlobalTime.getDelta();
			if (aliveComponent.dyingTime <= 0f)
				e.delete();
		}
	}

	private void setFilter(Body body, short categoryBits, short maskBits) {
		ArrayList<Fixture> fixtureList = body.getFixtureList();
		for (int i = 0; i < fixtureList.size(); i++) {
			Fixture fixture = fixtureList.get(i);
			Filter filter = fixture.getFilterData();
			filter.categoryBits = categoryBits;
			filter.maskBits = maskBits;
			fixture.setFilterData(filter);
		}
	}

}