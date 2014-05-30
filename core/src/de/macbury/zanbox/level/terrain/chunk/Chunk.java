package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.level.terrain.WorldEnv;
import de.macbury.zanbox.level.terrain.chunk.layers.GroundLayer;
import de.macbury.zanbox.level.terrain.chunk.layers.Layer;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 26.05.14.
 */
public class Chunk implements Disposable {
  public static final int SIZE = 16;
  private static final String TAG = "Chunk";
  private BoundingBox boundingBox;
  private Array<Layer> layers;
  public Vector2 position; // chunk position
  public Chunks chunks;

  private final static Vector3 temp = new Vector3();

  public Chunk(int sx, int sz) {
    position     = new Vector2(sx, sz);
    layers       = new Array<Layer>();
    layers.add(new GroundLayer(this));
    this.boundingBox = new BoundingBox();

    Vector3 startB = new Vector3();
    MyMath.chunkPositionToPosition(position, startB);
    Vector3 endB   = new Vector3(startB);
    endB.add(SIZE, Tile.SIZE, SIZE);
    this.boundingBox.set(startB, endB);
  }

  public void generate() {
    Gdx.app.log(TAG, "Rebuilding: "+ position.toString());
    MyMath.chunkPositionToTilePosition(position, temp);

    int ox = (int)temp.x;
    int oz = (int)temp.z;

    for(Layer layer : layers) {
      layer.generate(ox, oz);
    }
  }

  @Override
  public void dispose() {
    for(Layer layer : layers)
      layer.dispose();
    layers.clear();
  }

  public Chunks getChunks() {
    return chunks;
  }

  public void setChunks(Chunks chunks) {
    this.chunks = chunks;
  }

  public boolean at(float x, float z) {
    return at((int)x, (int)z);
  }

  public boolean at(int x, int z) {
    return (this.position.x == x && this.position.y == z);
  }

  public Layer getLayer(int layerIndex) {
    for(int i = 0; i < layers.size; i++) {
      Layer layer = layers.get(i);
      if (layer.index == layerIndex) {
        return layer;
      }
    }

    return null;
  }

  public void render(ModelAndSpriteBatch modelBatch) {
    Layer layer = getLayer(chunks.level.currentLayer);
    for(Renderable renderable : layer.renderables)
      modelBatch.render(renderable);
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }
}
