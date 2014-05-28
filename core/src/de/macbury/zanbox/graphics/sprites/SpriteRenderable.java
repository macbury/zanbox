package de.macbury.zanbox.graphics.sprites;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g3d.Renderable;

/**
 * Created by macbury on 28.05.14.
 */
public class SpriteRenderable extends Renderable {
  public void build(BaseSprite3D sprite) {
    this.material       = sprite.getMaterial();
    this.mesh           = sprite.getMesh();
    this.primitiveType  = GL30.GL_TRIANGLES;
    this.meshPartSize   = 6;
    this.meshPartOffset = 0;
    sprite.applyToMatrix(this.worldTransform);
  }
}
