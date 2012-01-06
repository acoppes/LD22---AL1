package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;

public class AliveComponent extends Component {
	
	public static enum State { Spawning, Alive, Dying }; 

	public float time;
	public State state = State.Spawning;
	
	public float spawnTime;
	public float dyingTime;
	
	public AliveComponent(float time) {
		this.time = time;
	}

}
