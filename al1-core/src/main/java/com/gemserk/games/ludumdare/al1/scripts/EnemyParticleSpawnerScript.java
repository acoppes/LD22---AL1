package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.utils.StoreFactory;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.components.Components;
import com.gemserk.games.ludumdare.al1.components.SpawnerComponent;
import com.gemserk.games.ludumdare.al1.components.StoreComponent;

public class EnemyParticleSpawnerScript extends ScriptJavaImpl {

	EventManager eventManager;
	EntityFactory entityFactory;

	@Override
	public void init(World world, final Entity e) {
		final SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
		spawnerComponent.store = new EntityStore(new StoreFactory<Entity>() {

			final ParametersWrapper parameters = new ParametersWrapper();

			@Override
			public Entity createObject() {
				EntityTemplate enemyParticleTemplate = spawnerComponent.entityTemplate;
				Entity entity = entityFactory.instantiate(enemyParticleTemplate, parameters//
						.put("spatial", new SpatialImpl(0f, 0f)));
				entity.addComponent(new StoreComponent(spawnerComponent.store));
				return entity;
			}
		});
		spawnerComponent.store.preCreate(5);
	}

	@Override
	public void update(World world, Entity e) {
		SpawnerComponent spawnerComponent = Components.getSpawnerComponent(e);
		spawnerComponent.timeToSpawn -= GlobalTime.getDelta();

		if (spawnerComponent.timeToSpawn > 0)
			return;

		// Entity target = world.getTagManager().getEntity(Tags.MainCharacter);
		//
		// SpatialComponent spatialComponent = Components.getSpatialComponent(target);
		// Spatial spatial = spatialComponent.getSpatial();
		//
		// // EntityTemplate enemyParticleTemplate = spawnerComponent.entityTemplate;
		//
		// position.set(MathUtils.random(5f, 12f), 0f);
		// position.rotate(MathUtils.random(0, 360f));

		// instantiate an entity from the store.
		spawnerComponent.store.get();

		// Spatial entitySpatial = Components.getSpatialComponent(entity).getSpatial();
		//
		// entitySpatial.setPosition(spatial.getX() + position.x, spatial.getY() + position.y);

		// entityFactory.instantiate(enemyParticleTemplate, new ParametersWrapper()//
		// .put("spatial", new SpatialImpl(spatial.getX() + position.x, spatial.getY() + position.y, 1f, 1f, 0f)));

		spawnerComponent.timeToSpawn = MathUtils.random( //
				spawnerComponent.spawnInterval.getMin(), //
				spawnerComponent.spawnInterval.getMax());
	}

}
