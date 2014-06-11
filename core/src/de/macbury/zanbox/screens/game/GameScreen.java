package de.macbury.zanbox.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import de.macbury.zanbox.ui.stage.InGameStage;
import de.macbury.zanbox.level.GameLevel;
import de.macbury.zanbox.screens.BaseScreen;

/**
 * Created by macbury on 28.05.14.
 */
public class GameScreen extends BaseScreen {

  private InputMultiplexer inputMultiplexer;
  private CameraInputController cameraController;
  private InGameStage gameStage;
  private GameLevel gameLevel;

  public GameScreen() {
    this.gameLevel = new GameLevel(49586);
    this.gameStage = new InGameStage();

    //cameraController = new CameraInputController(gameLevel.camera);

    gameStage.setListener(gameLevel.playerSystem);
    this.inputMultiplexer = new InputMultiplexer();
    this.inputMultiplexer.addProcessor(gameStage);
    this.inputMultiplexer.addProcessor(gameLevel.frustrumRenderer);
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
    Gdx.input.setInputProcessor(inputMultiplexer);
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
