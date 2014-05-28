package de.macbury.zanbox.graphics.sprites.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.macbury.zanbox.graphics.sprites.SpriteRenderable;
import de.macbury.zanbox.terrain.chunk.ChunkRenderable;

/**
 * Created by macbury on 28.05.14.
 */
public class ShaderProvider extends BaseShaderProvider {
  private final DefaultShader.Config config;
  private final SpriteShader spriteShader;

  public ShaderProvider() {
    this.config       = new DefaultShader.Config();
    this.spriteShader = new SpriteShader();
  }

  @Override
  protected Shader createShader(Renderable renderable) {
    if (spriteShader.canRender(renderable)) {
      spriteShader.setRenderable(renderable);
      return spriteShader;
    } else if (ChunkRenderable.class.isInstance(renderable)) {
      return new DefaultShader(renderable, config);
    }
    throw new GdxRuntimeException( "Unimplemented renderable: "+renderable.getClass().toString());
  }
}
