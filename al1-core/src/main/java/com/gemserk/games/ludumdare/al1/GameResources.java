package com.gemserk.games.ludumdare.al1;

import com.gemserk.commons.gdx.resources.LibgdxResourceBuilder;
import com.gemserk.resources.ResourceManager;

/**
 * Declares all resources needed for the game.
 */
public class GameResources extends LibgdxResourceBuilder {

	public static class TextureAtlases {

		private static final String Images = "AllImagesTextureAtlas";

	}

	public static class Sprites {

		public static final String Al1 = "Al1Sprite";
		public static final String Al2 = "Al2Sprite";
		public static final String Al3 = "Al3Sprite";

		public static final String Shield = "ShieldSprite";

	}

	/**
	 * Only creates all resource declarations, it doesn't load all the stuff yet.
	 */
	public static void load(ResourceManager<String> resourceManager) {
		new GameResources(resourceManager);
	}

	private GameResources(ResourceManager<String> resourceManager) {
		super(resourceManager);

		textureAtlas(TextureAtlases.Images, "data/images/pack");

		resource(Sprites.Al1, sprite2().textureAtlas(TextureAtlases.Images, "al1"));
		resource(Sprites.Al2, sprite2().textureAtlas(TextureAtlases.Images, "al2"));
		resource(Sprites.Al3, sprite2().textureAtlas(TextureAtlases.Images, "al3"));
		resource(Sprites.Shield, sprite2().textureAtlas(TextureAtlases.Images, "shield"));
	}
}
