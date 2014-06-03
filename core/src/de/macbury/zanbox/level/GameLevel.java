package de.macbury.zanbox.level;

import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.debug.DebugShape;
import de.macbury.zanbox.debug.FrustrumRenderer;
import de.macbury.zanbox.entities.EntityFactory;
import de.macbury.zanbox.entities.managers.Tags;
import de.macbury.zanbox.entities.systems.*;
import de.macbury.zanbox.graphics.GameCamera;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.level.pools.Pools;
import de.macbury.zanbox.level.terrain.WorldEnv;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.level.terrain.chunks.ChunksRenderables;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 26.05.14.
 */
public class GameLevel extends World implements Disposable {
  public final int seed;

  public Pools pools;
  public CullingSystem cullingSystem;
  public ChunksSystem chunksSystem;
  public ChunksRenderables chunksRenderables;
  public ChunksProvider chunksProvider;
  public FrustrumRenderer frustrumRenderer;
  public ShapeRenderer shapeRenderer;
  public DayNightSystem dayNightSystem;
  public TileBuilder  tileBuilder;
  public PlayerSystem playerSystem;
  public SpriteRenderingSystem spriteRenderingSystem;
  public MovementSystem movementSystem;
  public EntityFactory factory;
  public GameCamera camera;
  public ModelAndSpriteBatch modelBatch;
  public WorldBiomeProvider biomeProvider;
  public RenderContext renderContext;
  public Vector3 worldPosition = new Vector3(); // for visibility, chunksProvider and other stuff
  public int currentLayer;
  public WorldEnv env;

  public GameLevel(int seed) {
    this.seed               = seed;
    this.pools              = new Pools(this);
    this.env                = new WorldEnv();
    this.tileBuilder        = new TileBuilder(this);
    this.biomeProvider      = new WorldBiomeProvider(seed);
    this.renderContext      = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));
    this.modelBatch         = new ModelAndSpriteBatch(renderContext);
    this.camera             = new GameCamera();
    this.chunksProvider     = new ChunksProvider(this);
    this.chunksRenderables  = new ChunksRenderables(this);
    this.shapeRenderer      = new ShapeRenderer();
    this.modelBatch.setEnv(env);
    this.frustrumRenderer = new FrustrumRenderer(camera);
    camera.update(true);

    this.factory      = new EntityFactory(this);

    Zanbox.level      = this;

    cullingSystem         = new CullingSystem(this);
    chunksSystem          = new ChunksSystem(this);
    dayNightSystem        = new DayNightSystem();
    movementSystem        = new MovementSystem();
    spriteRenderingSystem = new SpriteRenderingSystem(modelBatch);
    playerSystem          = new PlayerSystem(this);

    setManager(new Tags());
    setSystem(chunksSystem);
    setSystem(movementSystem);
    setSystem(playerSystem);
    setSystem(cullingSystem);
    setSystem(dayNightSystem);
    setSystem(spriteRenderingSystem, true);
    initialize();

    factory.player().addToWorld();
    factory.sign().addToWorld();
    chunksProvider.initializeBaseChunks();
  }

  public void update(float delta) {
    setDelta(delta);
    process();
    camera.update();
  }

  public void render() {
    renderContext.begin(); {
      modelBatch.begin(camera); {
        modelBatch.render(chunksRenderables);
        spriteRenderingSystem.process();
      } modelBatch.end();
    } renderContext.end();

    renderContext.begin(); {
      shapeRenderer.setProjectionMatrix(camera.combined);
      renderContext.setDepthTest(GL20.GL_LESS);
      frustrumRenderer.render(shapeRenderer);

      DebugShape.drawMap(shapeRenderer, chunksRenderables);
      shapeRenderer.setColor(Color.MAGENTA);
      shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
      DebugShape.draw(shapeRenderer, chunksRenderables.boundingBox);
      shapeRenderer.end();
    } renderContext.end();
  }

  @Override
  public void dispose() {
    chunksProvider.dispose();
    chunksRenderables.dispose();
    this.biomeProvider = null;
    tileBuilder.dispose();
    pools.dispose();
    if (Zanbox.level == this)
      Zanbox.level = null;
  }
}
