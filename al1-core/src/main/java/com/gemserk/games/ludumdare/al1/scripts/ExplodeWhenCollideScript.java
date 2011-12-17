package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.games.ludumdare.al1.Components;
import com.gemserk.games.ludumdare.al1.Events;

public class ExplodeWhenCollideScript extends ScriptJavaImpl {
	
	EventManager eventManager;

	@Override
	public void update(World world, Entity e) {
		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
		
		Contacts contact = physicsComponent.getContact();
		
		if (!contact.isInContact())
			return;
		
		eventManager.registerEvent(Events.MainExploded, e);
		
		System.out.println(eventManager);
	}

}