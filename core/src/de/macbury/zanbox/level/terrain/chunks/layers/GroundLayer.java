package de.macbury.zanbox.level.terrain.chunks.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.biome.Biome;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layer.GeometryCache;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.chunks.layer.LayerSector;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 29.05.14.
 */
public class GroundLayer extends Layer {

  private static final String TAG = "GroundLayer";

  public GroundLayer() {
    super();
    setIndex(Layer.BASE_INDEX);
  }

  @Override
  protected void buildGeometryCaches(int tileStartX, int tileStartZ, boolean onlyBorder) {
    for(int x = 0; x < LayerSector.ROW_COUNT; x++) {
      for(int z = 0; z < LayerSector.ROW_COUNT; z++) {
        boolean border = (x == 0 || z == 0 || x == LayerSector.ROW_COUNT -1 || z == LayerSector.ROW_COUNT -1);

        int sx = x * LayerSector.SIZE_IN_TILES; //Local tile position in tiles table
        int sz = z * LayerSector.SIZE_IN_TILES;

        if (onlyBorder) {
          if (border) {
            buildPart(sx,sz, tileStartX, tileStartZ, border);
          }
        } else {
          buildPart(sx,sz, tileStartX, tileStartZ, border);
        }
      }
    }
  }

  @Override
  public void generateTiles(int tileStartX, int tileStartZ) {
    WorldBiomeProvider provider = chunk.chunksProvider.level.biomeProvider;

    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      for(int z = 0; z < Chunk.TILE_SIZE; z++) {
        Biome biome   = provider.getBiomeAt(tileStartX + x, tileStartZ + z);

        switch (biome) {
          case PLAINS:
            tiles[x][z] = Tile.LIGHT_GRASS;
          break;

          case FOREST:
            tiles[x][z] = Tile.DARK_GRASS;
          break;

          case DESERT:
            tiles[x][z] = Tile.SAND;
          break;

          case SNOW:
            tiles[x][z] = Tile.SNOW;
          break;

          case MOUNTAINS:
            tiles[x][z] = Tile.ROCK;
          break;

          case DEEP_WATER:
            tiles[x][z] = Tile.DEEP_WATER;
          break;

          case SHALLOW_WATER:
            tiles[x][z] = Tile.SHALLOW_WATER;
          break;

          case LAVA:
            tiles[x][z] = Tile.LAVA;
          break;

          default:
            throw new GdxRuntimeException("Undefined biome: " +biome.toString());
        }
      }
    }
  }

  public void buildPart(int sx, int sz, int tileStartX, int tileStartZ, boolean border) {
    ChunksProvider provider = chunk.chunksProvider;
    TileBuilder builder     = provider.level.tileBuilder;
    builder.begin(); {
      for(int x = 0; x < LayerSector.SIZE_IN_TILES; x++) {
        for(int z = 0; z < LayerSector.SIZE_IN_TILES; z++) {
          int tx = x + sx;
          int tz = z + sz;

          byte tile       = getTileByLocalTilePosition(tx, tz);
          byte topTile    = getTileByLocalToWorldTilePosition(tx, tz+1);
          byte bottomTile = getTileByLocalToWorldTilePosition(tx, tz-1);
          byte leftTile   = getTileByLocalToWorldTilePosition(tx-1, tz);
          byte rightTile  = getTileByLocalToWorldTilePosition(tx+1, tz);

          if (Tile.isLiquid(tile)) {

          } else {
            float y = Tile.height(tile);
            if (tile != Tile.NONE)
              builder.topFace(x, y, z, tile);
            if (Tile.isNextNotWall(tile,bottomTile))
              builder.backFace(x, 0, z, tile);
            if (Tile.isNextNotWall(tile,topTile))
              builder.frontFace(x, 0, z, tile);
            if (Tile.isNextNotWall(tile,leftTile))
              builder.leftFace(x, 0, z, tile);
            if (Tile.isNextNotWall(tile,rightTile))
              builder.rightFace(x, 0, z, tile);
          }
        }
      }
    } builder.end();

    LayerSector layerSector = getSector(sx, sz);
    if (layerSector == null) {
      layerSector = new LayerSector(sx, sz, this);
    }
    layerSector.setBoundingBox();
    layerSector.setTerrainGeometryCache(builder.toGeometryCache());

    if (!sectors.contains(layerSector, true)) {
      sectors.add(layerSector);
    }
    //sectors.add(renderable);

    //Gdx.app.log(TAG, "Builded part: " +layerSector.toString() + " tileStartXZ = " + tileStartX + "x" + tileStartZ);
  }


}
