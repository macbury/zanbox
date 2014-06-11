package de.macbury.zanbox.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.macbury.zanbox.level.terrain.tiles.BiomeDefinition;
import de.macbury.zanbox.loaders.TileLoader;

/**
 * Created by macbury on 26.05.14.
 */
public class Assets extends AssetManager {
  public final static String TERRAIN_TEXTURE = "textures/tiles.atlas";
  public final static String CHARSET_TEXTURE = "textures/charset.atlas";
  public final static String GUI_TEXTURE     = "textures/gui.atlas";
  public final static String SHADERS_PREFIX  = "shaders/";
  public final static String BIOMES_PREFIX   = "biomes/";

  public final static String MAIN_FONT = "fonts/advocut-webfont-18.fnt";
  public static final String SHADERS_HELPERS_PREFIX = SHADERS_PREFIX + "helpers/";
  public static final String WATER_NORMAL_MAP = "textures/water_normalmap.jpg";

  public void init() {
    setLoader(BiomeDefinition.class, new TileLoader(new InternalFileHandleResolver()));
    BitmapFontLoader.BitmapFontParameter fontParameter = new BitmapFontLoader.BitmapFontParameter();
    fontParameter.genMipMaps = false;
    fontParameter.magFilter  = Texture.TextureFilter.Nearest;
    fontParameter.minFilter  = Texture.TextureFilter.Nearest;

    load(GUI_TEXTURE,     TextureAtlas.class);
    load(MAIN_FONT, BitmapFont.class, fontParameter);
    finishLoading();

    load(TERRAIN_TEXTURE, TextureAtlas.class);
    load(CHARSET_TEXTURE, TextureAtlas.class);
    load(WATER_NORMAL_MAP, Texture.class);
    loadBiomes();
  }

  private void loadBiomes() {
    FileHandle[] tiles = Gdx.files.internal(BIOMES_PREFIX).list();

    for(FileHandle name : tiles) {
      load(BIOMES_PREFIX + name.name(), BiomeDefinition.class);
    }
  }
}
