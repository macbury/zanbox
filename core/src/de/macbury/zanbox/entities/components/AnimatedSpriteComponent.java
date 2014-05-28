package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;
import de.macbury.zanbox.graphics.sprites.animated.AnimatedSprite3D;
import de.macbury.zanbox.graphics.sprites.normal.Sprite3D;

/**
 * Created by macbury on 28.05.14.
 */

public class AnimatedSpriteComponent extends Component {
  public AnimatedSprite3D sprite;
  public Vector3 offset;
  public AnimatedSpriteComponent(AnimatedSprite3D sprite) {
    this.sprite = sprite;
    this.offset = new Vector3();
  }
}
