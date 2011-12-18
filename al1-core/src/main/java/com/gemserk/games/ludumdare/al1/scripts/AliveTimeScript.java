package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.animation4j.gdx.converters.LibgdxConverters;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.components.SpriteComponent;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.games.ludumdare.al1.AliveComponent;
import com.gemserk.games.ludumdare.al1.Components;

public class AliveTimeScript extends ScriptJavaImpl {
	
	Synchronizer synchronizer;

	@Override
	public void update(World world, Entity e) {
		AliveComponent aliveComponent = Components.getAliveComponent(e);
		
		aliveComponent.time -= GlobalTime.getDelta();
		
		if (aliveComponent.time <= 1f && !aliveComponent.dying) {
			aliveComponent.dying = true;
			
			SpriteComponent spriteComponent = Components.getSpriteComponent(e);

			synchronizer.transition(Transitions.transition(spriteComponent.getColor(), LibgdxConverters.color()) //
					.start(1f, 1f, 1f, 1f) //
					.end(1f, 1f, 1f, 1f, 0f) //
					.build());
			
		}
		
		if (aliveComponent.time <= 0f) {
			e.delete();
		}
	}

}