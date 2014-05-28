package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.graphics.sprites.normal.Sprite3D;

/**
 * Created by macbury on 28.05.14.
 */
public class SpriteComponent extends Component {
  public Sprite3D sprite;
  public Vector3 offset;

  public SpriteComponent(Sprite3D sprite) {
    this.sprite = sprite;
    this.offset = new Vector3();
  }
}
