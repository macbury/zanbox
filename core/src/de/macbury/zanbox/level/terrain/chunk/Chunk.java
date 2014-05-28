package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.level.terrain.tiles.GrassTile;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.utils.FPSLoop;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 26.05.14.
 */
public class Chunk implements Disposable {
  public static final int SIZE = 64;
  private static final String TAG = "Chunk";
  public Vector2 position; // chunk position
  public int level = 0;
  private Tile[][] tiles;
  private Chunks chunks;

  private final static Vector3 temp = new Vector3();

  public Chunk(int sx, int sz, int layer) {
    position     = new Vector2(sx, sz);
    level        = layer;
    tiles        = new Tile[SIZE][SIZE];
  }

  public void generate() {
    Gdx.app.log(TAG, "Rebuilding: "+ position.toString());
    MyMath.chunkPositionToTilePosition(position, temp);

    int ox = (int)temp.x;
    int oz = (int)temp.z;

    for(int x = 0; x < SIZE; x++) {
      for(int z = 0; z < SIZE; z++) {
        Zanbox.level.biomeProvider.getBiomeAt(x+ox,z+oz);
        tiles[x][z] = new GrassTile();
      }
    }
  }

  @Override
  public void dispose() {

  }

  public void update(float delta) {

  }

  public Chunks getChunks() {
    return chunks;
  }

  public void setChunks(Chunks chunks) {
    this.chunks = chunks;
  }

  public boolean at(float x, float z, int layer) {
    return at((int)x, (int)z, layer);
  }

  public boolean at(int x, int z, int layer) {
    return (this.position.x == x && this.position.y == z && this.level == layer);
  }

  public void render(ModelBatch modelBatch) {

  }
}
