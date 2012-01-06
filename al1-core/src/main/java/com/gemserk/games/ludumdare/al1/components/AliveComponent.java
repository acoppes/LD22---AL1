package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;

public class AliveComponent extends Component {

	public float time;
	public boolean dying = false;
	
	public AliveComponent(float time) {
		this.time = time;
	}

}
