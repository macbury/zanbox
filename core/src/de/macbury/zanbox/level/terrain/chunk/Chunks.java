package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.chunks.layers.ChunkLayerPartRenderable;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.utils.MyMath;
import de.macbury.zanbox.utils.time.BaseTimer;
import de.macbury.zanbox.utils.time.FrameTickTimer;
import de.macbury.zanbox.utils.time.TimerListener;

/**
 * Created by macbury on 27.05.14.
 */
public class Chunks extends Array<Chunk> implements Disposable, RenderableProvider, TimerListener {
  private static final String TAG = "Chunk";
  private static final Vector2[] CHUNKS_OFFSET_AROUND = {
    new Vector2(-1,1),
    new Vector2(0,1),
    new Vector2(1,1),
    new Vector2(-1,0),
    new Vector2(1,0),
    new Vector2(-1,-1),
    new Vector2(0,-1),
    new Vector2(1,-1),
  };

  public static final int MAX_CHUNKS_IN_MEMORY = 9;
  private final FrameTickTimer rebuildChunksTimer;
  public GameLevel level;
  private Vector2 temp = new Vector2();
  private Chunk currentChunk;
  public Array<Chunk> visibleChunks;
  public BoundingBox visibilityRect;
  public Array<ChunkLayerPartRenderable> visibleRenderables;
  public Array<ChunkLayerPartRenderable> totalRenderables;
  public Array<Chunk> toRebuild;

  public Chunks(GameLevel gameLevel) {
    this.level              = gameLevel;
    this.visibleChunks      = new Array<Chunk>();
    this.visibilityRect     = new BoundingBox();
    this.visibleRenderables = new Array<ChunkLayerPartRenderable>();
    this.totalRenderables   = new Array<ChunkLayerPartRenderable>();
    this.toRebuild          = new Array<Chunk>();
    this.rebuildChunksTimer = new FrameTickTimer(3);
    this.rebuildChunksTimer.setListener(this);
  }

  public Chunk getByPosition(Vector3 position) {
    MyMath.metersToChunkPosition(position, temp);
    return get(temp);
  }

  public Chunk getByTilePosition(Vector3 tilePosition) {
    MyMath.tilePositionToChunkPoistion(tilePosition, temp);
    return get(temp);
  }

  // Get chunk by chunk position
  public Chunk get(Vector2 position) {
    for(Chunk chunk : this) {
      if (chunk.at(position.x,position.y)) {
        return chunk;
      }
    }

    return generateOrLoadForPosition(position);
  }

  public Chunk generateOrLoadForPosition(Vector2 position) {
    Gdx.app.log(TAG, "Creating chunk for: " + position.toString());
    Chunk chunk          = new Chunk((int)position.x,(int)position.y);
    //chunk.chunksProvider = this;
    //chunk.build();//TODO: check if can load chunk from db
    //chunk.setStatus(Chunk.Status.Generate);
    this.add(chunk);
    return chunk;
  }

  @Override
  public void dispose() {
    clear();
  }

  private Vector3 minVector = new Vector3();
  private Vector3 maxVector = new Vector3();

  public void initializeBaseChunks() {
    Vector3 startPosition = new Vector3();
    getByPosition(startPosition);

    for(int i = 0; i < CHUNKS_OFFSET_AROUND.length; i++) {
      temp.set(startPosition.x, startPosition.y).add(CHUNKS_OFFSET_AROUND[i]);
      get(temp);
    }
  }

  public void update(float delta) {
    rebuildChunksTimer.update(delta);
    currentChunk = null;
    visibleChunks.clear();
    visibleRenderables.clear();
    totalRenderables.clear();

    currentChunk = getByPosition(level.worldPosition);

    for(int i = 0; i < CHUNKS_OFFSET_AROUND.length; i++) {
      temp.set(currentChunk.position).add(CHUNKS_OFFSET_AROUND[i]);
      get(temp);
    }

    Frustum frustum = level.frustrumRenderer.isEnabled() ? level.frustrumRenderer.getFrustrum() : level.camera.frustum;

    minVector.set(Vector3.Zero);
    maxVector.set(Vector3.Zero);

    boolean firstPart = true;
    /*
    for(Chunk chunk : this) {
      if (frustum.boundsInFrustum(chunk.getBoundingBox())) {
        if (chunk.requireRebuild() && !toRebuild.contains(chunk, true)) {
          toRebuild.add(chunk);
        }
        visibleChunks.add(chunk);

        Layer layer = chunk.getLayer(level.currentLayer);

        for(ChunkLayerPartRenderable renderable : layer.renderables) {
          if (frustum.boundsInFrustum(renderable.boundingBox)) {
            if (firstPart) {
              minVector.set(renderable.boundingBox.min);
              maxVector.set(renderable.boundingBox.max);
              firstPart = false;
            } else {
              minVector.set(MyMath.min(renderable.boundingBox.min, minVector));
              maxVector.set(MyMath.max(renderable.boundingBox.max, maxVector));
            }

            visibleRenderables.add(renderable);
          }

          //Gdx.app.log(TAG, renderable.toString());
          totalRenderables.add(renderable);
        }
      }
      //TODO: find chunksProvider visible on screen!
      //TODO: find chunksProvider around or create
      //TODO: if chunk dont exists for part of screen rebuild it
      //TODO: unload chunksProvider that are out of screen and are farthest from it and if chunk size exceeds MAX_CHUNKS_IN_MEMORY
    }
*/
    visibilityRect.set(minVector, maxVector);
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    renderables.addAll(visibleRenderables);
  }

  @Override
  public void timerTick(BaseTimer sender) {
    if (sender == rebuildChunksTimer && toRebuild.size > 0) {
      Chunk chunk = toRebuild.pop();
      if (chunk != null) {
        //chunk.process();
      }
    }
  }
}
