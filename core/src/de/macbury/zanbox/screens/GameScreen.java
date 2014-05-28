package de.macbury.zanbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import de.macbury.zanbox.graphics.stage.InGameStage;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 28.05.14.
 */
public class GameScreen extends BaseScreen {

  private InGameStage gameStage;
  private GameLevel gameLevel;

  public GameScreen() {
    this.gameLevel = new GameLevel(1234);
    this.gameStage = new InGameStage();
    gameStage.setListener(gameLevel.playerSystem);

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    gameLevel.update(delta);
    gameStage.act();

    gameLevel.render();
    gameStage.draw();
  }

  @Override
  public void resize(int width, int height) {
    gameStage.resize(width, height);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(gameStage);
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
