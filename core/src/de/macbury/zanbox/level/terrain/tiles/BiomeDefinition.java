package de.macbury.zanbox.level.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;

import de.macbury.zanbox.level.terrain.biome.Biome;

/**
 * Created by macbury on 11.06.14.
 */
public class BiomeDefinition {
  public String name;
  public boolean liquid;
  public boolean wall;
  public Biome biome;
  public HashMap<String, String> regions;

  private boolean dirty = true;

  public final static String FACE_TOP     = "top";
  public final static String FACE_FRONT   = "front";
  public final static String FACE_BACK    = "back";
  public final static String FACE_LEFT    = "left";
  public final static String FACE_RIGHT   = "right";
  public final static String FACE_BOTTOM  = "bottom";

  public TextureAtlas.AtlasRegion faceTop;
  public TextureAtlas.AtlasRegion faceFront;
  public TextureAtlas.AtlasRegion faceBack;
  public TextureAtlas.AtlasRegion faceLeft;
  public TextureAtlas.AtlasRegion faceRight;
  public TextureAtlas.AtlasRegion faceBottom;
  public void setup(TextureAtlas terrainAtlas) {
    if (!dirty)
      return;
    dirty           = false;
    this.faceTop    = terrainAtlas.findRegion(regionNameOrDefault(FACE_TOP));
    this.faceFront  = terrainAtlas.findRegion(regionNameOrDefault(FACE_FRONT));
    this.faceBack   = terrainAtlas.findRegion(regionNameOrDefault(FACE_BACK));
    this.faceLeft   = terrainAtlas.findRegion(regionNameOrDefault(FACE_LEFT));
    this.faceRight  = terrainAtlas.findRegion(regionNameOrDefault(FACE_RIGHT));
    this.faceBottom = terrainAtlas.findRegion(regionNameOrDefault(FACE_BOTTOM));
  }

  private String regionNameOrDefault(String face) {
    if (regions.containsKey(face)) {
      return regions.get(face);
    } else {
      return regions.get(FACE_TOP);
    }
  }

  public boolean isLiquid() {
    return liquid;
  }

  public boolean isWall() {
    return wall;
  }

  public float height() {
    if (isGround()) {
      return Tile.GROUND_HEIGHT;
    } else if (isLiquid()) {
      return Tile.LIQUID_BOTTOM_HEIGHT;
    } else {
      return Tile.WALL_HEIGHT;
    }
  }

  private boolean isGround() {
    return !isLiquid() && !isWall();
  }

  public boolean isNextNotLiquid(BiomeDefinition nextTile) {
    return isLiquid() && (nextTile != null && !nextTile.isLiquid());
  }

  public boolean isNextLiquid(BiomeDefinition nextTile) {
    return !isLiquid() && (nextTile != null && nextTile.isLiquid());
  }

  public boolean isNextWall(BiomeDefinition nextTile) {
    return (!isWall() && (nextTile != null && nextTile.isWall()));
  }

  public boolean isNextNotWall(BiomeDefinition nextTile) {
    return (isWall() && (nextTile != null && !nextTile.isWall()));
  }

  public boolean isPassable() {
    return isGround();
  }
}
