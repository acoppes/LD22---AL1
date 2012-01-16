package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;
import com.gemserk.commons.gdx.graphics.ConvexHull2d;

public class ConvexHullComponent extends Component {
	
	public ConvexHull2d convexHull2d;

	public ConvexHullComponent(ConvexHull2d convexHull2d) {
		this.convexHull2d = convexHull2d;
	}

}
