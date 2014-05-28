package de.macbury.zanbox.terrain.chunk;

import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.terrain.World;

/**
 * Created by macbury on 27.05.14.
 */
public class ChunkGeometryBuilder implements Disposable {

  private World world;
  private MeshAssembler builder;

  public ChunkGeometryBuilder(World world){
    this.world   = world;
    this.builder = new MeshAssembler();
  }

  @Override
  public void dispose() {
    builder.dispose();
  }
}
