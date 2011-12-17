package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.components.PhysicsComponent;
import com.gemserk.commons.artemis.components.SpatialComponent;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.games.SpatialPhysicsImpl;

public class MainParticleTemplate extends EntityTemplateImpl {

	BodyBuilder bodyBuilder;

	@Override
	public void apply(Entity entity) {

		Body body = bodyBuilder //
				.fixture(bodyBuilder.fixtureDefBuilder() //
						.circleShape(0.5f)) //
				.position(0f, 0f) //
				.build();

		entity.addComponent(new PhysicsComponent(body));
		entity.addComponent(new SpatialComponent(new SpatialPhysicsImpl(body, 1f, 1f)));

	}

}
