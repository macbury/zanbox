package de.macbury.zanbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.managers.Screens;
import de.macbury.zanbox.managers.Shaders;

public class Zanbox extends Game {
  public static Assets assets;
  public static Screens screens;
  public static Shaders shaders;
  private FPSLogger fpsLogger;

  @Override
	public void create () {
    fpsLogger = new FPSLogger();
    assets  = new Assets();
    screens = new Screens(this);
    shaders = new Shaders();
    assets.init();
    screens.showLoadingScreen();
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
    fpsLogger.log();
  }
}
