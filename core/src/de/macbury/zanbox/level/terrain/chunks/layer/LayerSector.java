package de.macbury.zanbox.level.terrain.chunks.layer;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.renderables.TerrainRenderable;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 03.06.14.
 */
public class LayerSector implements RenderableProvider, Disposable {
  public static final int SIZE_IN_TILES     = 8;
  public static final float SIZE_IN_METERS  = SIZE_IN_TILES * Tile.SIZE;
  public static final int ROW_COUNT         = Chunk.TILE_SIZE / SIZE_IN_TILES;
  public static final int TOTAL_COUNT       = ROW_COUNT * ROW_COUNT;

  private Layer parent;

  public BoundingBox boundingBox;
  public boolean border = false;

  private int offsetX;
  private int offsetZ;
  private TerrainRenderable groundRenderable;
  private TerrainRenderable liquidRenderable;
  private GeometryCache groundGeometryCache;
  private GeometryCache liquidGeometryCache;

  protected final Vector3 tempA = new Vector3();
  protected final Vector3 tempB = new Vector3();

  public LayerSector(int ox, int oz, Layer layer) {
    this.parent            = layer;
    this.offsetX           = ox;
    this.offsetZ           = oz;
  }

  public void setBoundingBox() {
    Vector3 boundingStartVector = new Vector3();
    Vector3 boundingEndVector   = new Vector3();

    MyMath.tilePositionToMeters(tempA.set(offsetX,0, offsetZ), tempB);
    MyMath.chunkPositionToMeters(parent.chunk.position, tempA);
    tempB.add(tempA);

    boundingStartVector.set(tempB);
    boundingEndVector.set(boundingStartVector).add(SIZE_IN_METERS, 0, SIZE_IN_METERS);
    boundingEndVector.y    = Tile.WALL_HEIGHT;
    boundingStartVector.y  = Tile.LIQUID_BOTTOM_HEIGHT;
    boundingBox            = new BoundingBox(boundingStartVector, boundingEndVector);
  }

  @Override
  public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
    updateGeometryCacheIfNeeded();
    updateLiquidGeometryCacheIfNeeded();
    if (groundRenderable != null)
      renderables.add(groundRenderable);
    if (liquidRenderable != null)
      renderables.add(liquidRenderable);
  }

  private void updateLiquidGeometryCacheIfNeeded() {
    if (liquidGeometryCache != null) {

      if (liquidRenderable == null) {
        this.liquidRenderable = parent.chunk.chunksProvider.level.pools.terrainRenderables.obtain();
      }

      if (liquidRenderable.mesh != null) {
        liquidRenderable.mesh.dispose();
      }

      MyMath.tilePositionToMeters(tempA.set(offsetX, 0, offsetZ), tempB);
      MyMath.chunkPositionToMeters(parent.chunk.position, tempA);
      tempB.add(tempA);

      liquidRenderable.material  = Zanbox.materials.liquidMaterial;
      liquidRenderable.worldTransform.idt().translate(tempB);
      liquidRenderable.setGeometryCache(liquidGeometryCache);
      liquidGeometryCache = null;
    }
  }

  private void updateGeometryCacheIfNeeded() {
    if (groundGeometryCache != null) {

      if (groundRenderable == null) {
        this.groundRenderable = parent.chunk.chunksProvider.level.pools.terrainRenderables.obtain();
      }

      if (groundRenderable.mesh != null) {
        groundRenderable.mesh.dispose();
      }

      MyMath.tilePositionToMeters(tempA.set(offsetX, 0, offsetZ), tempB);
      MyMath.chunkPositionToMeters(parent.chunk.position, tempA);
      tempB.add(tempA);
      groundRenderable.material  = Zanbox.materials.terrainMaterial;
      groundRenderable.worldTransform.idt().translate(tempB);
      groundRenderable.setGeometryCache(groundGeometryCache);
      groundGeometryCache = null;
    }
  }

  public boolean at(int x, int z) {
    return offsetX == x && offsetZ == z;
  }

  @Override
  public String toString() {
    return boundingBox.toString() + " border: " +border;
  }

  @Override
  public void dispose() {
    //terrainRenderable
    if (groundRenderable != null)
      parent.chunk.chunksProvider.level.pools.terrainRenderables.free(groundRenderable);
    if (liquidRenderable != null)
      parent.chunk.chunksProvider.level.pools.terrainRenderables.free(liquidRenderable);
    parent               = null;
    groundGeometryCache = null;
    liquidRenderable = null;
    groundRenderable    = null;
    liquidGeometryCache = null;
    boundingBox = null;
  }

  public GeometryCache getGroundGeometryCache() {
    return groundGeometryCache;
  }

  public void setGroundGeometryCache(GeometryCache groundGeometryCache) {
    this.groundGeometryCache = groundGeometryCache;
  }

  public void setLiquidGeometryCache(GeometryCache liquidGeometryCache) {
    this.liquidGeometryCache = liquidGeometryCache;
  }
}
