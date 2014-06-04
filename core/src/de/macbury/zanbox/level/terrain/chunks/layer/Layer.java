package de.macbury.zanbox.level.terrain.chunks.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 28.05.14.
 */
public abstract class Layer implements Disposable {
  public final static int BASE_INDEX = 0;
  private static final String TAG = "Layer";
  public int index = BASE_INDEX;
  public Array<LayerSector> sectors;

  public byte[][] tiles;
  protected Chunk chunk;
  protected boolean generatedTiles = false;
  protected final static Vector3 tempA = new Vector3();
  protected final static Vector3 tempB = new Vector3();

  public Layer() {
    this.sectors      = new Array<LayerSector>();
    this.tiles       = new byte[Chunk.TILE_SIZE][Chunk.TILE_SIZE];
  }

  public LayerSector getSector(int localOffsetX, int localOffsetZ) {
    for(LayerSector sector : sectors) {
      if (sector.at(localOffsetX, localOffsetZ))
        return sector;
    }
    return null;
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

  public void buildGeometry(boolean onlyBorder) {
    buildGeometryCaches(onlyBorder);

    if (sectors.size > LayerSector.TOTAL_COUNT) {
      throw new GdxRuntimeException("There is more than " + LayerSector.TOTAL_COUNT + " it was " + sectors.size);
    } else {
      Gdx.app.log(TAG, "Renderables for layer: " + sectors.size);
    }
  }

  protected abstract void buildGeometryCaches(boolean onlyBorder);
  public abstract void generateTiles(int tileStartX, int tileStartZ);

  @Override
  public void dispose() {
    for(LayerSector sector: sectors) {
      sector.dispose();
    }

    sectors.clear();

    chunk = null;
    tiles = null;
  }

  public byte getTileByLocalTilePosition(int tileX, int tileY) {
    if (tileX >= 0 && tileX < Chunk.TILE_SIZE && tileY >= 0 && tileY < Chunk.TILE_SIZE) {
      return tiles[tileX][tileY];
    } else {
      return 0;
    }
  }

  public byte getTileByWorldTilePosition(int tileX, int tileY) {
    MyMath.worldToLocalTilePosition(tempA.set(tileX, 0, tileY), tempB);
    return getTileByLocalTilePosition((int)tempB.x,(int)tempB.z);// powinno pobierać tile poprzez providera mother fucker :!!!!!
  }

  public byte getTileByLocalToWorldTilePosition(int tileX, int tileY) {
    MyMath.worldToLocalTilePosition(tempA.set(chunk.position.x + tileX, 0, chunk.position.y + tileY), tempB);
    return getTileByLocalTilePosition((int)tempB.x,(int)tempB.z);
  }
}
