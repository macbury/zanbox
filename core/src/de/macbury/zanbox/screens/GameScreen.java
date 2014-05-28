package de.macbury.zanbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 28.05.14.
 */
public class GameScreen extends BaseScreen {

  private GameLevel gameLevel;

  public GameScreen() {
    this.gameLevel = new GameLevel(1234);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    gameLevel.update(delta);
    gameLevel.render();
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

  }

  @Override
  public void onExit() {

  }
}
