package de.macbury.zanbox.graphics.sprites.animated;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.graphics.sprites.BaseSprite3D;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.graphics.sprites.Sprite3DCache;

/**
 * Created by macbury on 27.05.14.
 */
public class AnimatedSprite3D extends BaseSprite3D {
  private Animation animation;
  private Sprite3DCache cachedKeyFrames[];
  private float stateTime;

  public AnimatedSprite3D(ModelAndSpriteBatch manager) {
    super(manager);
  }

  @Override
  public Mesh getMesh() {
    return cachedKeyFrames[animation.getKeyFrameIndex(stateTime)].mesh;
  }

  @Override
  public Material getMaterial() {
    return material;
  }

  public Animation getAnimation() {
    return animation;
  }

  public void setAnimation(Animation animation) {
    this.animation = animation;

    TextureRegion[] keyFrames = animation.getKeyFrames();
    cachedKeyFrames = new Sprite3DCache[keyFrames.length];
    Texture textureTest = keyFrames[0].getTexture();
    for(int i = 0; i < keyFrames.length; i++) {
      cachedKeyFrames[i] = manager.findOrInitializeSpriteCacheByMaterialAndTextureRegion(keyFrames[i]);
      if (textureTest != keyFrames[i].getTexture())
        throw new GdxRuntimeException("Not the same texture in keyframes!");
      textureTest = keyFrames[i].getTexture();
    }

    TextureRegion region = keyFrames[0];

    if (staticSprite) {
      this.material = manager.findOrInitializeMaterialForTextureRegion(region, blendingAttribute.blended);
    } else {
      textureAttribute.textureDescription.texture = region.getTexture();
    }
  }

  public void setStateTime(float stateTime) {
    this.stateTime = stateTime;
  }

  public void incStateTime(float delta) {
    this.stateTime += delta;
  }
}
