package de.macbury.zanbox.level.terrain.chunks.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.chunks.Chunk;

/**
 * Created by macbury on 28.05.14.
 */
public abstract class Layer implements Disposable {
  public final static int BASE_INDEX = 0;
  private static final String TAG = "Layer";
  public int index = BASE_INDEX;
  public Array<ChunkLayerPartRenderable> renderables;

  protected Array<ChunkLayerPartRenderable> tempArray;
  public byte[][] tiles;
  protected Chunk chunk;
  protected boolean generatedTiles = false;
  protected final static Vector3 tempA = new Vector3();
  protected final static Vector3 tempB = new Vector3();

  public Layer() {
    this.renderables = new Array<ChunkLayerPartRenderable>();
    this.tempArray   = new Array<ChunkLayerPartRenderable>();
    this.tiles       = new byte[Chunk.TILE_SIZE][Chunk.TILE_SIZE];
  }

  public void setChunk(Chunk chunk) {
    this.chunk = chunk;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void buildTiles(int tileStartX, int tileStartZ) {
     generateTiles(tileStartX, tileStartZ);
  }

  public void buildGeometry(int tileStartX, int tileStartZ, boolean onlyBorder) {
    for(ChunkLayerPartRenderable renderable : renderables) {
      if (onlyBorder) {
        if (renderable.border) {
          chunk.chunksProvider.level.tileBuilder.free(renderable);
          tempArray.add(renderable);
        }
      } else {
        chunk.chunksProvider.level.tileBuilder.free(renderable);
        tempArray.add(renderable);
      }
    }

    for (ChunkLayerPartRenderable renderable : tempArray) {
      renderables.removeValue(renderable, true);
    }
    tempArray.clear();

    buildRenderables(tileStartX, tileStartZ, onlyBorder);

    if (renderables.size > ChunkLayerPartRenderable.TOTAL_COUNT) {
      throw new GdxRuntimeException("There is more than " + ChunkLayerPartRenderable.TOTAL_COUNT + " it was " + renderables.size);
    } else {
      Gdx.app.log(TAG, "Renderables for layer: " + renderables.size);
    }
  }

  protected abstract void buildRenderables(int tileStartX, int tileStartZ, boolean onlyBorder);
  public abstract void generateTiles(int tileStartX, int tileStartZ);

  @Override
  public void dispose() {
    for(ChunkLayerPartRenderable renderable : renderables) {
      chunk.chunksProvider.level.tileBuilder.free(renderable);
    }

    renderables.clear();

    chunk = null;
    tiles = null;
  }

  public byte getTileByLocalTilePosition(int tileX, int tileY) {
    return tiles[tileX][tileY];
  }

  public byte getTileByWorldTilePosition(int tileX, int tileY) {
    return getTileByLocalTilePosition((int)(tileX + chunk.position.x),(int)(tileY + chunk.position.y));
  }
}
