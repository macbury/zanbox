package de.macbury.zanbox.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by macbury on 26.05.14.
 */
public class Assets extends AssetManager {
  public final static String TERRAIN_TEXTURE = "textures/textures.pack";
  public final static String CHARSET_TEXTURE = "textures/characters.atlas";
  public static final String SHADERS_PREFIX = "shaders/";

  public void init() {
    finishLoading();
    load(TERRAIN_TEXTURE, TextureAtlas.class);
    load(CHARSET_TEXTURE, TextureAtlas.class);
  }
}
