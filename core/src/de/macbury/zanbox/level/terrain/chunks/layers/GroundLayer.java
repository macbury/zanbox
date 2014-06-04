package de.macbury.zanbox.level.terrain.chunks.layers;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.biome.Biome;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
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
  protected void buildGeometryCaches(boolean onlyBorder) {
    buildGroundGeometryCaches(onlyBorder);
    buildLiquidGeometryCaches(onlyBorder);
  }

  private void buildLiquidGeometryCaches(boolean onlyBorder) {
    for(int x = 0; x < LayerSector.ROW_COUNT; x++) {
      for(int z = 0; z < LayerSector.ROW_COUNT; z++) {
        boolean border = (x == 0 || z == 0 || x == LayerSector.ROW_COUNT -1 || z == LayerSector.ROW_COUNT -1);

        int sx = x * LayerSector.SIZE_IN_TILES; //Local tile position in tiles table
        int sz = z * LayerSector.SIZE_IN_TILES;

        if (onlyBorder) {
          if (border) {
            buildLiquidPart(sx, sz);
          }
        } else {
          buildLiquidPart(sx, sz);
        }
      }
    }
  }


  public void buildGroundGeometryCaches(boolean onlyBorder) {
    for(int x = 0; x < LayerSector.ROW_COUNT; x++) {
      for(int z = 0; z < LayerSector.ROW_COUNT; z++) {
        boolean border = (x == 0 || z == 0 || x == LayerSector.ROW_COUNT -1 || z == LayerSector.ROW_COUNT -1);

        int sx = x * LayerSector.SIZE_IN_TILES; //Local tile position in tiles table
        int sz = z * LayerSector.SIZE_IN_TILES;

        if (onlyBorder) {
          if (border) {
            buildGroundPart(sx, sz);
          }
        } else {
          buildGroundPart(sx, sz);
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

  public void buildGroundPart(int sx, int sz) {
    ChunksProvider provider = chunk.chunksProvider;
    TileBuilder builder     = provider.level.tileBuilder;
    builder.begin(); {
      for(int x = 0; x < LayerSector.SIZE_IN_TILES; x++) {
        for(int z = 0; z < LayerSector.SIZE_IN_TILES; z++) {
          int tx = x + sx;
          int tz = z + sz;

          MyMath.localToWorldTilePosition(chunk, tempA.set(tx, 0, tz), tempB);
          int worldX = (int)tempB.x;
          int worldZ = (int)tempB.z;

          byte tile       = provider.getTile(worldX, worldZ, index);//getTileByLocalTilePosition(tx, tz);
          byte topTile    = provider.getTile(worldX, worldZ+1, index);
          byte bottomTile = provider.getTile(worldX, worldZ-1, index);
          byte leftTile   = provider.getTile(worldX-1, worldZ, index);
          byte rightTile  = provider.getTile(worldX+1, worldZ, index);

          float y = Tile.height(tile);

          if (Tile.isLiquid(tile)) {
            if (Tile.isNextNotLiquid(tile, topTile))
              builder.frontFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, Tile.DIRT, true);
            if (Tile.isNextNotLiquid(tile, bottomTile))
              builder.backFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, Tile.DIRT, true);
            if (Tile.isNextNotLiquid(tile, leftTile))
              builder.leftFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, Tile.DIRT, true);
            if (Tile.isNextNotLiquid(tile,rightTile))
              builder.rightFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, Tile.DIRT, true);
            builder.topFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, Tile.DIRT);
          } else {
            if (tile != Tile.NONE)
              builder.topFace(x, y, z, tile);
            if (Tile.isNextNotWall(tile, bottomTile))
              builder.backFace(x, Tile.GROUND_HEIGHT, z, tile, false);
            if (Tile.isNextNotWall(tile, topTile))
              builder.frontFace(x, Tile.GROUND_HEIGHT, z, tile, false);
            if (Tile.isNextNotWall(tile, leftTile))
              builder.leftFace(x, Tile.GROUND_HEIGHT, z, tile, false);
            if (Tile.isNextNotWall(tile,rightTile))
              builder.rightFace(x, Tile.GROUND_HEIGHT, z, tile, false);
          }
        }
      }
    } builder.end();

    LayerSector layerSector = getSector(sx, sz);
    if (layerSector == null) {
      layerSector = new LayerSector(sx, sz, this);
    }
    layerSector.setBoundingBox();
    layerSector.setGroundGeometryCache(builder.toGeometryCache());

    if (!sectors.contains(layerSector, true)) {
      sectors.add(layerSector);
    }
    //sectors.add(renderable);

    //Gdx.app.log(TAG, "Builded part: " +layerSector.toString() + " tileStartXZ = " + tileStartX + "x" + tileStartZ);
  }


  private void buildLiquidPart(int sx, int sz) {
    ChunksProvider provider = chunk.chunksProvider;
    TileBuilder builder     = provider.level.tileBuilder;
    builder.begin(); {
      for(int x = 0; x < LayerSector.SIZE_IN_TILES; x++) {
        for(int z = 0; z < LayerSector.SIZE_IN_TILES; z++) {
          int tx = x + sx;
          int tz = z + sz;

          byte tile       = getTileByLocalTilePosition(tx, tz);

          builder.topFace(x, Tile.LIQUID_HEIGHT, z, tile);
        }
      }
    } builder.end();

    LayerSector layerSector = getSector(sx, sz);
    if (layerSector == null) {
      layerSector = new LayerSector(sx, sz, this);
    }
    layerSector.setBoundingBox();

    if (builder.isEmpty()) {
      layerSector.setLiquidGeometryCache(null);
    } else {
      layerSector.setLiquidGeometryCache(builder.toGeometryCache());
    }

    if (!sectors.contains(layerSector, true)) {
      sectors.add(layerSector);
    }
  }

}
