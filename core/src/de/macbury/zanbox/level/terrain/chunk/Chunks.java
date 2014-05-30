package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.WorldEnv;
import de.macbury.zanbox.utils.MyMath;

/**
 * Created by macbury on 27.05.14.
 */
public class Chunks extends Array<Chunk> implements Disposable {
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
  public GameLevel level;
  private Vector2 temp = new Vector2();
  private Chunk currentChunk;
  private Array<Chunk> visibleChunks;

  public Chunks(GameLevel gameLevel) {
    this.level = gameLevel;
    this.visibleChunks = new Array<Chunk>();
  }

  public Chunk getByPosition(Vector3 position) {
    MyMath.positionToChunkPosition(position, temp);
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
    Chunk chunk = new Chunk((int)position.x,(int)position.y);
    chunk.setChunks(this);
    chunk.generate();//TODO: check if can load chunk from db
    this.add(chunk);
    return chunk;
  }

  @Override
  public void dispose() {
    clear();
  }

  public void update(float delta) {
    currentChunk = null;
    visibleChunks.clear();

    currentChunk = getByPosition(level.worldPosition);

    for(int i = 0; i < CHUNKS_OFFSET_AROUND.length; i++) {
      temp.set(currentChunk.position).add(CHUNKS_OFFSET_AROUND[i]);
      get(temp);
    }

    for(Chunk chunk : this) {
      if (level.camera.frustum.boundsInFrustum(chunk.getBoundingBox())) {
        visibleChunks.add(chunk);
      }
      //TODO: find chunks visible on screen!
      //TODO: find chunks around or create
      //TODO: if chunk dont exists for part of screen generate it
      //TODO: unload chunks that are out of screen and are farthest from it and if chunk size exceeds MAX_CHUNKS_IN_MEMORY
    }
  }

  public void render(ModelAndSpriteBatch modelBatch) {
    for(Chunk chunk : visibleChunks) {
      chunk.render(modelBatch);
    }
  }
}
