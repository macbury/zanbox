package de.macbury.zanbox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 30.05.14.
 */
public class GameCamera extends PerspectiveCamera {
  public static final int ROTATION = -78;
  private static final Vector3 temp = new Vector3();
  private static final float CAMERA_HEIGHT = 11f;
  private static final float CAMERA_OFFSET = Tile.GROUND_HEIGHT + 2;

  public GameCamera() {
    super(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    rotate(Vector3.X, ROTATION);
    far  = 16;
    near = 2f;
    update(true);

  }

  public void follow(Vector3 target) {
    temp.set(target).add(0, CAMERA_HEIGHT, CAMERA_OFFSET);
    position.set(temp);
  }
}
