package de.macbury.zanbox.graphics.shaders;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.materials.LiquidMaterial;
import de.macbury.zanbox.managers.Shaders;

/**
 * Created by macbury on 04.06.14.
 */
public class LiquidShader extends CoreShader {
  private final int u_opacity;

  public LiquidShader() {
    super(Zanbox.shaders.get(Shaders.SHADER_LIQUID));
    u_opacity = register(CoreShader.Inputs.opacity);
  }

  @Override
  public void begin(Camera camera, RenderContext context) {
    super.begin(camera, context);
    context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    LiquidMaterial material = Zanbox.materials.liquidMaterial;
    BlendingAttribute blending = (BlendingAttribute)material.get(BlendingAttribute.Type);
    set(u_opacity, blending.opacity);
  }

  @Override
  public boolean canRender(Renderable instance) {
    return LiquidMaterial.class.isInstance(instance.material);
  }
}
