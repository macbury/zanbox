package de.macbury.zanbox.level;

import com.artemis.World;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.entities.EntityFactory;
import de.macbury.zanbox.entities.managers.Tags;
import de.macbury.zanbox.entities.systems.MovementSystem;
import de.macbury.zanbox.entities.systems.PlayerSystem;
import de.macbury.zanbox.entities.systems.SpriteRenderingSystem;
import de.macbury.zanbox.graphics.GameCamera;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunk.Chunks;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 26.05.14.
 */
public class GameLevel extends World implements Disposable {
  public TileBuilder  tileBuilder;
  public PlayerSystem playerSystem;
  public SpriteRenderingSystem spriteRenderingSystem;
  public MovementSystem movementSystem;
  public EntityFactory factory;
  public GameCamera camera;
  public ModelAndSpriteBatch modelBatch;
  public WorldBiomeProvider biomeProvider;
  public Chunks chunks;
  public RenderContext renderContext;
  public Vector3 worldPosition = new Vector3(); // for visibility, chunks and other stuff
  public int currentLayer;

  public GameLevel(int seed) {
    this.tileBuilder   = new TileBuilder();
    this.biomeProvider = new WorldBiomeProvider(seed);
    this.chunks        = new Chunks(this);
    this.renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED));
    this.modelBatch    = new ModelAndSpriteBatch(renderContext);

    this.camera        = new GameCamera();

    camera.update(true);

    this.factory      = new EntityFactory(this);

    Zanbox.level      = this;

    movementSystem        = new MovementSystem();
    spriteRenderingSystem = new SpriteRenderingSystem(modelBatch);
    playerSystem          = new PlayerSystem(this);

    setManager(new Tags());
    setSystem(movementSystem);
    setSystem(playerSystem);
    setSystem(spriteRenderingSystem, true);

    initialize();

    factory.player().addToWorld();
    factory.sign().addToWorld();

  }

  public void update(float delta) {
    camera.update();
    this.chunks.update(delta);
    setDelta(delta);
    process();
  }

  public void render() {
    renderContext.begin(); {
      modelBatch.begin(camera); {
        chunks.render(modelBatch);
        spriteRenderingSystem.process();
      } modelBatch.end();
    } renderContext.end();
  }

  @Override
  public void dispose() {
    this.biomeProvider = null;
    this.chunks.dispose();
    tileBuilder.dispose();
    if (Zanbox.level == this)
      Zanbox.level = null;
  }
}
