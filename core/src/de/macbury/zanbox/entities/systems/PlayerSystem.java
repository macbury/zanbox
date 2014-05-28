package de.macbury.zanbox.entities.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import de.macbury.zanbox.entities.components.MovementComponent;
import de.macbury.zanbox.entities.components.PositionComponent;
import de.macbury.zanbox.entities.managers.Tags;
import de.macbury.zanbox.graphics.stage.InGameStage;
import de.macbury.zanbox.graphics.stage.MainInGameUIListener;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 28.05.14.
 */
public class PlayerSystem extends VoidEntitySystem implements MainInGameUIListener {
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

    level.camera.position.set(temp.set(positionComponent.vector).add(0, 6,1));
    level.camera.lookAt(positionComponent.vector);

    level.chunks.getByPosition(positionComponent.vector, 0); //TODO: handle level

   // Gdx.app.log(TAG, "Chunks: " + level.chunks.size);
  }

  @Override
  public void onPlayerMovementChange(Vector3 direction, InGameStage stage) {
    movementComponent.direction.set(direction);
  }
}
