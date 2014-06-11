package de.macbury.zanbox.persister.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.level.terrain.biome.Biome;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 02.06.14.
 */
public abstract class LayerSerializer extends Serializer<Layer> {
  @Override
  public void write(Kryo kryo, Output output, Layer object) {
    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      for(int z = 0; z < Chunk.TILE_SIZE; z++) {
        kryo.writeObject(output, object.tiles[x][z].biome);
      }
    }
  }

  public void loadTiles(Kryo kryo, Input input, Layer layer) {
    TileBuilder builder = Zanbox.level.tileBuilder;

    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      for(int z = 0; z < Chunk.TILE_SIZE; z++) {
        Biome biome       = kryo.readObject(input, Biome.class);
        layer.tiles[x][z] = builder.get(biome);
      }
    }
  }
}
