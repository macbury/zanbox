package de.macbury.zanbox.level.terrain.chunks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 02.06.14.
 */
public class ChunksProvider implements Disposable {
  public GameLevel level;
  public static final Vector2[] CHUNKS_OFFSET_AROUND = {
          new Vector2(-1,1),
          new Vector2(0,1),
          new Vector2(1,1),
          new Vector2(-1,0),
          new Vector2(1,0),
          new Vector2(-1,-1),
          new Vector2(0,-1),
          new Vector2(1,-1),
  };
  private ChunksThread thread;
  public Array<Chunk> chunks;
  private Array<ChunkTask> tasks;
  private Chunk lastChunk;
  private ChunkTask currentTask = null;
  private Vector2 temp = new Vector2();

  public ChunksProvider(GameLevel level) {
    this.level  = level;
    this.chunks = new Array<Chunk>();
    this.tasks  = new Array<ChunkTask>();
    this.thread = new ChunksThread();
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.start();
  }

  public void initializeBaseChunks() {
    Vector3 startPosition = new Vector3();
    register(startPosition.x, startPosition.y);

    for(int i = 0; i < CHUNKS_OFFSET_AROUND.length; i++) {
      temp.set(startPosition.x, startPosition.y).add(CHUNKS_OFFSET_AROUND[i]);
      register(temp.x, temp.y);
    }
  }

  public boolean exists(int chunkX, int chunkY) {
    return get(chunkX, chunkY) != null;
  }

  public Chunk get(int chunkX, int chunkY) {
    if (lastChunk != null && lastChunk.at(chunkX, chunkY)) {
      return lastChunk;
    }

    for(Chunk chunk : chunks) {
      if (chunk.at(chunkX,chunkY)) {
        lastChunk = chunk;
        return chunk;
      }
    }
    return null;
  }

  public Chunk get(float chunkX, float chunkY) {
    return get((int) chunkX, (int) chunkY);
  }

  public Chunk register(float chunkX, float chunkY) {
    return register((int)chunkX, (int)chunkY);
  }

  public Chunk register(int chunkX, int chunkY) {
    if (exists(chunkX, chunkY)) {
      throw new GdxRuntimeException("Chunk already exists!");
    } else {
      Chunk chunk = new Chunk(chunkX, chunkY);
      chunk.chunksProvider = this;
      chunks.add(chunk);
      enqueueTask(ChunkTask.generateChunk(chunk));
      return chunk;
    }
  }

  public Chunk findOrRegister(int chunkX, int chunkY) {
    if (exists(chunkX, chunkY)){
      return get(chunkX, chunkY);
    } else {
      return register(chunkX, chunkY);
    }
  }

  public void enqueueTask(ChunkTask task) {
    tasks.add(task);
  }

  @Override
  public void dispose() {
    thread.interrupt();
  }

  private class ChunksThread extends Thread {

    private static final String TAG = "ChunksThread";

    @Override
    public void run() {
      while(!this.isInterrupted()) {
        Array<ChunkTask> tasks = ChunksProvider.this.tasks;
        synchronized (tasks) {
          if (tasks.size > 0) {
            currentTask = tasks.get(0);
          } else {
            currentTask = null;
          }
        }

        if (currentTask == null) {
          try {
            Thread.sleep(250);
          } catch (InterruptedException e) {
            break;
          }
        } else {
          Gdx.app.debug(TAG, "Processing task...");
          try {
            Thread.sleep(50);
          } catch (InterruptedException e) {
            break;
          }
          currentTask.async();

          synchronized (tasks) {
            tasks.removeValue(currentTask, true);
          }

          currentTask.dispose();
          currentTask = null;
        }
      }
    }
  }
}
