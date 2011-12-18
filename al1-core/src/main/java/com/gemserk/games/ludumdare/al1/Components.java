package com.gemserk.games.ludumdare.al1;

import com.artemis.ComponentType;
import com.artemis.ComponentTypeManager;
import com.artemis.Entity;

public class Components extends com.gemserk.commons.artemis.components.Components {
	
	public static final Class<ShieldComponent> shieldComponentClass = ShieldComponent.class;
	public static final ComponentType shieldComponentType = ComponentTypeManager.getTypeFor(shieldComponentClass);
	
	public static ShieldComponent getShieldComponent(Entity e) {
		return shieldComponentClass.cast(e.getComponent(shieldComponentType));
	}

	public static final Class<SpawnerComponent> spawnerComponentClass = SpawnerComponent.class;
	public static final ComponentType spawnerComponentType = ComponentTypeManager.getTypeFor(spawnerComponentClass);
	
	public static SpawnerComponent getSpawnerComponent(Entity e) {
		return spawnerComponentClass.cast(e.getComponent(spawnerComponentType));
	}
	
	public static final Class<FollowRandomTargetComponent> followRandomTargetComponentClass = FollowRandomTargetComponent.class;
	public static final ComponentType followRandomTargetComponentType = ComponentTypeManager.getTypeFor(followRandomTargetComponentClass);
	
	public static FollowRandomTargetComponent getFollowRandomTargetComponent(Entity e) {
		return followRandomTargetComponentClass.cast(e.getComponent(followRandomTargetComponentType));
	}
	
	
}

