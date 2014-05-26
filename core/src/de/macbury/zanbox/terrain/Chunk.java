package de.macbury.zanbox.terrain;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 26.05.14.
 */
public class Chunk implements Disposable {
  public static final int SIZE = 10;
  public Vector3 position;

  public Chunk(int sx, int sz) {
    position = new Vector3(sx * SIZE, 0, sz * SIZE);
  }

  @Override
  public void dispose() {

  }
}
