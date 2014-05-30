package de.macbury.zanbox.level.terrain.chunk.layers;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.level.terrain.chunk.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 28.05.14.
 */
public abstract class Layer implements Disposable {
  public final static int BASE_INDEX = 0;
  public int index = BASE_INDEX;
  public Array<ChunkLayerPartRenderable> renderables;
  protected byte[][] tiles;
  protected Chunk chunk;

  protected final static Vector3 tempA = new Vector3();
  protected final static Vector3 tempB = new Vector3();

  public Layer(Chunk chunk, int layer) {
    this.index       = layer;
    this.chunk       = chunk;
    this.renderables = new Array<ChunkLayerPartRenderable>();
    this.tiles       = new byte[Chunk.SIZE][Chunk.SIZE];
  }

  public abstract void generate(int startX, int startZ);

  @Override
  public void dispose() {
    for(ChunkLayerPartRenderable renderable : renderables) {
      chunk.chunks.level.tileBuilder.free(renderable);
    }

    renderables.clear();

    chunk = null;
    tiles = null;
  }
}
