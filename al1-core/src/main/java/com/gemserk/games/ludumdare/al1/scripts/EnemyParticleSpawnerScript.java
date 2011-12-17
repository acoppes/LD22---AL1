package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.EnemyParticleTemplate;
import com.gemserk.games.ludumdare.al1.Tags;

public class EnemyParticleSpawnerScript extends ScriptJavaImpl {

	private final Vector2 position = new Vector2(); 
	
	EventManager eventManager;
	EntityFactory entityFactory;
	
	Injector injector;
	
	float spawnTime;

	@Override
	public void init(World world, Entity e) {
		spawnTime = 2f;
	}

	@Override
	public void update(World world, Entity e) {
		spawnTime -= GlobalTime.getDelta();

		if (spawnTime > 0)
			return;
		
		Entity target = world.getTagManager().getEntity(Tags.MainCharacter);
		
		SpatialComponent spatialComponent = Components.getSpatialComponent(target);
		Spatial spatial = spatialComponent.getSpatial();

		EntityTemplate enemyParticleTemplate = injector.getInstance(EnemyParticleTemplate.class);
		
		position.set(MathUtils.random(3f, 8f), 0f);
		position.rotate(MathUtils.random(0, 360f));

		entityFactory.instantiate(enemyParticleTemplate, new ParametersWrapper()//
				.put("spatial", new SpatialImpl(spatial.getX() + position.x, spatial.getY() + position.y, 1f, 1f, 0f)));

		spawnTime = MathUtils.random(5f, 15f);
	}

}
