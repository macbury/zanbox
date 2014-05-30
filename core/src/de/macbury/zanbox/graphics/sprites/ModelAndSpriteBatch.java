package de.macbury.zanbox.graphics.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.graphics.sprites.animated.AnimatedSprite3D;
import de.macbury.zanbox.graphics.sprites.normal.Sprite3D;
import de.macbury.zanbox.graphics.sprites.shaders.ShaderProvider;
import de.macbury.zanbox.level.terrain.WorldEnv;

/**
 * Created by macbury on 27.05.14.
 */
public class ModelAndSpriteBatch extends ModelBatch {
  private static final String TAG = "Sprite3DManager";
  private Array<Material> materials;
  private Array<Sprite3DCache> spriteCache;
  private WorldEnv env;
  private Array<SpriteRenderable> usedRenderables;


  protected static class SpriteRenderablePool extends Pool<SpriteRenderable> {
    protected Array<SpriteRenderable> obtained = new Array<SpriteRenderable>();

    @Override
    protected SpriteRenderable newObject () {
      return new SpriteRenderable();
    }

    @Override
    public SpriteRenderable obtain () {
      SpriteRenderable renderable = super.obtain();
      renderable.environment = null;
      renderable.material = null;
      renderable.mesh = null;
      renderable.shader = null;
      obtained.add(renderable);
      return renderable;
    }

    public void flush () {
      super.freeAll(obtained);
      obtained.clear();
    }
  }

  private SpriteRenderablePool spriteRenderablePool = new SpriteRenderablePool();

  public ModelAndSpriteBatch(RenderContext renderContext) {
    super(renderContext, new ShaderProvider());
    materials   = new Array<Material>();
    spriteCache = new Array<Sprite3DCache>();
    usedRenderables = new Array<SpriteRenderable>();
  }

  public Sprite3D build(TextureRegion region, boolean isStatic, boolean transparent) {
    Sprite3D sprite3D = new Sprite3D(this);
    sprite3D.init(transparent, isStatic);
    sprite3D.setTextureRegion(region);
    return sprite3D;
  }

  public AnimatedSprite3D build(Animation animation, boolean isStatic, boolean transparent) {
    AnimatedSprite3D sprite3D = new AnimatedSprite3D(this);
    sprite3D.init(transparent, isStatic);
    sprite3D.setAnimation(animation);
    return sprite3D;
  }

  public Material findOrInitializeMaterialForTextureRegion(TextureRegion region, boolean transparent) {
    Texture texture = region.getTexture();
    Material found  = null;
    for(Material material : materials) {
      TextureAttribute textureAttribute = (TextureAttribute)material.get(TextureAttribute.Diffuse);
      BlendingAttribute blendingAttribute = (BlendingAttribute)material.get(BlendingAttribute.Type);
      if (textureAttribute != null && textureAttribute.textureDescription.texture == texture && blendingAttribute != null && blendingAttribute.blended == transparent) {
        found = material;
        break;
      }
    }

    if (found == null) {
      found = new Material(new BlendingAttribute(transparent, 1f),TextureAttribute.createDiffuse(region.getTexture()));
      materials.add(found);
    }

    return found;
  }

  public Sprite3DCache findOrInitializeSpriteCacheByMaterialAndTextureRegion(TextureRegion region) {
    Sprite3DCache found = null;
    for(Sprite3DCache cache : spriteCache) {
      if (cache.is(region)) {
        found = cache;
        break;
      }
    }

    if (found == null) {
      found = new Sprite3DCache(region);
      spriteCache.add(found);
    }

    return found;
  }

  public void render(BaseSprite3D sprite) {
    SpriteRenderable renderable  = spriteRenderablePool.obtain();
    renderable.environment       = env;
    renderable.build(sprite);
    render(renderable);
    usedRenderables.add(renderable);
  }

  @Override
  public void end() {
    super.end();
    for(SpriteRenderable renderable : usedRenderables) {
      spriteRenderablePool.free(renderable);
    }
    usedRenderables.clear();
  }

  @Override
  public void dispose() {
    for(Sprite3DCache cache : spriteCache) {
      cache.dispose();
    }
    spriteCache.clear();
    materials.clear();
    spriteRenderablePool.flush();
    super.dispose();
  }

  public void debug() {
    Gdx.app.log(TAG, "Materials: "+materials.size);
    Gdx.app.log(TAG, "Sprite cache: "+spriteCache.size);
  }


  public void setEnv(WorldEnv env) {
    this.env = env;
  }

  public WorldEnv getEnv() {
    return env;
  }

}
