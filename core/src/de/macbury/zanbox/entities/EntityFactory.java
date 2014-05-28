package de.macbury.zanbox.entities;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.entities.components.MovementComponent;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.entities.components.SpriteComponent;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.managers.Assets;

/**
 * Created by macbury on 28.05.14.
 */
public class EntityFactory {
  private GameLevel level;

  public EntityFactory(GameLevel gameLevel) {
    this.level = gameLevel;
  }

  public Entity player() {
    Entity e = level.createEntity();
    e.addComponent(new PositionComponent(1,1,1));
    e.addComponent(new MovementComponent(1));
    TextureAtlas atlas = Zanbox.assets.get(Assets.CHARSET_TEXTURE);
    TextureRegion region = atlas.findRegions("dummy").get(0);
    SpriteComponent spriteComponent = new SpriteComponent(level.modelBatch.build(region, false, true));
    e.addComponent(spriteComponent);
    return e;
  }
}
