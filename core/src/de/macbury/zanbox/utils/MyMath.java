package de.macbury.zanbox.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 26.05.14.
 */
public class MyMath {
  private static Vector3 tempA = new Vector3();
  private static Vector3 tempB = new Vector3();
  private static Vector2 tempC = new Vector2();

  public static synchronized void worldToLocalTilePosition(Vector3 in, Vector3 out) {
    abs(tempA.set(in));
    tilePositionToChunkPoistion(tempA, tempC);
    int dx = MathUtils.round(tempC.x * Chunk.TILE_SIZE);
    int dy = MathUtils.round(tempC.y * Chunk.TILE_SIZE);

    tempA.sub(dx, 0, dy);

    out.set(tempA);
    if (out.x > Chunk.TILE_SIZE || out.z > Chunk.TILE_SIZE || out.y > Chunk.TILE_SIZE) {
      throw new GdxRuntimeException("Is above chunk size: " + out.toString());
    }
  }

  public static void abs(Vector3 vector) {
    vector.set(fastAbs(vector.x), fastAbs(vector.y), fastAbs(vector.z));
  }

  public static synchronized void localToWorldTilePosition(Chunk chunk, Vector3 in, Vector3 out) {
    chunkPositionToTilePosition(chunk.position, tempB);
    out.set(tempB.add(in));
  }

  public static synchronized void metersToTilePosition(Vector3 in, Vector3 out) {
    out.set(MathUtils.floor(in.x / Tile.SIZE), MathUtils.floor(in.y / Tile.SIZE), MathUtils.floor(in.z / Tile.SIZE));
  }

  public static synchronized void tilePositionToChunkPoistion(Vector3 in, Vector2 out) {
    out.set(MathUtils.floor(in.x / Chunk.TILE_SIZE), MathUtils.floor(in.z / Chunk.TILE_SIZE));
  }

  public static synchronized void metersToChunkPosition(Vector3 in, Vector2 out) {
    metersToTilePosition(in, tempA);
    tilePositionToChunkPoistion(tempA, out);
  }

  public static synchronized void chunkPositionToTilePosition(Vector2 in, Vector3 out) {
    out.set(MathUtils.floor(in.x * Chunk.TILE_SIZE), 0, MathUtils.floor(in.y * Chunk.TILE_SIZE));
  } 
  public static synchronized void tilePositionToMeters(Vector3 in, Vector3 out) {
    out.set(MathUtils.floor(in.x * Tile.SIZE), MathUtils.floor(in.y * Tile.SIZE), MathUtils.floor(in.z * Tile.SIZE));
  }

  public static synchronized void chunkPositionToMeters(Vector2 in, Vector3 out) {
    chunkPositionToTilePosition(in, tempA);
    tilePositionToMeters(tempA, out);
  }

  public static double fastFloor(double d) {
    int i = (int) d;
    return (d < 0 && d != i) ? i - 1 : i;
  }

  public static float fastFloor(float d) {
    int i = (int) d;
    return (d < 0 && d != i) ? i - 1 : i;
  }

  public static double fastAbs(double d) {
    return (d >= 0) ? d : -d;
  }

  public static float fastAbs(float d) {
    return (d >= 0.0) ? d : -d;
  }

  public static double clamp(double value) {
    if (value > 1.0) {
      return 1.0;
    } else if (value < 0.0) {
      return 0.0;
    }
    return value;
  }


  public synchronized static Vector3 min(Vector3 minimum, Vector3 maximum) {
    return tempA.set(minimum.x < maximum.x ? minimum.x : maximum.x, minimum.y < maximum.y ? minimum.y : maximum.y,
            minimum.z < maximum.z ? minimum.z : maximum.z);
  }

  public synchronized static Vector3 max(Vector3 minimum, Vector3 maximum) {
    return tempA.set(minimum.x > maximum.x ? minimum.x : maximum.x, minimum.y > maximum.y ? minimum.y : maximum.y,
            minimum.z > maximum.z ? minimum.z : maximum.z);
  }
}
