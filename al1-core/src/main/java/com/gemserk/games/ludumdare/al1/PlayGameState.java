package com.gemserk.games.ludumdare.al1;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gemserk.animation4j.transitions.sync.Synchronizer;
import com.gemserk.commons.artemis.WorldWrapper;
import com.gemserk.commons.artemis.components.Components;
import com.gemserk.commons.artemis.components.ScriptComponent;
import com.gemserk.commons.artemis.events.Event;
import com.gemserk.commons.artemis.events.EventListener;
import com.gemserk.commons.artemis.events.EventManager;
import com.gemserk.commons.artemis.events.EventManagerImpl;
import com.gemserk.commons.artemis.render.RenderLayers;
import com.gemserk.commons.artemis.scripts.Script;
import com.gemserk.commons.artemis.systems.EventManagerWorldSystem;
import com.gemserk.commons.artemis.systems.GroupSystem;
import com.gemserk.commons.artemis.systems.LimitLinearVelocitySystem;
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
import com.gemserk.commons.gdx.GlobalTime;
import com.gemserk.commons.gdx.box2d.BodyBuilder;
import com.gemserk.commons.gdx.camera.Libgdx2dCamera;
import com.gemserk.commons.gdx.camera.Libgdx2dCameraTransformImpl;
import com.gemserk.commons.gdx.games.SpatialImpl;
import com.gemserk.commons.gdx.graphics.ImmediateModeRendererUtils;
import com.gemserk.commons.gdx.graphics.SpriteBatchUtils;
import com.gemserk.commons.gdx.screens.transitions.TransitionBuilder;
import com.gemserk.commons.reflection.Injector;
import com.gemserk.commons.text.CustomDecimalFormat;
import com.gemserk.componentsengine.utils.Interval;
import com.gemserk.componentsengine.utils.ParametersWrapper;
import com.gemserk.games.ludumdare.al1.components.SpawnerComponent;
import com.gemserk.games.ludumdare.al1.scripts.EnemyParticleSpawnerScript;
import com.gemserk.games.ludumdare.al1.scripts.GameLogicScript;
import com.gemserk.games.ludumdare.al1.scripts.StickControllerScript;
import com.gemserk.games.ludumdare.al1.templates.EnemyParticleSimpleTemplate;
import com.gemserk.games.ludumdare.al1.templates.EnemyParticleTemplate;
import com.gemserk.games.ludumdare.al1.templates.ForceInAreaTemplate;
import com.gemserk.games.ludumdare.al1.templates.MainParticleTemplate;

public class PlayGameState extends GameStateImpl {

	Game game;
	Injector injector;

	Libgdx2dCamera worldCamera;

	WorldWrapper scene;
	Libgdx2dCamera normalCamera;

	float score;
	SpriteBatch spriteBatch;
	BitmapFont font;
	CustomDecimalFormat customDecimalFormat;

	Synchronizer synchronizer;

	@Override
	public void init() {

		final Injector injector = this.injector.createChildInjector();

		synchronizer = new Synchronizer();

		float gameZoom = Gdx.graphics.getHeight() / 480f;

		normalCamera = new Libgdx2dCameraTransformImpl(0f, 0f);
		normalCamera.zoom(1f * gameZoom);

		worldCamera = new Libgdx2dCameraTransformImpl(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f);
		worldCamera.zoom(48f * gameZoom);

		RenderLayers renderLayers = new RenderLayers();

		renderLayers.add("World", new RenderLayerSpriteBatchImpl(-500, 500, worldCamera));

		scene = new WorldWrapper(new World());

		com.badlogic.gdx.physics.box2d.World physicsWorld = new com.badlogic.gdx.physics.box2d.World(new Vector2(0f, 0f), false);
		EntityFactory entityFactory = new EntityFactoryImpl(scene.getWorld());
		EventManager eventManager = new EventManagerImpl();

		final BodyBuilder bodyBuilder = new BodyBuilder(physicsWorld);

		injector.bind("entityFactory", entityFactory);
		injector.bind("eventManager", eventManager);
		injector.bind("physicsWorld", physicsWorld);
		injector.bind("bodyBuilder", bodyBuilder);
		injector.bind("synchronizer", synchronizer);

		scene.addUpdateSystem(new ScriptSystem());
		scene.addUpdateSystem(new TagSystem());
		scene.addUpdateSystem(new GroupSystem());
		scene.addUpdateSystem(new ReflectionRegistratorEventSystem(eventManager));
		scene.addUpdateSystem(new PhysicsSystem(physicsWorld));

		scene.addUpdateSystem(new LimitLinearVelocitySystem(physicsWorld));

		scene.addUpdateSystem(injector.getInstance(EventManagerWorldSystem.class));

		scene.addRenderSystem(new SpriteUpdateSystem());

		scene.addRenderSystem(new RenderableSystem(renderLayers));

		scene.addRenderSystem(new Box2dRenderSystem(worldCamera, physicsWorld));
		scene.addRenderSystem(new Box2dLinearVelocityRenderSystem(worldCamera));

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

		// EntityTemplate shieldTemplate = injector.getInstance(ShieldTemplate.class);
		// entityFactory.instantiate(shieldTemplate, new ParametersWrapper() //
		// .put("camera", worldCamera));

		entityFactory.instantiate(new EntityTemplateImpl() {
			@Override
			public void apply(Entity entity) {
				entity.addComponent(new SpawnerComponent(injector.getInstance(EnemyParticleTemplate.class), new Interval(5, 10), 5f));
				entity.addComponent(new ScriptComponent(injector.getInstance(EnemyParticleSpawnerScript.class)));
			}
		});

		entityFactory.instantiate(new EntityTemplateImpl() {
			@Override
			public void apply(Entity entity) {
				entity.addComponent(new SpawnerComponent(injector.getInstance(EnemyParticleSimpleTemplate.class), new Interval(4, 8), 2f));
				entity.addComponent(new ScriptComponent(injector.getInstance(EnemyParticleSpawnerScript.class)));
			}
		});

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(0f, -6.75f, 10f, 2f, 0f)) //
				.put("force", new Vector2(0f, 100f)) //
				);
		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(0f, 6.75f, 10f, 2f, 0f)) //
				.put("force", new Vector2(0f, -100f)) //
				);

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(-10f, 0f, 2f, 20f, 0f)) //
				.put("force", new Vector2(100f, 0f)) //
				);

		entityFactory.instantiate(injector.getInstance(ForceInAreaTemplate.class), new ParametersWrapper() //
				.put("spatial", new SpatialImpl(10f, 0f, 2f, 20f, 0f)) //
				.put("force", new Vector2(-100f, 0f)) //
				);

		eventManager.register(Events.GameOver, new EventListener() {
			@Override
			public void onEvent(Event event) {
				new TransitionBuilder(game, game.gameOverScreen) //
						.disposeCurrent() //
						.restartScreen() //
						.parameter("score", (long) score).start();
			}
		});

		score = 0;

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();

		customDecimalFormat = new CustomDecimalFormat(5);
	}

	@Override
	public void update() {
		synchronizer.synchronize(getDelta());
		scene.update(getDeltaInMs());

		ImmutableBag<Entity> enemies = scene.getWorld().getGroupManager().getEntities(Groups.EnemyCharacter);

		score += GlobalTime.getDelta() * enemies.size();

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		scene.render();

		normalCamera.apply();

		spriteBatch.begin();
		SpriteBatchUtils.drawMultilineText(spriteBatch, font, customDecimalFormat.format((long) score), 20f, Gdx.graphics.getHeight() * 0.95f, 0f, 0.5f);
		spriteBatch.end();

		renderMoveableStickOnScreen();

	}

	private void renderMoveableStickOnScreen() {
		Entity mainCharacter = scene.getWorld().getTagManager().getEntity(Tags.MainCharacter);

		if (mainCharacter == null)
			return;

		Script script = Components.getScriptComponent(mainCharacter).getScripts().get(0);

		if (script == null)
			return;

		if (!(script instanceof StickControllerScript))
			return;

		StickControllerScript stickControllerScript = (StickControllerScript) script;

		if (!stickControllerScript.moving)
			return;
		
		Vector2 stickPosition = stickControllerScript.stickPosition;
		Vector2 touchPosition = stickControllerScript.touchPosition;

		ImmediateModeRendererUtils.drawSolidCircle(stickPosition.x, stickPosition.y, stickControllerScript.radius * 0.1f, Color.RED);
		ImmediateModeRendererUtils.drawSolidCircle(stickPosition.x, stickPosition.y, stickControllerScript.radius, Color.RED);
		ImmediateModeRendererUtils.drawSolidCircle(touchPosition.x, touchPosition.y, stickControllerScript.radius * 0.25f, Color.RED);
	}

	@Override
	public void resume() {
		Gdx.input.setCatchBackKey(false);
	}

}
