package de.macbury.zanbox.graphics.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by macbury on 04.06.14.
 */
public class LiquidShader extends CoreShader {
  public LiquidShader(ShaderProgram program) {
    super(program);
  }

  @Override
  public boolean canRender(Renderable instance) {
    return false;
  }
}
