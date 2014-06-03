package de.macbury.zanbox.level.pools;

import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 03.06.14.
 */
public class Pools implements Disposable {
  public TerrainRenderablePool terrainRenderables;

  public Pools(GameLevel level) {
    this.terrainRenderables = new TerrainRenderablePool(level);
  }

  @Override
  public void dispose() {
    terrainRenderables.flush();
    terrainRenderables.clear();
  }
}
