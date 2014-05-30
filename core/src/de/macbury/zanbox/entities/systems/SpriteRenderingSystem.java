package de.macbury.zanbox.entities.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.entities.components.AnimatedSpriteComponent;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.entities.components.SpriteComponent;
import de.macbury.zanbox.graphics.GameCamera;
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
  @Mapper
  ComponentMapper<AnimatedSpriteComponent> animatedSpriteMapper;
  private static Vector3 temp = new Vector3();

  public SpriteRenderingSystem(ModelAndSpriteBatch batch) {
    super(Aspect.getAspectForAll(PositionComponent.class).one(SpriteComponent.class, AnimatedSpriteComponent.class));
    this.batch = batch;
  }

  @Override
  protected void process(Entity entity) {
    PositionComponent positionComponent             = positionMapper.get(entity);
    SpriteComponent   spriteComponent               = spriteMapper.get(entity);
    AnimatedSpriteComponent animatedSpriteComponent = animatedSpriteMapper.get(entity);

    if (animatedSpriteComponent != null) {
      animatedSpriteComponent.sprite.incStateTime(Gdx.graphics.getDeltaTime());
      temp.set(positionComponent.vector).add(animatedSpriteComponent.offset);
      animatedSpriteComponent.sprite.set(temp);
      animatedSpriteComponent.sprite.setRotationX(GameCamera.ROTATION);
      //animatedSpriteComponent.sprite.setRotation(temp.set(-0.38f, 0,0));
      batch.render(animatedSpriteComponent.sprite);
    } else {
      temp.set(positionComponent.vector).add(spriteComponent.offset);
      spriteComponent.sprite.set(temp);
      spriteComponent.sprite.setRotationX(GameCamera.ROTATION);
      batch.render(spriteComponent.sprite);
    }


  }
}
