package de.macbury.zanbox.level.terrain.chunks.layer;

import de.macbury.zanbox.level.terrain.tiles.TileBuilder;

/**
 * Created by macbury on 04.06.14.
 */
public abstract class BuildGeometryPass {

  public abstract void buildTile(TileBuilder builder, GeometryCursor cursor);
  public abstract void onFinish(TileBuilder builder, LayerSector sector);

  public static class GeometryCursor {
    public int localX;
    public float localY;
    public int localZ;

    public int worldX;
    public int worldZ;

    public byte currentTile;
    public byte topTile;
    public byte bottomTile;
    public byte leftTile;
    public byte rightTile;
  }
}
