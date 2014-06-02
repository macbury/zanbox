package de.macbury.zanbox.persister;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layers.GroundLayer;
import de.macbury.zanbox.level.terrain.chunks.layers.Layer;
import de.macbury.zanbox.persister.serializers.GroundLayerSerializer;
import de.macbury.zanbox.persister.serializers.LayerSerializer;

import java.io.*;

/**
 * Created by macbury on 02.06.14.
 */
public class KryoSystem extends Kryo {

  private static final String MAIN_DIRECTORY = "zanbox/";
  private static final String TAG = "KryoSystem";

  public KryoSystem() {
    super();
    register(GroundLayer.class, new GroundLayerSerializer());
  }

  public static String fileName(Chunk chunk) {
    return "chunk_"+Integer.toHexString((int)chunk.position.x)+"x"+Integer.toHexString((int)chunk.position.y)+".ns";
  }

  public static FileHandle handle(Chunk chunk) {
    FileHandle handle = Gdx.files.external(MAIN_DIRECTORY);
    handle.mkdirs();
    handle = handle.child(folder(chunk.chunksProvider.level));
    handle.mkdirs();
    handle = handle.child(fileName(chunk));
    return handle;
  }

  public static boolean exists(Chunk chunk) {
    return handle(chunk).exists();
  }

  public static String folder(GameLevel level) {
    return "world_"+level.seed;
  }

  public void load(Chunk chunk) {
    FileHandle handle = handle(chunk);
    if (handle.exists()) {
      try {
        Gdx.app.log(TAG, "Loading "+ chunk.toString() +" to " + handle.file().getAbsolutePath());
        Input input = new Input(new FileInputStream(handle.file()));
        if (chunk.layers.size != 0) {
          throw new GdxRuntimeException("Should have no layers!!!");
        }
        chunk.position.set(input.readInt(), input.readInt());
        int layerCount = input.readInt();

        while(layerCount > 0) {
          Layer layer = readObject(input, GroundLayer.class);
          chunk.addLayer(layer);
          layerCount--;
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } else {
      throw new GdxRuntimeException("Non existing chunk to load: " + handle.path());
    }
  }

  public void save(Chunk chunk) {
    FileHandle handle = handle(chunk);

    try {
      Gdx.app.log(TAG, "Saving "+ chunk.toString() +" to " + handle.file().getAbsolutePath());
      OutputStream outputStream = new FileOutputStream(handle.file());
      Output output             = new Output(outputStream);

      output.writeInt((int)chunk.position.x);
      output.writeInt((int)chunk.position.y);
      output.writeInt(chunk.layers.size);
      for (Layer layer : chunk.layers) {
        writeObject(output, layer);
      }
      output.flush();
      output.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
