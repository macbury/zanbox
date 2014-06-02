package de.macbury.zanbox.level.terrain.chunks;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.chunks.layers.ChunkLayerPartRenderable;
import de.macbury.zanbox.level.terrain.chunks.layers.Layer;
import de.macbury.zanbox.utils.MyMath;
import de.macbury.zanbox.utils.time.BaseTimer;
import de.macbury.zanbox.utils.time.FrameTickTimer;
import de.macbury.zanbox.utils.time.TimerListener;

/**
 * Created by macbury on 02.06.14.
 */
public class ChunksRenderables implements RenderableProvider, Disposable, TimerListener {
  private static final int REBUILD_GEOMETRY_EVERY_FRAME = 4;
  private FrameTickTimer rebuildChunkFpsTicker;
  private Array<Chunk> chunksToRebuild;
  private ChunksProvider chunksProvider;
  private GameLevel level;
  public Array<Chunk> visibleChunks;
  public Array<ChunkLayerPartRenderable> visibleRenderables;
  private Vector2 tempA = new Vector2();
  private Chunk currentChunk;
  private Vector3 minVector = new Vector3();
  private Vector3 maxVector = new Vector3();
  public BoundingBox boundingBox;
  public Array<Renderable> totalRenderables;

  public ChunksRenderables(GameLevel level) {
    this.level              = level;
    this.chunksProvider     = level.chunksProvider;
    this.visibleChunks      = new Array<Chunk>();
    this.visibleRenderables = new Array<ChunkLayerPartRenderable>();
    this.totalRenderables   = new Array<Renderable>();
    this.chunksToRebuild    = new Array<Chunk>();
    this.chunksToRebuild    = new Array<Chunk>();
    this.boundingBox        = new BoundingBox();

    this.rebuildChunkFpsTicker = new FrameTickTimer(REBUILD_GEOMETRY_EVERY_FRAME);
    this.rebuildChunkFpsTicker.setListener(this);
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    renderables.addAll(visibleRenderables);
  }


  public void cull() {
    rebuildChunkFpsTicker.update();
    totalRenderables.clear();
    visibleChunks.clear();
    visibleRenderables.clear();
    minVector.set(Vector3.Zero);
    maxVector.set(Vector3.Zero);

    Frustum frustum = level.frustrumRenderer.isEnabled() ? level.frustrumRenderer.getFrustrum() : level.camera.frustum;

    MyMath.metersToChunkPosition(level.worldPosition, tempA);

    currentChunk = chunksProvider.findOrRegister((int)tempA.x, (int)tempA.y);

    for(int i = 0; i < ChunksProvider.CHUNKS_OFFSET_AROUND.length; i++) {
      tempA.set(currentChunk.position).add( ChunksProvider.CHUNKS_OFFSET_AROUND[i]);
      chunksProvider.findOrRegister((int)tempA.x, (int)tempA.y);
    }

    boolean firstPart = true;

    for(Chunk chunk : chunksProvider.chunks) {
      if (frustum.boundsInFrustum(chunk.getBoundingBox())) {
        visibleChunks.add(chunk);
        if (chunk.needsToRebuildGeometry() && !chunksToRebuild.contains(chunk, true)) {
          chunksToRebuild.add(chunk);
        }
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
          totalRenderables.add(renderable);
        }
      }
      //TODO: find chunksProvider visible on screen!
      //TODO: find chunksProvider around or create
      //TODO: if chunk dont exists for part of screen rebuild it
      //TODO: unload chunksProvider that are out of screen and are farthest from it and if chunk size exceeds MAX_CHUNKS_IN_MEMORY
    }

    boundingBox.set(minVector, maxVector);
  }

  @Override
  public void dispose() {
    visibleChunks.clear();
    visibleRenderables.clear();
  }

  @Override
  public void timerTick(BaseTimer sender) {
    if (sender == rebuildChunkFpsTicker && chunksToRebuild.size > 0) {
      chunksToRebuild.pop().reBuildGeometry();
    }
  }
}
