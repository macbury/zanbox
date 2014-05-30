package de.macbury.zanbox.graphics.sprites.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.sprites.SpriteRenderable;
import de.macbury.zanbox.level.terrain.chunk.layers.ChunkLayerPartRenderable;
import de.macbury.zanbox.managers.Shaders;

/**
 * Created by macbury on 30.05.14.
 */
public class TerrainShader extends BaseShader {
  private final int u_projViewTrans;
  private final int u_worldTrans;
  private final int u_diffuseTexture;
  private Renderable renderable;
  private boolean initialized;

  public static class Inputs {
    public final static Uniform projViewTrans   = new Uniform("u_projViewTrans");
    public final static Uniform worldTrans      = new Uniform("u_worldTrans");
    public final static Uniform diffuseTexture  = new Uniform("u_diffuseTexture", TextureAttribute.Diffuse);
  }

  public static class Setters {
    public final static Setter projViewTrans = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return true;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        shader.set(inputID, shader.camera.combined);
      }
    };
    public final static Setter worldTrans    = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        shader.set(inputID, renderable.worldTransform);
      }
    };
    public final static Setter diffuseTexture = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        final int unit = shader.context.textureBinder.bind(((TextureAttribute)(combinedAttributes
                .get(TextureAttribute.Diffuse))).textureDescription);
        shader.set(inputID, unit);
      }
    };
  }

  public TerrainShader() {
    this.program     = Zanbox.shaders.get(Shaders.SHADER_TERRAIN);
    u_projViewTrans  = register(Inputs.projViewTrans, Setters.projViewTrans);
    u_worldTrans     = register(Inputs.worldTrans, Setters.worldTrans);
    u_diffuseTexture = register(Inputs.diffuseTexture, Setters.diffuseTexture);
  }

  @Override
  public void init() {
    init(program, renderable);
    renderable = null;
    initialized = true;
  }

  @Override
  public void begin(Camera camera, RenderContext context) {
    super.begin(camera, context);
    context.setCullFace(GL20.GL_BACK);
    context.setDepthMask(true);
    context.setDepthTest(GL20.GL_LESS);
  }

  public void setRenderable(Renderable renderable) {
    if (!initialized)
      this.renderable = renderable;
  }

  @Override
  public int compareTo(Shader other) {
    if (other == null) return -1;
    if (other == this) return 0;
    return 0;
  }

  @Override
  public boolean canRender(Renderable instance) {
    return ChunkLayerPartRenderable.class.isInstance(instance);
  }
}
