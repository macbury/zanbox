package de.macbury.zanbox.entities.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.math.Vector3;

import de.macbury.zanbox.entities.components.MovementComponent;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.entities.managers.Tags;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.ui.Joystick;
import de.macbury.zanbox.utils.Direction;

/**
 * Created by macbury on 28.05.14.
 */
public class PlayerSystem extends VoidEntitySystem implements Joystick.JoystickListener {
  private static final String TAG = "PlayerSystem";
  private GameLevel level;
  @Mapper
  ComponentMapper<PositionComponent> positionMapper;
  @Mapper
  ComponentMapper<MovementComponent> movementMapper;

  private static Vector3 temp = new Vector3();
  private Entity player;
  private PositionComponent positionComponent;
  private MovementComponent movementComponent;

  public PlayerSystem(GameLevel gameLevel) {
    this.level = gameLevel;
  }

  @Override
  protected void processSystem() {
    if (player == null) {
      this.player = level.getManager(Tags.class).getEntity(Tags.PLAYER);

      this.positionComponent = positionMapper.get(player);
      this.movementComponent = movementMapper.get(player);
    }

    level.camera.follow(positionComponent.vector);
    //level.camera.lookAt(positionComponent.vector);

    level.worldPosition.set(positionComponent.vector);
   // Gdx.app.log(TAG, "Chunks: " + level.chunksProvider.size);
  }

  @Override
  public void onJoystickTouchDownDirection(Direction direction, Joystick joystick) {
    movementComponent.move(direction);
  }

  @Override
  public void onJoystickTouchUpDirection(Direction direction, Joystick joystick) {
    movementComponent.stop();
  }
}
