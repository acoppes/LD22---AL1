package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector2;

public class AreaForceComponent extends Component {

	public final Vector2 force = new Vector2();

	public AreaForceComponent(float x, float y) {
		this.force.set(x, y);
	}

}
