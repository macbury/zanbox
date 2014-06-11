package de.macbury.zanbox.level.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.graphics.geometry.MeshVertexData;
import de.macbury.zanbox.graphics.materials.TerrainMaterial;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.biome.Biome;
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
  private HashMap<Biome, BiomeDefinition> biomeMap;

  public TileBuilder(GameLevel level) {
    super();
    this.level          = level;
    this.terrainAtlas   = Zanbox.assets.get(Assets.TERRAIN_TEXTURE);

    if (terrainAtlas.getTextures().size > 1)
      throw new GdxRuntimeException("Only supports one texture for terrain!!!!");

    this.tileMaterial      = Zanbox.materials.terrainMaterial;

    this.rockRegion        = new RandomRegion(this.terrainAtlas.findRegions("rock"));

    verifyAndSetupTiles();
  }

  private void verifyAndSetupTiles() {
    this.biomeMap          = new HashMap<Biome, BiomeDefinition>();

    Array<BiomeDefinition> defTemp  = new Array<BiomeDefinition>();

    Zanbox.assets.getAll(BiomeDefinition.class, defTemp);

    for (BiomeDefinition biomeDefinition : defTemp) {
      biomeDefinition.setup(terrainAtlas);
      if (biomeMap.containsKey(biomeDefinition.biome)) {
        throw new GdxRuntimeException("Already existing tile with biome: " + biomeDefinition.biome);
      } else {
        biomeMap.put(biomeDefinition.biome, biomeDefinition);
      }
    }

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

  public BiomeDefinition get(Biome biome) {
    if (!biomeMap.containsKey(biome)) {
      throw new GdxRuntimeException("Undefined tile biome: " + biome);
    }
    return biomeMap.get(biome);
  }

  public void topFace(float x, float y, float z, BiomeDefinition biome, boolean flip) {
    TextureRegion region = biome.faceTop;
    topFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2());
  }

  public void bottomFace(float x, float y, float z, BiomeDefinition biome, boolean flip) {
    TextureRegion region = biome.faceBottom;
    topFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2());
  }

  public void backFace(float x, float y, float z, BiomeDefinition biome, boolean outside) {
    TextureRegion region = biome.faceBack;
    backFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void frontFace(float x, float y, float z, BiomeDefinition biome, boolean outside) {
    TextureRegion region = biome.faceFront;
    frontFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void leftFace(float x, float y, float z, BiomeDefinition biome, boolean outside) {
    TextureRegion region = biome.faceLeft;
    leftFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void rightFace(float x, float y, float z, BiomeDefinition biome, boolean outside) {
    TextureRegion region = biome.faceRight;
    rightFace(x, y, z, Tile.SIZE, Tile.SIZE, Tile.SIZE, region.getU(), region.getV(), region.getU2(), region.getV2(), outside);
  }

  public void shadeBottom() {
    this.bottomLeftVertex.shade = true;
    this.bottomRightVertex.shade = true;
  }

  public void shadeTop() {
    this.topLeftVertex.shade = true;
    this.topRightVertex.shade = true;
  }

  public void shadeLeft() {
    this.topLeftVertex.shade = true;
    this.bottomLeftVertex.shade = true;
  }

  public void shadeRight() {
    this.topRightVertex.shade = true;
    this.bottomRightVertex.shade = true;
  }


}
