package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.games.Physics;
import com.gemserk.games.ludumdare.al1.Controller;
import com.gemserk.games.ludumdare.al1.components.Components;

public class MovementScript extends ScriptJavaImpl {

	private final Vector2 force = new Vector2();

	@Override
	public void update(World world, Entity e) {
		Controller controller = Components.getControllerComponent(e).controller;
		
		force.set(controller.direction);
		force.mul(50f);
		
		Physics physics = Components.getPhysicsComponent(e).getPhysics();
		Body body = physics.getBody();
		body.applyForceToCenter(force);
		
		Vector2 velocity = body.getLinearVelocity();
		
		force.set(velocity);
		force.mul(-5f);
		
		body.applyForceToCenter(force);
	}

}