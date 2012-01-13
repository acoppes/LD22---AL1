package com.gemserk.games.ludumdare.al1.templates;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.box2d.Contacts;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.games.ludumdare.al1.Collisions;
import com.gemserk.games.ludumdare.al1.components.Components;

public class ForceInAreaTemplate extends EntityTemplateImpl {
	
	BodyBuilder bodyBuilder;
	Injector injector;
	
	public static class AreaForceScript extends ScriptJavaImpl {

		@Override
		public void update(World world, Entity e) {
			
			PhysicsComponent physicsComponent = Components.getPhysicsComponent(e);
			
			Contacts contact = physicsComponent.getContact();
			
			if (!contact.isInContact())
				return;
			
			System.out.println("in contact!");
		}

	}

	@Override
	public void apply(Entity entity) {
		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.categoryBits(Collisions.Area) //
						.maskBits(Collisions.All) //
						.sensor() //
						.boxShape(5f, 1f)) //
				.type(BodyType.StaticBody) //
				.position(0f, 0f) //
				.angle(0f) //
				.userData(entity) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new ScriptComponent(injector.getInstance(AreaForceScript.class)));
	}


}
