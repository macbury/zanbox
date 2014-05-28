package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.utils.FPSLoop;

/**
 * Created by macbury on 26.05.14.
 */
public class Chunk implements Disposable, FPSLoop.FPSLoopListener {
  public static final int SIZE = 64;
  public int tx; //position tile
  public int tz;
  public int level = 0;
  private byte[][] tiles;
  private FPSLoop rebuildLoop;
  private Chunks chunks;

  public Chunk(int sx, int sz, int layer) {
    tx           = sx * SIZE;
    tz           = sz * SIZE;
    level        = layer;
    tiles        = new byte[SIZE][SIZE];
    rebuildLoop  = new FPSLoop(3);
    rebuildLoop.setListener(this);
  }

  public void rebuild() {
    rebuildLoop.start(SIZE * SIZE);
  }

  @Override
  public void dispose() {

  }

  public void update(float delta) {
    rebuildLoop.tick();
  }

  @Override
  public void onFPSLoopStart(FPSLoop loop) {

  }

  @Override
  public boolean onFPSLoopTick(FPSLoop loop, int ticks) {
    //TODO: Perlin noise builder
    return false;
  }

  @Override
  public void onFPSLoopFinish(FPSLoop loop) {
    //TODO: Build geometry here
  }

  public Chunks getChunks() {
    return chunks;
  }

  public void setChunks(Chunks chunks) {
    this.chunks = chunks;
  }

  public boolean at(float x, float z, int layer) {
    return at((int)x, (int)z, layer);
  }

  public boolean at(int x, int z, int layer) {
    return (this.tx == x && this.tz == z && this.level == layer);
  }

  public void render(ModelBatch modelBatch) {

  }
}
