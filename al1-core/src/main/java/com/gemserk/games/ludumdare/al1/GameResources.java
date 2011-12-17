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
		
		public static final String MainParticle = "MainParticleSprite";
		public static final String EnemyParticle = "EnemyParticleSprite";

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
		
		resource(Sprites.MainParticle, sprite2() //
				.textureAtlas(TextureAtlases.Images, "al1") //
		);

		resource(Sprites.EnemyParticle, sprite2() //
				.textureAtlas(TextureAtlases.Images, "al2") //
		);


	}
}

