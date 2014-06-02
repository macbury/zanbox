package de.macbury.zanbox.level.terrain.chunks.layers;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 28.05.14.
 */
public class ChunkLayerPartRenderable extends Renderable implements Pool.Poolable {
  public int x = 0;
  public int z = 0;
  public static final int SIZE_IN_TILES = 8;
  public static final float SIZE_IN_METERS = SIZE_IN_TILES * Tile.SIZE;
  public static final int ROW_COUNT = Chunk.TILE_SIZE / SIZE_IN_TILES;
  public static final int TOTAL_COUNT = ROW_COUNT * ROW_COUNT;
  public boolean border = false;
  public TileBuilder builder;
  public BoundingBox boundingBox;

  @Override
  public void reset() {
    if (this.mesh != null) {
      mesh.dispose();
    }
    mesh     = null;
    material = null;
    border   = false;
  }

  @Override
  public String toString() {
    return x+"x"+z+" - "+boundingBox.toString() + " border: " +border;
  }
}
