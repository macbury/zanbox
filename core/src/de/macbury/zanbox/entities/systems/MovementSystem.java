package de.macbury.zanbox.entities.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.entities.components.MovementComponent;
import de.macbury.zanbox.entities.components.PositionComponent;

/**
 * Created by macbury on 28.05.14.
 */
public class MovementSystem extends EntityProcessingSystem {
  @Mapper
  ComponentMapper<PositionComponent> positionMapper;
  @Mapper
  ComponentMapper<MovementComponent> movementMapper;

  private static Vector3 temp = new Vector3();

  public MovementSystem() {
    super(Aspect.getAspectForAll(PositionComponent.class, MovementComponent.class));
  }

  @Override
  protected void process(Entity entity) {
    PositionComponent positionComponent = positionMapper.get(entity);
    MovementComponent movementComponent = movementMapper.get(entity);

    positionComponent.vector.add(temp.set(movementComponent.direction).scl(movementComponent.speed * Gdx.graphics.getDeltaTime()));
  }
}
