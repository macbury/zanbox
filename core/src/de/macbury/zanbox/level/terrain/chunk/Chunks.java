package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 27.05.14.
 */
public class Chunks extends Array<Chunk> implements Disposable {
  public ChunkGeometryBuilder chunksBuilder;
  public GameLevel gameLevel;
  private Vector2 temp = new Vector2();
  public Chunks(GameLevel gameLevel) {
    this.gameLevel = gameLevel;
    this.chunksBuilder = new ChunkGeometryBuilder(gameLevel);
  }

  public Chunk getByPosition(Vector3 position, int layer) {
    MyMath.positionToChunkPosition(position, temp);
    return get(temp, layer);
  }

  public Chunk getByTilePosition(Vector3 tilePosition, int layer) {
    MyMath.tilePositionToChunkPoistion(tilePosition, temp);
    return get(temp, layer);
  }

  // Get chunk by chunk position
  public Chunk get(Vector2 position, int layer) {
    for(Chunk chunk : this) {
      if (chunk.at(position.x,position.y,layer)) {
        return chunk;
      }
    }
    //TODO: check if can load chunk from db
    Chunk chunk = new Chunk((int)position.x,(int)position.y,layer);
    chunk.setChunks(this);
    chunk.rebuild();
    this.add(chunk);
    return chunk;
  }

  @Override
  public void dispose() {
    clear();
  }

  public void update(float delta) {
    for(Chunk chunk : this) {
      chunk.update(delta);
    }
  }
}
