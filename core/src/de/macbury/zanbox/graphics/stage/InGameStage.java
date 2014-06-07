package de.macbury.zanbox.graphics.stage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.ui.DebugWindow;
import de.macbury.zanbox.ui.Joystick;

/**
 * Created by macbury on 28.05.14.
 */
public class InGameStage extends Stage {
  private Joystick joystick;
  private Table mainTable;

  public InGameStage() {
    super(new ScalingViewport(Scaling.stretch, Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight(), new OrthographicCamera(Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight())));
    mainTable = new Table();
    mainTable.setFillParent(true);
    this.joystick = new Joystick();

    mainTable.row(); {
      mainTable.add(joystick).expand().left().bottom().pad(40);
    }
    addActor(mainTable);

    DebugWindow debugWindow = new DebugWindow();
    addActor(debugWindow);
    debugWindow.setVisible(true);
  }

  public void resize(int width, int height) {
  }

  public void setListener(Joystick.JoystickListener listener) {
    this.joystick.setListener(listener);
  }
}
