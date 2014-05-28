package de.macbury.zanbox.graphics.stage;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

/**
 * Created by macbury on 28.05.14.
 */
public interface MainInGameUIListener {
  public void onPlayerMovementChange(Vector3 direction, InGameStage stage);
}
