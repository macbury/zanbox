package de.macbury.zanbox.level.terrain.chunk.layers;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.terrain.chunk.Chunk;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 28.05.14.
 */
public class ChunkLayerPartRenderable extends Renderable implements Pool.Poolable {
  public static final int SIZE  = 8;
  public static final int COUNT = Chunk.SIZE / SIZE;
  public TileBuilder builder;

  @Override
  public void reset() {
    if (this.mesh != null) {
      mesh.dispose();
    }
    material = null;
  }
}
