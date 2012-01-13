package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.components.GroupComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.gdx.box2d.Contacts.Contact;
import com.gemserk.games.ludumdare.al1.Events;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.components.Components;

public class ExplodeWhenCollideScript extends ScriptJavaImpl {
	
	EventManager eventManager;

	@Override
	public void update(World world, Entity e) {
		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
		
		Contacts contacts = physicsComponent.getContact();
		
		if (!contacts.isInContact())
			return;
		
		boolean shouldExplode = false;
		
		for (int i = 0; i < contacts.getContactCount(); i++) {
			
			Contact contact = contacts.getContact(i);
			Entity otherEntity = (Entity) contact.getOtherFixture().getBody().getUserData();
			
			GroupComponent groupComponent = Components.getGroupComponent(otherEntity);
			
			if (groupComponent == null)
				continue;
			
			if (groupComponent.group.equals(Groups.EnemyCharacter)) {
				shouldExplode= true;
				break;
			}
			
		}
		
		if (shouldExplode)
			eventManager.registerEvent(Events.MainExploded, e);
	}

}