package de.macbury.zanbox.persister.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;

/**
 * Created by macbury on 02.06.14.
 */
public abstract class LayerSerializer extends Serializer<Layer> {
  @Override
  public void write(Kryo kryo, Output output, Layer object) {
    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      output.write(object.tiles[x]);
    }
  }

  public void loadTiles(Kryo kryo, Input input, Layer layer) {
    for(int x = 0; x < Chunk.TILE_SIZE; x++) {
      layer.tiles[x] = input.readBytes(Chunk.TILE_SIZE);
    }
  }
}
