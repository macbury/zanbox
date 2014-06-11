package de.macbury.zanbox.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 30.05.14.
 */
public class GameCamera extends PerspectiveCamera {
  public static final int ROTATION = -80;
  private static final Vector3 temp = new Vector3();
  public GameCamera() {
    super(90, Math.max(Gdx.graphics.getWidth() / 2, 480), Math.max(Gdx.graphics.getHeight() / 2, 320));
    rotate(Vector3.X, ROTATION);
    far  = 16;
    near = 2f;
    update(true);
  }

  public void follow(Vector3 target) {
    temp.set(target).add(0, 7f, Tile.GROUND_HEIGHT + 2);
    position.set(temp);
  }
}
