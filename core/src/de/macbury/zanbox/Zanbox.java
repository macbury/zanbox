package de.macbury.zanbox;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.managers.Screens;
import de.macbury.zanbox.managers.Shaders;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.managers.UISkin;
import de.macbury.zanbox.utils.VirtualViewport;

public class Zanbox extends Game {
  private static final String TAG = "Zanbox";
  public static Assets assets;
  public static Screens screens;
  public static Shaders shaders;
  public static GameLevel level;
  public static UISkin skin;
  public static VirtualViewport viewport;

  private FPSLogger fpsLogger;

  @Override
	public void create () {
    fpsLogger = new FPSLogger();
    viewport  = new VirtualViewport(800, 600);
    assets    = new Assets();
    screens   = new Screens(this);
    shaders   = new Shaders();
    assets.init();
    skin      = new UISkin();
    screens.showLoadingScreen();

    Gdx.app.log(TAG, "Real width: " + Gdx.graphics.getWidth());
    Gdx.app.log(TAG, "Real height: " +  Gdx.graphics.getHeight());

    Gdx.app.log(TAG, "Virtual width: " +  Zanbox.viewport.getWidth());
    Gdx.app.log(TAG, "Virtual height: " +  Zanbox.viewport.getHeight());

    Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

  @Override
  public void dispose() {
    assets.dispose();
    screens.dispose();
    super.dispose();
  }

  @Override
  public void render() {
    super.render();
    //fpsLogger.log();
  }
}
