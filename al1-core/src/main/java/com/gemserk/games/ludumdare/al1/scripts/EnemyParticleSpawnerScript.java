package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.events.Event;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.events.reflection.Handles;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.Events;
import com.gemserk.games.ludumdare.al1.Tags;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.SpawnerComponent;

public class EnemyParticleSpawnerScript extends ScriptJavaImpl {

	private final Vector2 position = new Vector2();

	EventManager eventManager;
	EntityFactory entityFactory;

	@Override
	public void update(World world, Entity e) {
		SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);

		spawnerComponent.timeToSpawn -= GlobalTime.getDelta();

		if (spawnerComponent.timeToSpawn > 0)
			return;

		Entity target = world.getTagManager().getEntity(Tags.MainCharacter);

		SpatialComponent spatialComponent = Components.getSpatialComponent(target);
		Spatial spatial = spatialComponent.getSpatial();

		EntityTemplate enemyParticleTemplate = spawnerComponent.entityTemplate;

		position.set(MathUtils.random(5f, 12f), 0f);
		position.rotate(MathUtils.random(0, 360f));

		entityFactory.instantiate(enemyParticleTemplate, new ParametersWrapper()//
				.put("spatial", new SpatialImpl(spatial.getX() + position.x, spatial.getY() + position.y, 1f, 1f, 0f)));

		spawnerComponent.timeToSpawn = MathUtils.random( //
				spawnerComponent.spawnInterval.getMin(), //
				spawnerComponent.spawnInterval.getMax());
	}

	@Handles(ids = Events.ParticlesDestroyed)
	public void particleDestroyed(Event e) {
		ImmutableBag<Entity> particles = (ImmutableBag<Entity>) e.getSource();
		for (int i = 0; i < particles.size(); i++)
			particles.get(i).delete();
		// return to store if corresponds...
	}

}
