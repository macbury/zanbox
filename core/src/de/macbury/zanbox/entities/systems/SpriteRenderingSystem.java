package de.macbury.zanbox.entities.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.entities.components.SpriteComponent;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;

/**
 * Created by macbury on 28.05.14.
 */
public class SpriteRenderingSystem extends EntityProcessingSystem {
  private ModelAndSpriteBatch batch;

  @Mapper
  ComponentMapper<PositionComponent> positionMapper;
  @Mapper
  ComponentMapper<SpriteComponent> spriteMapper;

  private static Vector3 temp = new Vector3();

  public SpriteRenderingSystem(ModelAndSpriteBatch batch) {
    super(Aspect.getAspectForAll(PositionComponent.class).one(SpriteComponent.class));
    this.batch = batch;
  }

  @Override
  protected void process(Entity entity) {
    PositionComponent positionComponent = positionMapper.get(entity);
    SpriteComponent   spriteComponent   = spriteMapper.get(entity);

    temp.set(positionComponent.vector).add(spriteComponent.offset);
    spriteComponent.sprite.set(temp);
    batch.render(spriteComponent.sprite);
  }
}
