package de.macbury.zanbox.level.terrain.chunk.layers;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.biome.Biome;
import de.macbury.zanbox.level.terrain.biome.Liquid;
import de.macbury.zanbox.level.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.level.terrain.chunk.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 29.05.14.
 */
public class GroundLayer extends Layer {
  public GroundLayer(Chunk chunk) {
    super(chunk, BASE_INDEX);
  }

  @Override
  public void generate(int startX, int startZ) {
    WorldBiomeProvider provider = chunk.chunks.level.biomeProvider;

    for(int x = 0; x < Chunk.SIZE; x++) {
      for(int z = 0; z < Chunk.SIZE; z++) {
        Biome biome   = provider.getBiomeAt(startX + x, startZ + z);

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

          default:
            throw new GdxRuntimeException("Undefined biome: " +biome.toString());
        }

      }
    }

    for(int x = 0; x < ChunkLayerPartRenderable.COUNT; x++) {
      for(int z = 0; z < ChunkLayerPartRenderable.COUNT; z++) {
        buildPart(x,z, startX, startZ);
      }
    }
  }

  public void buildPart(int offsetX, int offsetZ, int startX, int startZ) {
    int sx = offsetX * ChunkLayerPartRenderable.SIZE; //Local tile position in tiles table
    int sz = offsetZ * ChunkLayerPartRenderable.SIZE;

    int ex = sx + ChunkLayerPartRenderable.SIZE;
    int ez = sz + ChunkLayerPartRenderable.SIZE;

    TileBuilder builder = chunk.chunks.level.tileBuilder;
    builder.begin(); {

      for(int x = sx; x < ex; x++) {
        for(int z = sz; z < ez; z++) {
          byte tileID = tiles[x][z];

          builder.topFace(x,index,z, tileID);
        }
      }
    } builder.end();

    ChunkLayerPartRenderable renderable = builder.getRenderable();
    renderable.worldTransform.idt();

    MyMath.tilePositionToPosition(tempA.set(startX, 0, startZ), tempB);
    renderable.worldTransform.translate(tempB);

    renderables.add(renderable);
  }
}
