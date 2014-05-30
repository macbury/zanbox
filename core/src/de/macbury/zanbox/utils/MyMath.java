package de.macbury.zanbox.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.level.terrain.chunk.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 26.05.14.
 */
public class MyMath {
  private static Vector3 tempA = new Vector3();
  private static Vector3 tempB = new Vector3();

  public static void positionToTilePosition(Vector3 in, Vector3 out) {
    out.set(MathUtils.floor(in.x / Tile.SIZE), MathUtils.floor(in.y / Tile.SIZE), MathUtils.floor(in.z / Tile.SIZE));
  }

  public static void tilePositionToChunkPoistion(Vector3 in, Vector2 out) {
    out.set(MathUtils.floor(in.x / Chunk.SIZE), MathUtils.floor(in.z / Chunk.SIZE));
  }

  public static void positionToChunkPosition(Vector3 in, Vector2 out) {
    positionToTilePosition(in, tempA);
    tilePositionToChunkPoistion(tempA, out);
  }

  public static void chunkPositionToTilePosition(Vector2 in, Vector3 out) {
    out.set(MathUtils.floor(in.x * Chunk.SIZE), 0, MathUtils.floor(in.y * Chunk.SIZE));
  } 
  public static void tilePositionToPosition(Vector3 in, Vector3 out) {
    out.set(MathUtils.floor(in.x * Tile.SIZE), MathUtils.floor(in.y * Tile.SIZE), MathUtils.floor(in.z * Tile.SIZE));
  }

  public static void chunkPositionToPosition(Vector2 in, Vector3 out) {
    chunkPositionToTilePosition(in, tempA);
    tilePositionToPosition(tempA, out);
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

  public static double clamp(double value) {
    if (value > 1.0) {
      return 1.0;
    } else if (value < 0.0) {
      return 0.0;
    }
    return value;
  }


}
