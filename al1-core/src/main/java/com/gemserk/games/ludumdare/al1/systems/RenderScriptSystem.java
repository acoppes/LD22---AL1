package com.gemserk.games.ludumdare.al1.systems;

import com.artemis.Entity;
import com.artemis.EntityProcessingSystem;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.games.ludumdare.al1.components.RenderScriptComponent;

public class RenderScriptSystem extends EntityProcessingSystem {

	private static final Class<RenderScriptComponent> renderScriptComponentClass = RenderScriptComponent.class;

	public RenderScriptSystem() {
		super(RenderScriptComponent.class);
	}

	@Override
	protected void enabled(Entity e) {
		super.enabled(e);
		Script[] scripts = e.getComponent(renderScriptComponentClass).getScripts();
		for (int i = 0; i < scripts.length; i++)
			scripts[i].init(world, e);
	}

	@Override
	protected void disabled(Entity e) {
		Script[] scripts = e.getComponent(renderScriptComponentClass).getScripts();
		for (int i = 0; i < scripts.length; i++)
			scripts[i].dispose(world, e);
		super.disabled(e);
	}

	@Override
	protected void process(Entity e) {
		Script[] scripts = e.getComponent(renderScriptComponentClass).getScripts();
		for (int i = 0; i < scripts.length; i++)
			scripts[i].update(world, e);
	}

}
