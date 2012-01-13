package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.GroupComponent;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.gdx.box2d.Contacts.Contact;
import com.gemserk.commons.gdx.games.Spatial;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.Groups;
import com.gemserk.games.ludumdare.al1.components.AreaForceComponent;
import com.gemserk.games.ludumdare.al1.components.Components;

public class ForceInAreaTemplate extends EntityTemplateImpl {
	
	BodyBuilder bodyBuilder;
	Injector injector;
	
	public static class AreaForceScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {
			
			PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
			
			Contacts contacts = physicsComponent.getContact();
			
			if (!contacts.isInContact())
				return;
			
			AreaForceComponent areaForceComponent = Components.getAreaForceComponent(e);
			Vector2 force = areaForceComponent.force;
			
			for (int i = 0; i < contacts.getContactCount(); i++) {
				Contact contact = contacts.getContact(i);
				contact.getOtherFixture().getBody().applyForceToCenter(force.x, force.y);
			}
			
		}

	}

	@Override
	public void apply(Entity entity) {
		Spatial spatial = parameters.get("spatial");
		Vector2 force = parameters.get("force");
		
		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Area) //
						.maskBits(Collisions.All) //
						.sensor() //
						.boxShape(spatial.getWidth(), spatial.getHeight())) //
				.type(BodyType.StaticBody) //
				.position(0f, 0f) //
				.angle(0f) //
				.userData(entity) //
				.build();
		
		body.setTransform(spatial.getX(), spatial.getY(), spatial.getAngle());

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new ScriptComponent(injector.getInstance(AreaForceScript.class)));
		
		entity.addComponent(new GroupComponent(Groups.AreaForce));
		
		entity.addComponent(new AreaForceComponent(force.x, force.y));
	}


}
