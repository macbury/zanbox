package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 28.05.14.
 */
public class MovementComponent extends Component {
  public Vector3 direction;
  public float speed;

  public MovementComponent(float speed) {
    this.speed = 1f;
    direction = new Vector3();
  }
}
