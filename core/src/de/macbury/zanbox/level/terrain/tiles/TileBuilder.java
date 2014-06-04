package de.macbury.zanbox.level.terrain.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.materials.TerrainMaterial;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.graphics.geometry.MeshVertexData;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.chunks.layer.GeometryCache;
import de.macbury.zanbox.managers.Assets;

/**
 * Created by macbury on 29.05.14.
 */
public class TileBuilder extends MeshAssembler {
  private final TextureAtlas terrainAtlas;
  public final TerrainMaterial tileMaterial;
  private final Vector3 tempA = new Vector3();
  private final Vector3 tempB = new Vector3();
  private final RandomRegion rockRegion;
  private final GameLevel level;


  public TileBuilder(GameLevel level) {
    super();
    this.level          = level;
    this.terrainAtlas   = Zanbox.assets.get(Assets.TERRAIN_TEXTURE);

    if (terrainAtlas.getTextures().size > 1)
      throw new GdxRuntimeException("Only supports one texture for terrain!!!!");

    this.tileMaterial      = Zanbox.materials.terrainMaterial;

    this.rockRegion        = new RandomRegion(this.terrainAtlas.findRegions("rock"));
  }

  @Override
  public void begin() {
    super.begin();
    this.using(MeshVertexData.AttributeType.Position);
    this.using(MeshVertexData.AttributeType.TextureCord);
    this.using(MeshVertexData.AttributeType.Shading);
  }


  public GeometryCache toGeometryCache() {
    GeometryCache cache = new GeometryCache();
    cache.verties = verties;
    cache.indices = indices;
    cache.attributes = getVertexAttributes();
    return cache;
  }


  public TextureRegion regionForTile(byte tileID) {
    switch (tileID) {
      case Tile.LAVA:
        return terrainAtlas.findRegion("lava");
      case Tile.SAND:
        return terrainAtlas.findRegion("sand");
      case Tile.DIRT:
        return terrainAtlas.findRegion("dirt");
      case Tile.SNOW:
        return terrainAtlas.findRegion("snow"); //TODO: change this
      case Tile.ROCK:
        return rockRegion.get();
      case Tile.LIGHT_GRASS:
        return terrainAtlas.findRegion("light_grass");
      case Tile.DARK_GRASS:
        return terrainAtlas.findRegion("grass");
      case Tile.DEEP_WATER:
        return terrainAtlas.findRegion("water");
      case Tile.SHALLOW_WATER:
        return terrainAtlas.findRegion("shallow_water");//TODO: Animated and other type!
      default:
        throw new GdxRuntimeException("Undefined tile id: " + tileID);
    }
  }

  public void topFace(float x, float y, float z, byte tileID) {//TODO implement static height
    TextureRegion region = regionForTile(tileID);
    topFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2());
  }


  public void backFace(float x, float y, float z, byte tileID, boolean outside) {
    TextureRegion region = regionForTile(tileID);
    backFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void frontFace(float x, float y, float z, byte tileID, boolean outside) {
    TextureRegion region = regionForTile(tileID);
    frontFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void leftFace(float x, float y, float z, byte tileID, boolean outside) {
    TextureRegion region = regionForTile(tileID);
    leftFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void rightFace(float x, float y, float z, byte tileID, boolean outside) {
    TextureRegion region = regionForTile(tileID);
    rightFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

}
