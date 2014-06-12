package de.macbury.zanbox.graphics.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.materials.TerrainMaterial;
import de.macbury.zanbox.level.terrain.WorldEnv;
import de.macbury.zanbox.managers.Shaders;

/**
 * Created by macbury on 30.05.14.
 */
public class TerrainShader extends CoreShader {
  private final int u_shadeColor;


  public static class Inputs {
    public final static Uniform shadeColor      = new Uniform("u_shadeColor");
  }

  public static class Setters {
    public final static Setter shadeColor       = new Setter() {
      @Override
      public boolean isGlobal (BaseShader shader, int inputID) {
        return false;
      }

      @Override
      public void set (BaseShader shader, int inputID, Renderable renderable, Attributes combinedAttributes) {
        WorldEnv env = (WorldEnv)renderable.environment;
        shader.set(inputID, env.shadeColor);
      }
    };
  }

  public TerrainShader() {
    super(Zanbox.shaders.get(Shaders.SHADER_TERRAIN));

    u_shadeColor         = register(Inputs.shadeColor, Setters.shadeColor);
  }

  @Override
  public void begin(Camera camera, RenderContext context) {
    this.program = Zanbox.shaders.get(Shaders.SHADER_TERRAIN);
    super.begin(camera, context);
    context.setCullFace(GL20.GL_BACK);
    context.setDepthMask(true);
    context.setDepthTest(GL20.GL_LEQUAL);
    //set(u_shadeColor, Zanbox.level.env.shadeColor);
  }


  @Override
  public boolean canRender(Renderable instance) {
    return TerrainMaterial.class.isInstance(instance.material);
  }

}
