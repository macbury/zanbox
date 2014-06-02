package de.macbury.zanbox.graphics;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 30.05.14.
 */
public class GameCamera extends PerspectiveCamera {
  public static final int ROTATION = -80;
  private static final Vector3 temp = new Vector3();
  public GameCamera() {
    super(90, Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight());
    rotate(Vector3.X, ROTATION);
    far  = 10;
    near = 2f;
    update(true);
  }

  public void follow(Vector3 target) {
    temp.set(target).add(0, 7, 2);
    position.set(temp);
  }
}
