package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.gdx.box2d.Contacts.Contact;
import com.gemserk.commons.gdx.games.Physics;
import com.gemserk.games.ludumdare.al1.Components;

public class BounceWhenCollideScript extends ScriptJavaImpl {
	
	private final Vector2 impulse = new Vector2();
	
	@Override
	public void update(World world, Entity e) {
		
		PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
		Physics physics = physicsComponent.getPhysics();
		
		Contacts contacts = physics.getContact();
		if (!contacts.isInContact())
			return;
		
		
		for (int i = 0; i < contacts.getContactCount(); i++) {
			Contact contact = contacts.getContact(i);
			Vector2 normal = contact.getNormal();
			
			impulse.set(normal);
			impulse.mul(-5f);

			Body body = physics.getBody();
			
			body.applyLinearImpulse(impulse, body.getTransform().getPosition());
		}
		
	}
	
}