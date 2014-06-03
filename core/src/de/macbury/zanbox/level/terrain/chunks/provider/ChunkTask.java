package de.macbury.zanbox.level.terrain.chunks.provider;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.terrain.chunks.Chunk;

/**
 * Created by macbury on 02.06.14.
 */
public abstract class ChunkTask implements Disposable {
  public Chunk chunk;
  private ChunksProvider provider;

  public ChunkTask(Chunk chunk) {
    this.chunk = chunk;
  }

  public void start() {
    synchronized (chunk) {
      if (chunk.isLocked()) {
        throw new GdxRuntimeException("Chunk already locked!!!");
      }
      chunk.lock();
    }
  }

  public abstract void async();

  @Override
  public void dispose() {
    chunk.unlock();
    chunk = null;
  }

  public static ChunkTask generateChunk(Chunk chunk) {
    return new ChunkTask(chunk) {
      @Override
      public void async() {
        chunk.load();
        getProvider().enqueueTask(ChunkTask.buildGeometry(chunk));
      }
    };
  }

  public static ChunkTask buildGeometry(Chunk chunk) {
    return new ChunkTask(chunk) {
      @Override
      public void async() {
        chunk.buildGeometry(false);
      }
    };
  }


  public void setProvider(ChunksProvider provider) {
    this.provider = provider;
  }

  public ChunksProvider getProvider() {
    return provider;
  }
}
