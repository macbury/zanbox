package de.macbury.zanbox.graphics.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.materials.LiquidMaterial;
import de.macbury.zanbox.level.terrain.tiles.Tile;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.managers.Shaders;

/**
 * Created by macbury on 04.06.14.
 */
public class LiquidShader extends CoreShader {
  private final int u_opacity;
  private final int u_waterHeight;
  private final int u_normalMap;
  private final LiquidMaterial material;
  private final Texture normalMapTexture;
  private int numWaves;
  private float[] amplitude;
  private float[] wavelength;
  private float[] speed;
  private Vector2[] direction;

  public static class Inputs {
    public final static Uniform waterHeight    = new Uniform("u_waterHeight");
    public final static Uniform waterNormalmap = new Uniform("u_normalMap");
  }


  public LiquidShader() {
    super(Zanbox.shaders.get(Shaders.SHADER_LIQUID));
    u_opacity             = register(CoreShader.Inputs.opacity);
    u_waterHeight         = register(Inputs.waterHeight);
    u_normalMap           = register(Inputs.waterNormalmap);
    this.material         = Zanbox.materials.liquidMaterial;
    this.normalMapTexture = Zanbox.assets.get(Assets.WATER_NORMAL_MAP);
    normalMapTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    //setNumWaves(4);
  }

  @Override
  public void begin(Camera camera, RenderContext context) {
    super.begin(camera, context);
    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    BlendingAttribute blending = (BlendingAttribute)material.get(BlendingAttribute.Type);
    set(u_opacity, blending.opacity);
    set(u_waterHeight, Tile.LIQUID_HEIGHT);
    //set(u_normalMap, context.textureBinder.bind(normalMapTexture));
  }

  public void setNumWaves(int numWaves) {
    this.numWaves   = numWaves;
    this.amplitude  = new float[numWaves];
    this.wavelength = new float[numWaves];
    this.speed      = new float[numWaves];
    this.direction  = new Vector2[numWaves];

    for (int i = 0; i < numWaves; ++i) {
      amplitude[i]   = 0.5f / (i + 1);
      wavelength[i]  = 8 * MathUtils.PI / (i + 1);
      speed[i]       = 1.0f + 2*i;

      float angle    = MathUtils.random(-MathUtils.PI, MathUtils.PI);
      direction[i]   = new Vector2(MathUtils.cos(angle), MathUtils.sin(angle));
    }
  }

  @Override
  public boolean canRender(Renderable instance) {
    return LiquidMaterial.class.isInstance(instance.material);
  }
}
