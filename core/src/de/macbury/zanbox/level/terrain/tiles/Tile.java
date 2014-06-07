package de.macbury.zanbox.level.terrain.tiles;

/**
 * Created by macbury on 28.05.14.
 */
public abstract class Tile {
  public static final float SIZE = 1f;
  public static final float HALF_SIZE = SIZE / 2;

  public static final float LIQUID_BOTTOM_HEIGHT = 0;
  public static final float LIQUID_HEIGHT = LIQUID_BOTTOM_HEIGHT + 0.7f;
  public static final float GROUND_HEIGHT = SIZE;
  public static final float WALL_HEIGHT   = SIZE * 2;

  public static final byte NONE          = 0;
  public static final byte LIGHT_GRASS   = 1;
  public static final byte SAND          = 2;
  public static final byte DARK_GRASS    = 3;
  public static final byte SNOW          = 4;
  public static final byte ROCK          = 5;
  public static final byte DEEP_WATER    = 6;
  public static final byte SHALLOW_WATER = 7;
  public static final byte LAVA          = 8;
  public static final byte DIRT          = 9;


  public static boolean isNextWall(byte currentTile, byte nextTile) {
    return (!isSolid(currentTile) && isSolid(nextTile));
  }

  public static boolean isNextNotWall(byte currentTile, byte nextTile) {
    return (isSolid(currentTile) && !isSolid(nextTile));
  }

  public static boolean isLiquid(byte tileID) {
    return (tileID == DEEP_WATER || tileID == LAVA || tileID == LAVA || tileID == SHALLOW_WATER || tileID == NONE);
  }

  public static boolean isWall(byte tileID) {
    return tileID == ROCK;
  }

  public static boolean isGround(byte tileID) {
    return !isWall(tileID) && !isLiquid(tileID);
  }

  public static float height(byte tileID) {
    if (isGround(tileID)) {
      return GROUND_HEIGHT;
    } else if (isLiquid(tileID)) {
      return LIQUID_BOTTOM_HEIGHT;
    } else {
      return WALL_HEIGHT;
    }
  }

  public static boolean isSolid(byte tileID) {
    return (tileID == ROCK);
  }

  public static boolean isNotEmpty(byte tileId) {
    return tileId != NONE;
  }

  public static boolean isNextNotLiquid(byte currentTile, byte nextTile) {
    return isLiquid(currentTile) && !isLiquid(nextTile);
  }

  public static boolean isNextLiquid(byte currentTile, byte nextTile) {
    return !isLiquid(currentTile) && isLiquid(nextTile);
  }

  public static byte waterWall(byte tile) {
    if (tile == LIGHT_GRASS || tile == DARK_GRASS || tile == ROCK) {
      return DIRT;
    } else {
      return tile;
    }
  }

  public static boolean isPassable(byte tileId) {
    return isNotEmpty(tileId) && isGround(tileId);
  }
}
