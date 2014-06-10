package de.macbury.zanbox.ui.stage;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.ui.widgets.NormalButton;

/**
 * Created by macbury on 10.06.14.
 */
public class MainMenuStage extends BaseMenuStage {
  private static final float BUTTON_WIDTH = 240;
  private static final float BUTTON_PADDING_BOTTOM = 10;
  private final NormalButton newWorldButton;
  private final NormalButton loadWorldButton;
  private final NormalButton settingsButton;

  public MainMenuStage() {
    super();

    newWorldButton  = new NormalButton("New world");
    loadWorldButton = new NormalButton("Load world");
    settingsButton  = new NormalButton("Settings");

    button(newWorldButton);
    button(loadWorldButton);
    button(settingsButton);

    newWorldButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        Zanbox.screens.showGameScreen();
      }
    });
  }

  private void button(TextButton button) {
    mainTable.row(); {
      mainTable.add(button).width(BUTTON_WIDTH).padBottom(BUTTON_PADDING_BOTTOM);
    }
  }
}
