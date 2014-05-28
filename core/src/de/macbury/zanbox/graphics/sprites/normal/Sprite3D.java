package de.macbury.zanbox.graphics.sprites.normal;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import de.macbury.zanbox.graphics.sprites.BaseSprite3D;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.graphics.sprites.Sprite3DCache;

/**
 * Created by macbury on 27.05.14.
 */
public class Sprite3D extends BaseSprite3D {
  protected Sprite3DCache spriteCache;
  protected TextureRegion textureRegion;
  public Sprite3D(ModelAndSpriteBatch manager) {
    super(manager);
  }

  public void setTextureRegion(TextureRegion textureRegion) {
    this.textureRegion = textureRegion;
    if (staticSprite) {
      this.material = manager.findOrInitializeMaterialForTextureRegion(textureRegion, blendingAttribute.blended);
    } else {
      textureAttribute.textureDescription.texture = textureRegion.getTexture();
    }
    spriteCache = manager.findOrInitializeSpriteCacheByMaterialAndTextureRegion(textureRegion);
  }

  @Override
  public Mesh getMesh() {
    return spriteCache.mesh;
  }

  @Override
  public Material getMaterial() {
    return material;
  }
}
