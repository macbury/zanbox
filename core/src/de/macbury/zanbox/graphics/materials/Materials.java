package de.macbury.zanbox.graphics.materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.managers.Assets;

/**
 * Created by macbury on 04.06.14.
 */
public class Materials implements Disposable{

  private boolean initialized;
  public TerrainMaterial terrainMaterial;
  public LiquidMaterial liquidMaterial;

  public Materials() {

  }

  @Override
  public void dispose() {

  }

  public void init() {
    if (!initialized) {
      initialized = true;

      TextureAtlas terrainAtlas   = Zanbox.assets.get(Assets.TERRAIN_TEXTURE);

      if (terrainAtlas.getTextures().size > 1)
        throw new GdxRuntimeException("Only supports one texture for terrain!!!!");

      Texture terrainTexture = terrainAtlas.getTextures().first();
      terrainTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Nearest);
      terrainMaterial = new TerrainMaterial(TextureAttribute.createDiffuse(terrainTexture));
      liquidMaterial  = new LiquidMaterial(TextureAttribute.createDiffuse(terrainTexture));
      liquidMaterial.set(new BlendingAttribute(true, 0.2f));
    }
  }
}
