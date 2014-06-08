package de.macbury.zanbox.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public static final String SHADER_LIQUID  = "SHADER_LIQUID";
  private static final String TAG = "Shaders";

  private HashMap<String, String> helpers;
  private static final Pattern REQIUIRE_HELPER_PATTERN = Pattern.compile("\\/\\/=\\W+require\\W+([a-z_]+)");
  public Shaders() {
    super(Assets.SHADERS_PREFIX, Zanbox.assets);
    ShaderProgram.pedantic = true;
    loadHelpers();
    add(SHADER_EMPTY, "empty.vert.glsl", "empty.frag.glsl");
    add(SHADER_DEFAULT, "default.vert.glsl", "default.frag.glsl");

    add(SHADER_SPRITES, "sprite.vert.glsl", "sprite.frag.glsl");
    add(SHADER_TERRAIN, "terrain.vert.glsl", "terrain.frag.glsl");
    add(SHADER_LIQUID, "liquid.vert.glsl", "liquid.frag.glsl");
  }

  private void loadHelpers() {
    helpers = new HashMap<String, String>();
    FileHandle[] fileHelpers = Gdx.files.internal(Assets.SHADERS_HELPERS_PREFIX).list(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.contains(".glsl");
      }
    });

    for(FileHandle handle : fileHelpers) {
      Gdx.app.log(TAG, "Loading helper: " + handle.name());
      helpers.put(handle.nameWithoutExtension(), handle.readString());
    }
  }

  private String applyHelpers(String source) {
    while(true) {
      Matcher matcher  = REQIUIRE_HELPER_PATTERN.matcher(source);
      if (matcher.find()) {
        String helperKey = matcher.group(1);
        if (helpers.containsKey(helperKey)) {
          String helperContent = helpers.get(helperKey);
          source = source.substring(0, matcher.start()) + helperContent + "\n" + source.substring(matcher.end(), source.length());

          Gdx.app.log(TAG, "Appending: " + helperKey);
        } else {
          throw new GdxRuntimeException("Not found helper: " + helperKey);
        }
      } else {
        break;
      }
    }
    return source;
  }

  @Override
  protected boolean init(String key, String vert, String frag) {
    if (frag == null || vert == null)
      return false;
    vert = applyHelpers(vert);
    return super.init(key, vert, frag);
  }
}