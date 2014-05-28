package de.macbury.zanbox.graphics.sprites;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * Created by macbury on 27.05.14.
 */
public abstract class BaseSprite3D {
  protected boolean staticSprite;
  protected TextureAttribute textureAttribute;
  protected BlendingAttribute blendingAttribute;
  protected ModelAndSpriteBatch manager;

  protected Vector3 position;
  protected Quaternion rotation = new Quaternion();
  protected Vector2 scale       = new Vector2(1, 1);
  protected Material material;
  protected Matrix4 transform   = new Matrix4();
  protected boolean dirty       = true;

  public BaseSprite3D(ModelAndSpriteBatch manager) {
    this.manager           = manager;
    this.position          = new Vector3();
    this.blendingAttribute = new BlendingAttribute(false, 1f);
    this.textureAttribute  = new TextureAttribute(TextureAttribute.Diffuse);
  }

  protected void init(boolean transparent, boolean staticSprite) {
    setTransparent(transparent);
    this.staticSprite      = staticSprite;
    if (!staticSprite) {
      this.material          = new Material(blendingAttribute, textureAttribute);
    }
  }

  public void applyToMatrix(Matrix4 matrix) {
    if (dirty) {
      transform.idt();
      transform.translate(position);
      transform.scl(scale.x, scale.y, 1);
      transform.rotate(rotation);
      dirty = false;
    }

    matrix.set(transform);
  }

  public void set(float x, float y, float z) {
    position.set(x,y,z);
    dirty = true;
  }

  public void setTransparent(boolean transparent) {
    if (this.staticSprite) {
      throw new GdxRuntimeException("Cannot change transparency for static sprite!");
    }
    blendingAttribute.blended         = transparent;
    blendingAttribute.destFunction    = GL20.GL_ONE_MINUS_SRC_ALPHA;
    blendingAttribute.sourceFunction  = GL20.GL_SRC_ALPHA;
  }

  public void scale(float x, float y) {
    scale.set(x,y);
    dirty = true;
  }


  public abstract Mesh getMesh();
  public abstract Material getMaterial();
}
