package de.macbury.zanbox.screens;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.screens.menu.BaseMenuScreen;
import de.macbury.zanbox.ui.stage.LoadingStage;

/**
 * Created by macbury on 26.05.14.
 */
public class LoadingScreen extends BaseMenuScreen {

  public LoadingScreen() {
    super(new LoadingStage());
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    LoadingStage ui = (LoadingStage)stage;
    ui.setProgress(Zanbox.assets.getProgress());

    if (Zanbox.assets.update()) {
      Zanbox.screens.showGameMenu();
    }
  }
}
