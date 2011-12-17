package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.MathUtils;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.EnemyParticleTemplate;

public class EnemyParticleSpawnerScript extends ScriptJavaImpl {

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

		EntityTemplate enemyParticleTemplate = injector.getInstance(EnemyParticleTemplate.class);

		entityFactory.instantiate(enemyParticleTemplate, new ParametersWrapper()//
				.put("spatial", new SpatialImpl(MathUtils.random(-10f, 10f), MathUtils.random(-10f, 10f), 1f, 1f, 0f)));

		spawnTime = MathUtils.random(5f, 15f);
	}

}
