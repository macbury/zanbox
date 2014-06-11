package de.macbury.zanbox;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;

import de.macbury.zanbox.graphics.materials.Materials;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.managers.Screens;
import de.macbury.zanbox.managers.Shaders;
import de.macbury.zanbox.managers.UISkin;
import de.macbury.zanbox.utils.DebugExtBase;

public class Zanbox extends Game {
  private static final String TAG = "Zanbox";
  public static Assets assets;
  public static Screens screens;
  public static Shaders shaders;
  public static GameLevel level;
  public static Materials materials;
  public static UISkin skin;
  public static DebugExtBase debug;

  private FPSLogger fpsLogger;

  public Zanbox(DebugExtBase debug) {
    super();
    this.debug = debug;
  }

  @Override
	public void create () {
    fpsLogger = new FPSLogger();
    assets    = new Assets();
    screens   = new Screens(this);
    shaders   = new Shaders();
    materials = new Materials();
    assets.init();
    skin      = new UISkin();
    screens.showLoadingScreen();
    if (debug != null) {
      debug.init();
    }
    Gdx.app.log(TAG, "Real width: " + Gdx.graphics.getWidth());
    Gdx.app.log(TAG, "Real height: " +  Gdx.graphics.getHeight());

    Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

  @Override
  public void dispose() {
    assets.dispose();
    screens.dispose();
    if (debug != null) {
      debug.dispose();
    }
    super.dispose();
  }

  @Override
  public void render() {
    super.render();
    if (debug != null) {
      debug.update();
    }
    //fpsLogger.log();
  }
}
