package de.macbury.zanbox.level.terrain.chunks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 02.06.14.
 */
public abstract class ChunkTask implements Disposable {
  public Chunk chunk;

  public ChunkTask(Chunk chunk) {
    this.chunk = chunk;
    if (chunk.isLocked()) {
      throw new GdxRuntimeException("Chunk already locked!!!");
    }
    chunk.lock();
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
        chunk.buildTiles(false);
        final Chunk chunkCache = chunk;
        Gdx.app.postRunnable(new Runnable() {
          @Override
          public void run() {
            chunkCache.buildGeometry(false);
          }
        });
      }
    };
  }
}
