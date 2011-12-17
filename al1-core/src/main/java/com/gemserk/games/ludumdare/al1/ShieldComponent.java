package com.gemserk.games.ludumdare.al1;

import com.artemis.Component;
import com.gemserk.componentsengine.utils.Container;

public class ShieldComponent extends Component {

	public Container container;
	
	public boolean enabled;

	public ShieldComponent(Container container) {
		this.container = container;
	}

}
