package de.macbury.zanbox.level.terrain.chunks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.chunks.layers.GroundLayer;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.persister.KryoSystem;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 26.05.14.
 */
public class Chunk implements Disposable {
  public final Vector3 worldPosition;
  private boolean visible;

  public enum RebuildGeometryMode {
    All, Border, None
  }
  public static final int TILE_SIZE = 32;
  public static final float METERS_SIZE = TILE_SIZE * Tile.SIZE;
  private static final String TAG = "Chunk";
  private BoundingBox boundingBox;
  public Array<Layer> layers;
  public Vector2 position; // chunk position
  public ChunksProvider chunksProvider;

  private final static Vector3 temp = new Vector3();
  private boolean locked;

  public Chunk(int sx, int sz) {
    position         = new Vector2(sx, sz);
    worldPosition    = new Vector3();
    layers           = new Array<Layer>();
    this.boundingBox = new BoundingBox();

    Vector3 startB = new Vector3();
    MyMath.chunkPositionToMeters(position, startB);
    Vector3 endB   = new Vector3();
    endB.set(startB).add(METERS_SIZE, Tile.SIZE * 2, METERS_SIZE);
    this.boundingBox.set(startB, endB);
    worldPosition.set(startB);
  }

  public void buildGeometry(boolean onlyBorder) {
    Gdx.app.log(TAG, onlyBorder ? "Rebuilding: " : "Building: " + position.toString());
    MyMath.chunkPositionToTilePosition(position, temp);

    int ox = (int)temp.x;
    int oz = (int)temp.z;

    for(Layer layer : layers) {
      layer.buildGeometry(ox, oz, onlyBorder);
    }
  }

  public void buildTiles() {
    Gdx.app.log(TAG, "Building: " + position.toString());
    MyMath.chunkPositionToTilePosition(position, temp);

    int ox = (int)temp.x;
    int oz = (int)temp.z;

    for(int i = 0; i < layers.size; i++) {
      Layer layer = layers.get(i);
      layer.buildTiles(ox, oz);
    }
  }

  @Override
  public void dispose() {
    Gdx.app.debug(TAG, "Dispose: "+toString());
    for(Layer layer : layers)
      layer.dispose();
    layers.clear();
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

  public boolean isLocked() {
    return locked;
  }

  public void lock() {
    this.locked = true;
  }

  public void unlock() {
    this.locked = false;
  }


  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean isVisible() {
    return visible;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }

  public String toString() {
    return "Chunk: " +position.toString() + " locked = " + locked;
  }

  public void unload() {
    dispose();
  }

  public void load() {
    if (KryoSystem.exists(this)) {
      KryoSystem system = new KryoSystem();
      system.load(this);
    } else {
      buildLayers();
      buildTiles();
      save();
    }
  }

  private void buildLayers() {
    addLayer(new GroundLayer());
  }

  public void addLayer(Layer layer) {
    layer.setChunk(this);
    layers.add(layer);
  }

  public void save() {
    KryoSystem kryo = new KryoSystem();
    kryo.save(this);
  }
}
