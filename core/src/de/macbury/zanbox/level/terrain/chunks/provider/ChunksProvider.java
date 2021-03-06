package de.macbury.zanbox.level.terrain.chunks.provider;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Comparator;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.events.Event;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.tiles.BiomeDefinition;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 02.06.14.
 */
public class ChunksProvider implements Disposable, Thread.UncaughtExceptionHandler {
  private static final String TAG = "ChunkProvider";
  private static final int MAX_CHUNKS_IN_MEMORY         = 18;
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
  public GameLevel level;
  private Array<ChunkTask> tasks;
  private Chunk lastChunk;
  private ChunkTask currentTask = null;
  private Vector2 tempA = new Vector2();
  private Vector3 tempB = new Vector3();
  private boolean dirtyChunks = true;

  public ChunksProvider(GameLevel level) {
    this.level  = level;
    this.chunks = new Array<Chunk>();
    this.tasks  = new Array<ChunkTask>();
    this.thread = new ChunksThread();
    thread.setPriority(Thread.MIN_PRIORITY);
    thread.setUncaughtExceptionHandler(this);
    thread.start();
  }

  public void initializeBaseChunks() {
    Vector3 startPosition = new Vector3();
    register(startPosition.x, startPosition.y);

    for(int i = 0; i < CHUNKS_OFFSET_AROUND.length; i++) {
      tempA.set(startPosition.x, startPosition.y).add(CHUNKS_OFFSET_AROUND[i]);
      register(tempA.x, tempA.y);
    }
  }

  public void update() {
    if (dirtyChunks)
      unloadChunksThatExceedLimit();
  }

  public BiomeDefinition getTile(int tileX, int tileY, int layerIndex) {
    MyMath.tilePositionToChunkPoistion(tempB.set(tileX, 0, tileY), tempA);
    if (exists(tempA.x, tempA.y)) {
      Chunk chunk = get(tempA.x, tempA.y);
      Layer layer = chunk.getLayer(layerIndex);

      if (layer == null) {
        return null;
      } else {
        return layer.getTileByWorldTilePosition(tileX, tileY);
      }
    } else {
      return null;
    }
  }


  public BiomeDefinition getTile(float x, float z, int index) {
    return getTile((int)x, (int)z, index);
  }

  public BiomeDefinition getTile(Vector3 tileVector) {
    return getTile(Math.round(tileVector.x), Math.round(tileVector.z), level.currentLayer);
  }

  public boolean exists(float chunkX, float chunkY) {
    return exists((int)chunkX, (int)chunkY);
  }

  public boolean exists(int chunkX, int chunkY) {
    return get(chunkX, chunkY) != null;
  }

  public Chunk get(int chunkX, int chunkY) {
    if (lastChunk != null && lastChunk.at(chunkX, chunkY)) {
      return lastChunk;
    }

    for(int ci = 0; ci < chunks.size; ci++) {
      Chunk chunk = chunks.get(ci);
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
      Chunk chunk          = new Chunk(chunkX, chunkY);
      chunk.chunksProvider = this;
      chunks.add(chunk);
      this.dirtyChunks = true;
      enqueueTask(ChunkTask.generateChunk(chunk));
      return chunk;
    }
  }

  public void unloadChunksThatExceedLimit() {
    int diff = chunks.size - MAX_CHUNKS_IN_MEMORY;
    if (diff > 0) {
      Gdx.app.log(TAG, "Exceed limit for chunks. Unloading invisible chunks: " + diff);
      chunks.sort(chunkCameraComparator);
      int i = 0;
      while(true) {
        if (i > chunks.size)
          break;
        if (diff <= 0)
          break;

        Chunk chunk = chunks.get(i);
        if (!chunk.isVisible() && !chunk.isLocked()) {
          chunks.removeIndex(i);
          chunk.unload();
          diff--;
          Gdx.app.debug(TAG, "Unloaded: " + chunk.toString());
        }

        i++;

        level.events.trigger(Event.ChunksUpdated);
      }
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
    synchronized (tasks) {
      tasks.add(task);
    }
  }

  @Override
  public void dispose() {
    thread.interrupt();
    for(Chunk chunk : chunks)
      chunk.dispose();
    chunks.clear();
  }


  @Override
  public void uncaughtException(Thread t, Throwable e) {
    e.printStackTrace();
    throw new GdxRuntimeException("Somethin went terrible wrong...");
  }

  private class ChunksThread extends Thread {

    private static final String TAG = "ChunksThread";

    public void enqueueTask(ChunkTask task) {
      ChunksProvider.this.enqueueTask(task);
    }

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
            Thread.sleep(50);
          } catch (InterruptedException e) {
            break;
          }
        } else {
          Gdx.app.debug(TAG, "Processing task...");
          try {
            Thread.sleep(5);
          } catch (InterruptedException e) {
            break;
          }
          currentTask.setProvider(ChunksProvider.this);
          currentTask.start();
          currentTask.async();
          currentTask.setProvider(null);

          synchronized (tasks) {
            tasks.removeValue(currentTask, true);
          }

          currentTask.dispose();
          currentTask = null;
        }
      }
    }
  }

  private Comparator chunkCameraComparator = new Comparator<Chunk>() {
    @Override
    public int compare(Chunk a, Chunk b) {
      Vector3 worldCenter = Zanbox.level.worldPosition;
      float aDst = worldCenter.dst(a.worldPosition);
      float bDst = worldCenter.dst(b.worldPosition);

      if (aDst == bDst) {
        return 0;
      } else if (aDst > bDst) {
        return -1;
      } else {
        return 1;
      }
    }
  };
}
