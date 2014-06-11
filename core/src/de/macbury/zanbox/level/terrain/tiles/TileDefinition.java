package de.macbury.zanbox.level.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.HashMap;

import de.macbury.zanbox.level.terrain.biome.Biome;

/**
 * Created by macbury on 11.06.14.
 */
public class TileDefinition {
  public byte uuid;
  public String name;
  public boolean liquid;
  public boolean wall;
  public Biome biome;
  public HashMap<String, String> regions;

  private boolean dirty = true;

  public final static String FACE_TOP    = "top";
  public final static String FACE_FRONT  = "front";
  public final static String FACE_BACK   = "back";
  public final static String FACE_LEFT   = "left";
  public final static String FACE_RIGHT  = "right";
  public TextureAtlas.AtlasRegion faceTop;
  public TextureAtlas.AtlasRegion faceFront;
  public TextureAtlas.AtlasRegion faceBack;
  public TextureAtlas.AtlasRegion faceLeft;
  public TextureAtlas.AtlasRegion faceRight;

  public void setup(TextureAtlas terrainAtlas) {
    if (!dirty)
      return;
    dirty          = false;
    this.faceTop   = terrainAtlas.findRegion(regionNameOrDefault(FACE_TOP));
    this.faceFront = terrainAtlas.findRegion(regionNameOrDefault(FACE_FRONT));
    this.faceBack  = terrainAtlas.findRegion(regionNameOrDefault(FACE_BACK));
    this.faceLeft  = terrainAtlas.findRegion(regionNameOrDefault(FACE_LEFT));
    this.faceRight = terrainAtlas.findRegion(regionNameOrDefault(FACE_RIGHT));
  }

  private String regionNameOrDefault(String face) {
    if (regions.containsKey(face)) {
      return regions.get(face);
    } else {
      return regions.get(FACE_TOP);
    }
  }
}
