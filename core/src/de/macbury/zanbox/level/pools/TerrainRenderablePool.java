package de.macbury.zanbox.level.pools;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.graphics.renderables.TerrainRenderable;

public class TerrainRenderablePool extends Pool<TerrainRenderable> {
  private final GameLevel level;
  protected Array<TerrainRenderable> obtained = new Array<TerrainRenderable>();

  public TerrainRenderablePool(GameLevel level) {
    this.level = level;
  }

  @Override
  protected TerrainRenderable newObject () {
    return new TerrainRenderable();
  }

  @Override
  public TerrainRenderable obtain () {
    TerrainRenderable renderable = super.obtain();
    renderable.environment       = level.env;
    renderable.material          = level.tileBuilder.tileMaterial;
    renderable.mesh              = null;
    renderable.shader            = null;
    renderable.worldTransform.idt();
    obtained.add(renderable);
    return renderable;
  }

  public void flush () {
    super.freeAll(obtained);
    obtained.clear();
  }
}