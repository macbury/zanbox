package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

import de.macbury.zanbox.utils.Direction;

/**
 * Created by macbury on 28.05.14.
 */
public class MovementComponent extends Component {
  public Direction direction = Direction.None;
  public Vector3 target;
  public Vector3 start;
  public boolean lockOnTarger;
  public float speed;
  public float alpha = 0.0f;
  private boolean move;

  public MovementComponent(float speed) {
    this.speed = speed;
    target    = new Vector3();
    start     = new Vector3();
  }

  public void resetMovement() {
    lockOnTarger = false;
    alpha        = 0.0f;
  }

  public void lock(Vector3 begin, Vector3 nextTarget) {
    target.set(nextTarget);
    start.set(begin);
    lockOnTarger = true;
  }

  public void step(float delta) {
    alpha += speed * delta;
    alpha = Math.min(alpha, 1.0f);
  }

  public boolean isStill() {
    return !move;
  }
  public boolean isMoving() {
    return move;
  }

  public void moveLeft() {
    move(Direction.Left);
  }

  public void move(Direction inDirection) {
    direction = inDirection;
    move = true;
  }

  public void stop() {
    move = false;
    direction = Direction.None;
  }

  public boolean locked() {
    return lockOnTarger;
  }

  public boolean finishedLock() {
    return alpha >= 1.0f;
  }
}
