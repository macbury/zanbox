package de.macbury.zanbox.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by macbury on 26.05.14.
 */
public class Assets extends AssetManager {
  public final static String TERRAIN_TEXTURE = "textures/tiles.atlas";
  public final static String CHARSET_TEXTURE = "textures/charset.atlas";
  public final static String GUI_TEXTURE     = "textures/gui.atlas";
  public final static String SHADERS_PREFIX  = "shaders/";

  public final static String MAIN_FONT = "fonts/advocut-webfont-18.fnt";
  public static final String SHADERS_HELPERS_PREFIX = SHADERS_PREFIX + "helpers/";

  public void init() {
    BitmapFontLoader.BitmapFontParameter fontParameter = new BitmapFontLoader.BitmapFontParameter();
    fontParameter.genMipMaps = true;
    fontParameter.magFilter  = Texture.TextureFilter.MipMapNearestNearest;
    fontParameter.minFilter  = Texture.TextureFilter.MipMapNearestNearest;

    load(GUI_TEXTURE,     TextureAtlas.class);
    load(MAIN_FONT, BitmapFont.class, fontParameter);
    finishLoading();

    load(TERRAIN_TEXTURE, TextureAtlas.class);
    load(CHARSET_TEXTURE, TextureAtlas.class);
  }
}
