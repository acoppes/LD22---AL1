package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.utils.EntityStore;
import com.gemserk.componentsengine.utils.Interval;

public class SpawnerComponent extends Component {
	
	public EntityTemplate entityTemplate;
	public float timeToSpawn;

	public Interval spawnInterval;
	
	public EntityStore store;

	public SpawnerComponent(EntityTemplate entityTemplate, Interval spawnInterval, float timeToSpawn) {
		this.entityTemplate = entityTemplate;
		this.spawnInterval = spawnInterval;
		this.timeToSpawn = timeToSpawn;
	}

}
