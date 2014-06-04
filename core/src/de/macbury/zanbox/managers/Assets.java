package de.macbury.zanbox.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by macbury on 26.05.14.
 */
public class Assets extends AssetManager {
  public final static String TERRAIN_TEXTURE = "textures/tiles.atlas";
  public final static String CHARSET_TEXTURE = "textures/charset.atlas";
  public final static String GUI_TEXTURE     = "textures/gui.atlas";
  public static final String SHADERS_PREFIX  = "shaders/";

  public final static String MAIN_FONT = "fonts/Minecraftia-14.fnt";

  public void init() {
    load(GUI_TEXTURE,     TextureAtlas.class);
    load(MAIN_FONT, BitmapFont.class);
    finishLoading();
    load(TERRAIN_TEXTURE, TextureAtlas.class);
    load(CHARSET_TEXTURE, TextureAtlas.class);
  }
}
