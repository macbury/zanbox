package de.macbury.zanbox.level.terrain.chunks.layers;

import de.macbury.zanbox.level.terrain.biome.Biome;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.chunks.layer.LayerSector;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.level.terrain.tiles.BiomeDefinition;
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
    TileBuilder builder         = chunk.chunksProvider.level.tileBuilder;

    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      for(int z = 0; z < Chunk.TILE_SIZE; z++) {
        Biome biome         = provider.getBiomeAt(tileStartX + x, tileStartZ + z);
        tiles[x][z]         = builder.get(biome);
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

          BiomeDefinition tile       = provider.getTile(worldX, worldZ, index);//getTileByLocalTilePosition(tx, tz);
          BiomeDefinition topTile    = provider.getTile(worldX, worldZ+1, index);
          BiomeDefinition bottomTile = provider.getTile(worldX, worldZ-1, index);
          BiomeDefinition leftTile   = provider.getTile(worldX-1, worldZ, index);
          BiomeDefinition rightTile  = provider.getTile(worldX+1, worldZ, index);

          BiomeDefinition topLeftTile     = provider.getTile(worldX-1, worldZ+1, index);
          BiomeDefinition topRightTile    = provider.getTile(worldX+1, worldZ+1, index);
          BiomeDefinition bottomRightTile = provider.getTile(worldX+1, worldZ-1, index);
          BiomeDefinition bottomLeftTile  = provider.getTile(worldX-1, worldZ-1, index);

          float y = tile.height();

          if (tile.isLiquid()) {
            if (tile.isNextNotLiquid(topTile)) {
              builder.frontFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, tile, true);
              builder.shadeBottom();
            }

            if (tile.isNextNotLiquid(bottomTile)) {
              builder.backFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, bottomTile, true);
              builder.shadeBottom();
            }

            if (tile.isNextNotLiquid(leftTile)) {
              builder.leftFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, leftTile, true);
              builder.shadeBottom();
            }

            if (tile.isNextNotLiquid(rightTile)) {
              builder.rightFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, rightTile, true);
              builder.shadeBottom();
            }

            builder.bottomFace(x, Tile.LIQUID_BOTTOM_HEIGHT, z, tile, false);

            if (tile.isNextNotLiquid(topTile)) {
              builder.shadeBottom();
            }

            if (tile.isNextNotLiquid(bottomTile)) {
              builder.shadeTop();
            }

            if (tile.isNextNotLiquid(leftTile)) {
              builder.shadeLeft();
            }

            if (tile.isNextNotLiquid(rightTile)) {
              builder.shadeRight();
            }

            if (tile.isNextNotLiquid(topLeftTile)) {
              builder.bottomLeftVertex.shade = true;
            }

            if (tile.isNextNotLiquid(bottomLeftTile)) {
              builder.topLeftVertex.shade = true;
            }

            if (tile.isNextNotLiquid(topRightTile)) {
              builder.bottomRightVertex.shade = true;
            }

            if (tile.isNextNotLiquid(bottomRightTile)) {
              builder.topRightVertex.shade = true;
            }
          } else {
            if (tile != null) {
              builder.topFace(x, y, z, tile, true);

              if (tile.isNextWall(topTile)) {
                builder.shadeBottom();
              }

              if (tile.isNextWall(bottomTile)) {
                builder.shadeTop();
              }

              if (tile.isNextWall(leftTile)) {
                builder.shadeLeft();
              }

              if (tile.isNextWall(rightTile)) {
                builder.shadeRight();
              }

              if (tile.isNextWall(topLeftTile)) {
                builder.bottomLeftVertex.shade = true;
              }

              if (tile.isNextWall(bottomLeftTile)) {
                builder.topLeftVertex.shade = true;
              }

              if (tile.isNextWall(topRightTile)) {
                builder.bottomRightVertex.shade = true;
              }

              if (tile.isNextWall(bottomRightTile)) {
                builder.topRightVertex.shade = true;
              }
            }

            if (tile.isNextNotWall(bottomTile)) {
              builder.backFace(x, Tile.GROUND_HEIGHT, z, tile, false);
              if (!tile.isNextLiquid(bottomTile))
                builder.shadeBottom();
            }

            if (tile.isNextNotWall(topTile)) {
              builder.frontFace(x, Tile.GROUND_HEIGHT, z, tile, false);
              if (!tile.isNextLiquid(topTile))
                builder.shadeBottom();
            }

            if (tile.isNextNotWall(leftTile)) {
              builder.leftFace(x, Tile.GROUND_HEIGHT, z, tile, false);
              if (!tile.isNextLiquid(leftTile))
                builder.shadeBottom();
            }

            if (tile.isNextNotWall(rightTile)) {
              builder.rightFace(x, Tile.GROUND_HEIGHT, z, tile, false);
              if (!tile.isNextLiquid(rightTile))
                builder.shadeBottom();
            }

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

          MyMath.localToWorldTilePosition(chunk, tempA.set(tx, 0, tz), tempB);
          int worldX = (int)tempB.x;
          int worldZ = (int)tempB.z;

          BiomeDefinition tile = provider.getTile(worldX, worldZ, index);
          if(tile.isLiquid())
            builder.topFace(x, Tile.LIQUID_HEIGHT, z, tile, false);
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
