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
import de.macbury.zanbox.level.terrain.chunks.layer.Layer;
import de.macbury.zanbox.level.terrain.chunks.layer.LayerSector;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 02.06.14.
 */
public class ChunksRenderables implements RenderableProvider, Disposable {
  private ChunksProvider chunksProvider;
  private GameLevel level;
  public Array<Chunk> visibleChunks;

  private Vector2 tempA = new Vector2();
  private Chunk currentChunk;
  private Vector3 minVector = new Vector3();
  private Vector3 maxVector = new Vector3();
  public BoundingBox boundingBox;
  public Array<LayerSector> totalSectors;
  public Array<LayerSector> visibleSectors;

  public ChunksRenderables(GameLevel level) {
    this.level              = level;
    this.chunksProvider     = level.chunksProvider;
    this.visibleChunks      = new Array<Chunk>();
    this.visibleSectors     = new Array<LayerSector>();
    this.totalSectors       = new Array<LayerSector>();
    this.boundingBox        = new BoundingBox();
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    for(LayerSector sector : visibleSectors)
      sector.getRenderables(renderables, pool);
  }

  public void cull() {
    totalSectors.clear();
    visibleChunks.clear();
    visibleSectors.clear();
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
        chunk.setVisible(true);
        Layer layer = chunk.getLayer(level.currentLayer);

        if (layer != null) {
          for(int i = 0; i < layer.sectors.size; i++) {
            LayerSector sector = layer.sectors.get(i);
            if (frustum.boundsInFrustum(sector.boundingBox)) {
              if (firstPart) {
                minVector.set(sector.boundingBox.min);
                maxVector.set(sector.boundingBox.max);
                firstPart = false;
              } else {
                minVector.set(MyMath.min(sector.boundingBox.min, minVector));
                maxVector.set(MyMath.max(sector.boundingBox.max, maxVector));
              }

              visibleSectors.add(sector);
            }
            totalSectors.add(sector);
          }
        }
      } else {
        chunk.setVisible(false);
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
    visibleSectors.clear();
  }

}
