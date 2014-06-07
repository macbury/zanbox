package de.macbury.zanbox.utils;

import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 07.06.14.
 */
public enum Direction {
  None(new Vector3(0, 0,0)),
  Up(new Vector3(0, 0, -1)),
  Down(new Vector3(0, 0,1)),
  Left(new Vector3(-1, 0,0)),
  Right(new Vector3(1,0,0));

  private Vector3 vector;
  Direction(Vector3 dirVector) {
    vector = dirVector;
  }

  public Vector3 getVector() {
    return vector;
  }
}