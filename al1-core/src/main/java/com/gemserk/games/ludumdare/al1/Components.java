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

}

