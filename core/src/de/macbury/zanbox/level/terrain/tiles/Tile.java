package de.macbury.zanbox.level.terrain.tiles;

/**
 * Created by macbury on 28.05.14.
 */
public abstract class Tile {
  public static final float SIZE = 1f;
  public static final float HALF_SIZE = SIZE / 2;

  public static final byte NONE = 0;
  public static final byte LIGHT_GRASS = 1;
  public static final byte SAND  = 2;
  public static final byte DARK_GRASS = 3;
  public static final byte SNOW = 4;
  public static final byte ROCK = 5;
  public static final byte DEEP_WATER = 6;
  public static final byte SHALLOW_WATER = 7;
  public static final byte LAVA = 8;

  public static boolean isNextWall(byte currentTile, byte nextTile) {
    return (!isSolid(currentTile) && isSolid(nextTile));
  }

  public static boolean isNextNotWall(byte currentTile, byte nextTile) {
    return (isSolid(currentTile) && !isSolid(nextTile));
  }

  public static boolean isLiquid(byte tileID) {
    return (tileID == DEEP_WATER || tileID == LAVA || tileID == LAVA || tileID == SHALLOW_WATER);
  }

  public static float height(byte tileID) {
    return isSolid(tileID) ? Tile.SIZE : 0;
  }

  public static boolean isSolid(byte tileID) {
    return (tileID == ROCK && isNotEmpty(tileID));
  }

  public static boolean isNotEmpty(byte tileId) {
    return tileId != NONE;
  }
}
