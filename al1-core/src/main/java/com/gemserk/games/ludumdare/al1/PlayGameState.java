package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.commons.artemis.WorldWrapper;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.events.Event;
import com.gemserk.commons.artemis.events.EventListener;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.events.EventManagerImpl;
import com.gemserk.commons.artemis.render.RenderLayers;
import com.gemserk.commons.artemis.systems.CameraUpdateSystem;
import com.gemserk.commons.artemis.systems.EventManagerWorldSystem;
import com.gemserk.commons.artemis.systems.PhysicsSystem;
import com.gemserk.commons.artemis.systems.ReflectionRegistratorEventSystem;
import com.gemserk.commons.artemis.systems.RenderLayerSpriteBatchImpl;
import com.gemserk.commons.artemis.systems.RenderableSystem;
import com.gemserk.commons.artemis.systems.ScriptSystem;
import com.gemserk.commons.artemis.systems.SpriteUpdateSystem;
import com.gemserk.commons.artemis.systems.TagSystem;
import com.gemserk.commons.artemis.templates.EntityFactory;
import com.gemserk.commons.artemis.templates.EntityFactoryImpl;
import com.gemserk.commons.artemis.templates.EntityTemplate;
import com.gemserk.commons.artemis.templates.EntityTemplateImpl;
import com.gemserk.commons.gdx.GameStateImpl;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.camera.Libgdx2dCameraTransformImpl;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.gdx.screens.transitions.TransitionBuilder;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.scripts.GameLogicScript;

public class PlayGameState extends GameStateImpl {

	Game game;
	Injector injector;

	Libgdx2dCamera worldCamera;

	WorldWrapper scene;

	@Override
	public void init() {

		final Injector injector = this.injector.createChildInjector();

		worldCamera = new Libgdx2dCameraTransformImpl(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		worldCamera.zoom(48f);

		RenderLayers renderLayers = new RenderLayers();

		renderLayers.add("World", new RenderLayerSpriteBatchImpl(-500, 500, worldCamera));

		scene = new WorldWrapper(new World());

		com.badlogic.gdx.physics.box2d.World physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0f, 0f), false);
		EntityFactory entityFactory = new EntityFactoryImpl(scene.getWorld());
		EventManager eventManager = new EventManagerImpl();

		injector.bind("entityFactory", entityFactory);
		injector.bind("eventManager", eventManager);
		injector.bind("physicsWorld", physicsWorld);
		injector.bind("bodyBuilder", new BodyBuilder(physicsWorld));

		scene.addUpdateSystem(new ScriptSystem());
		scene.addUpdateSystem(new TagSystem());
		scene.addUpdateSystem(new ReflectionRegistratorEventSystem(eventManager));
		scene.addUpdateSystem(new PhysicsSystem(physicsWorld));

		scene.addUpdateSystem(injector.getInstance(EventManagerWorldSystem.class));

		scene.addRenderSystem(new CameraUpdateSystem());
		scene.addRenderSystem(new SpriteUpdateSystem());

		scene.addRenderSystem(new RenderableSystem(renderLayers));
		scene.addRenderSystem(new RenderScriptSystem());

		scene.addRenderSystem(new Box2dRenderSystem(worldCamera, physicsWorld));

		scene.init();

		entityFactory.instantiate(new EntityTemplateImpl() {
			@Override
			public void apply(Entity entity) {
				entity.addComponent(new ScriptComponent(injector.getInstance(GameLogicScript.class)));
			}
		});

		EntityTemplate mainParticleTemplate = injector.getInstance(MainParticleTemplate.class);
		entityFactory.instantiate(mainParticleTemplate, new ParametersWrapper() //
				.put("camera", worldCamera));

		EntityTemplate enemyParticleTemplate = injector.getInstance(EnemyParticleTemplate.class);

		for (int i = 0; i < 3; i++) {
			entityFactory.instantiate(enemyParticleTemplate, new ParametersWrapper()//
					.put("spatial", new SpatialImpl(MathUtils.random(-10f, 10f), MathUtils.random(-10f, 10f), 1f, 1f, 0f)));
		}

		eventManager.register(Events.GameOver, new EventListener() {
			@Override
			public void onEvent(Event event) {
				new TransitionBuilder(game, game.gameOverScreen) //
						.disposeCurrent() //
						.start();
			}
		});

	}

	@Override
	public void update() {
		scene.update(getDeltaInMs());
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		scene.render();
		// ImmediateModeRendererUtils.getProjectionMatrix().set(worldCamera.getCombinedMatrix());
	}

}
