package de.macbury.zanbox.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.shaders.ShaderManager;


/**
 * Created by macbury on 09.05.14.
 */
public class Shaders extends ShaderManager {
  public static final String SHADER_EMPTY = "SHADER_EMPTY";
  public static final String SHADER_DEFAULT = "SHADER_DEFAULT";
  public static final String SHADER_SPRITES = "SHADER_SPRITES";
  public static final String SHADER_TERRAIN = "SHADER_TERRAIN";

  public Shaders() {
    super(Assets.SHADERS_PREFIX, Zanbox.assets);
    ShaderProgram.pedantic = true;
    add(SHADER_EMPTY, "empty.vert.glsl", "empty.frag.glsl");
    add(SHADER_DEFAULT, "default.vert.glsl", "default.frag.glsl");

    add(SHADER_SPRITES, "sprite.vert.glsl", "sprite.frag.glsl");
    add(SHADER_TERRAIN, "terrain.vert.glsl", "terrain.frag.glsl");
  }

}