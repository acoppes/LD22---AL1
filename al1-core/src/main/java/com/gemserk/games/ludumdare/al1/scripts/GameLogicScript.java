package com.gemserk.games.ludumdare.al1.scripts;

import com.artemis.Entity;
import com.artemis.World;
import com.gemserk.commons.artemis.events.ArtemisEventListener;
import com.gemserk.commons.artemis.events.Event;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.scripts.ScriptJavaImpl;
import com.gemserk.games.ludumdare.al1.Events;

public class GameLogicScript extends ScriptJavaImpl {
	
	EventManager eventManager;

	@Override
	public void init(World world, Entity e) {
		eventManager.register(Events.MainExploded, new ArtemisEventListener(world, e) {
			@Override
			public void onEvent(Event event) {
				onMainParticleExploded(world, entity, event);
			}
		});
	}
	
	@Override
	public void update(World world, Entity e) {
		
	}
	
	public void onMainParticleExploded(World world, Entity e, Event event) {
		Entity mainParticle = (Entity) event.getSource();
		
		mainParticle.delete();
		
		eventManager.registerEvent(Events.GameOver, e);
	}
	
}
