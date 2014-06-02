package de.macbury.zanbox.entities.systems;

import com.artemis.systems.VoidEntitySystem;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 02.06.14.
 */
public class CullingSystem extends VoidEntitySystem {
  private final GameLevel level;

  public CullingSystem(GameLevel level) {
    this.level = level;
  }

  @Override
  protected void processSystem() {
    level.chunksRenderables.cull();
  }
}
