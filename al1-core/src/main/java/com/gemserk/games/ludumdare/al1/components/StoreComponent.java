package com.gemserk.games.ludumdare.al1.components;

import com.artemis.Component;
import com.gemserk.commons.artemis.utils.EntityStore;

public class StoreComponent extends Component {

	public final EntityStore store;

	public StoreComponent(EntityStore store) {
		this.store = store;
	}

}
