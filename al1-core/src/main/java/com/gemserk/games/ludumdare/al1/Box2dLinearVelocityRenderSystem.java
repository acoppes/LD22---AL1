package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;

public class Box2dLinearVelocityRenderSystem extends EntityProcessingSystem {

	private final Libgdx2dCamera camera;
	private final ShapeRenderer shapeRenderer;
	
	private final Vector2 direction = new Vector2();

	public Box2dLinearVelocityRenderSystem(Libgdx2dCamera camera) {
		super(Components.physicsComponentClass);
		this.camera = camera;
		this.shapeRenderer = new ShapeRenderer(500);
	}

	@Override
	protected void process(Entity e) {
		shapeRenderer.setProjectionMatrix(camera.getProjectionMatrix());
		shapeRenderer.setTransformMatrix(camera.getModelViewMatrix());
		
		Body body = Components.getPhysicsComponent(e).getPhysics().getBody();
		Vector2 position = body.getPosition();
		
		direction.set(body.getLinearVelocity());
		
		if (direction.len() < 0.01f)
			return;
		
//		direction.mul(0.35f * 40f / camera.getModelViewMatrix().val[Matrix4.M00]);
		direction.nor().mul(40f / camera.getModelViewMatrix().val[Matrix4.M00]);
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0f, 1f, 0f, 1f);
		shapeRenderer.line(position.x, position.y, position.x+ direction.x, position.y + direction.y);
		shapeRenderer.end();
	}

}
