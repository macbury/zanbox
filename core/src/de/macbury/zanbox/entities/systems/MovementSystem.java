package de.macbury.zanbox.entities.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector3;

import de.macbury.zanbox.entities.components.MovementComponent;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 28.05.14.
 */
public class MovementSystem extends EntityProcessingSystem {
  private GameLevel level;
  @Mapper
  ComponentMapper<PositionComponent> positionMapper;
  @Mapper
  ComponentMapper<MovementComponent> movementMapper;

  private static Vector3 temp = new Vector3();

  public MovementSystem(GameLevel level) {
    super(Aspect.getAspectForAll(PositionComponent.class, MovementComponent.class));
    this.level = level;
  }

  @Override
  protected void process(Entity entity) {
    PositionComponent positionComponent = positionMapper.get(entity);
    MovementComponent movementComponent = movementMapper.get(entity);

    if (movementComponent.locked()) {
      movementComponent.step(world.delta);
      temp.set(movementComponent.start).lerp(movementComponent.target, movementComponent.alpha);
      positionComponent.vector.set(temp);
      if (movementComponent.finishedLock()) {
        movementComponent.resetMovement();
      }
    }

    if (!movementComponent.locked() && movementComponent.isMoving()) {
      moveToNextTile(positionComponent, movementComponent);
    }
  }

  public void moveToNextTile(PositionComponent positionComponent, MovementComponent movementComponent) {
    temp.set(positionComponent.vector).add(movementComponent.direction.getVector());
    byte nextTileID = level.chunksProvider.getTile(temp);
    if (Tile.isPassable(nextTileID)) {
      movementComponent.lock(positionComponent.vector, temp);
    } else {
      movementComponent.stop();
    }

  }
}
