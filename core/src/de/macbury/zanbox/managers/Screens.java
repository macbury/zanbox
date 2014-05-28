package de.macbury.zanbox.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.screens.*;

/**
 * Created by macbury on 26.05.14.
 */
public class Screens implements Disposable {
  private static final String TAG = "Screens";
  private Zanbox game;
  private LoadingScreen loadingScreen;
  private BaseScreen currentScreen;

  public Screens(Zanbox zanbox) {
    this.game = zanbox;
  }

  public void showLoadingScreen() {
    if (loadingScreen == null) {
      loadingScreen = new LoadingScreen();
    }

    enter(loadingScreen);
  }

  public void showGameScreen() {
    enter(new GameScreen());
  }

  public void enter(BaseScreen screen) {
    if (currentScreen != null) {
      currentScreen.onExit();
    }

    currentScreen = screen;
    game.setScreen(currentScreen);
    currentScreen.onEnter();
    //Gdx.app.log(TAG, "Opening: " + currentScreen.getClass().getSimpleName());
  }

  @Override
  public void dispose() {
    loadingScreen.dispose();
  }
}
