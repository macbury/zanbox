package de.macbury.zanbox.level.terrain.chunk;

import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 27.05.14.
 */
public class ChunkGeometryBuilder implements Disposable {

  private GameLevel gameLevel;
  private MeshAssembler builder;

  public ChunkGeometryBuilder(GameLevel gameLevel){
    this.gameLevel = gameLevel;
    this.builder = new MeshAssembler();
  }

  @Override
  public void dispose() {
    builder.dispose();
  }
}
