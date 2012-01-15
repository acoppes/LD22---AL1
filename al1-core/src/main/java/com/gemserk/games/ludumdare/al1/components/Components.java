package com.gemserk.games.ludumdare.al1.components;

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
	
	public static final Class<AliveComponent> aliveComponentClass = AliveComponent.class;
	public static final ComponentType aliveComponentType = ComponentTypeManager.getTypeFor(aliveComponentClass);
	
	public static AliveComponent getAliveComponent(Entity e) {
		return aliveComponentClass.cast(e.getComponent(aliveComponentType));
	}
	
	public static final Class<ControllerComponent> controllerComponentClass = ControllerComponent.class;
	public static final ComponentType controllerComponentType = ComponentTypeManager.getTypeFor(controllerComponentClass);
	
	public static ControllerComponent getControllerComponent(Entity e) {
		return controllerComponentClass.cast(e.getComponent(controllerComponentType));
	}
	
	public static final Class<AreaForceComponent> areaForceComponentClass = AreaForceComponent.class;
	public static final ComponentType areaForceComponentType = ComponentTypeManager.getTypeFor(areaForceComponentClass);
	
	public static AreaForceComponent getAreaForceComponent(Entity e) {
		return areaForceComponentClass.cast(e.getComponent(areaForceComponentType));
	}
	
	public static final Class<StoreComponent> storeComponentClass = StoreComponent.class;
	public static final ComponentType storeComponentType = ComponentTypeManager.getTypeFor(storeComponentClass);
	
	public static StoreComponent getStoreComponent(Entity e) {
		return storeComponentClass.cast(e.getComponent(storeComponentType));
	}
	
}

