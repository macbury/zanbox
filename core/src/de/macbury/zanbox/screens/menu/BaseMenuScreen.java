package de.macbury.zanbox.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.macbury.zanbox.screens.BaseScreen;
import de.macbury.zanbox.ui.stage.BaseMenuStage;

/**
 * Created by macbury on 09.06.14.
 */
public class BaseMenuScreen extends BaseScreen {

  protected final BaseMenuStage stage;

  public BaseMenuScreen(BaseMenuStage stage) {
    super();
    this.stage = stage;
  }

  @Override
  public void onEnter() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void onExit() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    stage.act();
    stage.draw();
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
}
