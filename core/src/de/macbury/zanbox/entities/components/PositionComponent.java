package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 28.05.14.
 */
public class PositionComponent extends Component {
  public Vector3 vector;

  public PositionComponent(float x, float y, float z) {
    vector = new Vector3(x,y,z);
  }
}
