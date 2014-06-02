package de.macbury.zanbox.graphics.sprites.shaders;

import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix3;
import de.macbury.zanbox.level.terrain.WorldEnv;

/**
 * Created by macbury on 30.05.14.
 */
public abstract class CoreShader extends BaseShader {
  protected final int u_projViewTrans;
  protected final int u_worldTrans;
  protected final int u_diffuseTexture;
  protected final int u_normalMatrix;
  protected final int u_ambientColor;
  protected final int u_sunLightColor;
  protected final int u_sunLightDirection;
  protected Renderable renderable;
  protected boolean initialized;

  public static class Inputs {
    public final static Uniform projViewTrans       = new Uniform("u_projViewTrans");
    public final static Uniform worldTrans          = new Uniform("u_worldTrans");
    public final static Uniform diffuseTexture      = new Uniform("u_diffuseTexture", TextureAttribute.Diffuse);
    public final static Uniform normalMatrix        = new Uniform("u_normalMatrix");
    public final static Uniform sunLightColor       = new Uniform("u_sunLightColor");
    public final static Uniform sunLightDirection   = new Uniform("u_sunLightDirection");
    public final static Uniform ambientLight        = new Uniform("u_ambientLight");
  }

  public static class Setters {
    public final static Setter projViewTrans  = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return true;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        shader.set(inputID, shader.camera.combined);
      }
    };
    public final static Setter worldTrans     = new Setter() {
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
    public final static Setter normalMatrix = new Setter() {
      private final Matrix3 tmpM = new Matrix3();

      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        shader.set(inputID, tmpM.set(renderable.worldTransform).inv().transpose());
      }
    };

    public final static Setter sunLightColor     = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        WorldEnv env = (WorldEnv)renderable.environment;
        shader.set(inputID, env.sunLight.color);
      }
    };
    public final static Setter sunLightDirection     = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        WorldEnv env = (WorldEnv)renderable.environment;
        shader.set(inputID, env.sunLight.direction);
      }
    };

    public final static Setter ambientColor = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        WorldEnv env = (WorldEnv)renderable.environment;
        shader.set(inputID, env.ambientColor.color);
      }
    };
  }

  public CoreShader(ShaderProgram program) {
    this.program     = program;
    u_projViewTrans  = register(Inputs.projViewTrans, Setters.projViewTrans);
    u_worldTrans     = register(Inputs.worldTrans, Setters.worldTrans);
    u_diffuseTexture = register(Inputs.diffuseTexture, Setters.diffuseTexture);
    u_normalMatrix   = register(Inputs.normalMatrix, Setters.normalMatrix);
    u_ambientColor   = register(Inputs.ambientLight, Setters.ambientColor);
    u_sunLightColor      = register(CoreShader.Inputs.sunLightColor);
    u_sunLightDirection  = register(CoreShader.Inputs.sunLightDirection);
  }

  @Override
  public void init() {
    init(program, renderable);
    renderable = null;
    initialized = true;
  }

  public void setRenderable(Renderable renderable) {
    if (!initialized)
      this.renderable = renderable;
  }

  @Override
  public void render(Renderable renderable) {
    WorldEnv env = (WorldEnv) renderable.environment;
    program.setUniformf(loc(u_sunLightColor), env.sunLight.color);
    program.setUniformf(loc(u_sunLightDirection), env.sunLight.direction);
    super.render(renderable);
  }

  @Override
  public int compareTo(Shader other) {
    if (other == null) return -1;
    if (other == this) return 0;
    return 0;
  }

}
