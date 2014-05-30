package de.macbury.zanbox.graphics;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 30.05.14.
 */
public class GameCamera extends PerspectiveCamera {
  public static final int ROTATION = -80;
  public GameCamera() {
    super(67, Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight());
    rotate(Vector3.X, ROTATION);
  }
}
