package de.macbury.zanbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 26.05.14.
 */
public class LoadingScreen extends BaseScreen {
  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(1, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    if (!Zanbox.assets.update()) {
      Zanbox.screens.showGameScreen();
    }
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public void onEnter() {
    Zanbox.assets.init();
    Zanbox.screens.showGameScreen();
  }

  @Override
  public void onExit() {

  }
}
