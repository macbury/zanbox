package de.macbury.zanbox.persister.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import de.macbury.zanbox.level.terrain.chunks.layers.GroundLayer;
import de.macbury.zanbox.level.terrain.chunks.layers.Layer;

/**
 * Created by macbury on 02.06.14.
 */
public class GroundLayerSerializer extends LayerSerializer {
  @Override
  public Layer read(Kryo kryo, Input input, Class<Layer> type) {
    GroundLayer layer = new GroundLayer();
    loadTiles(kryo, input, layer);
    return layer;
  }
}
