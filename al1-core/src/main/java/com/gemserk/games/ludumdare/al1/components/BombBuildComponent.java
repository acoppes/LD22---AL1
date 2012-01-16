package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;

public class BombBuildComponent extends Component {

	public static enum BombBuildState {
		None, Inside, TouchedCenter
	};

	public BombBuildState state;
	public boolean shouldExplode = false;

	// capture the particles in the moment the main enters the convex hull
	// public Array<Entity> particles = new Array<Entity>();

	public BombBuildComponent(BombBuildState state) {
		this.state = state;
	}

}
