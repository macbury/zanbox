package de.macbury.zanbox.ui.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.macbury.zanbox.ui.DebugWindow;
import de.macbury.zanbox.ui.widgets.Joystick;

/**
 * Created by macbury on 28.05.14.
 */
public class InGameStage extends BaseStage {
  private Joystick joystick;
  private Table mainTable;

  public InGameStage() {
    super();
    mainTable = new Table();
    mainTable.setFillParent(true);
    this.joystick = new Joystick();
    addActor(mainTable);
    mainTable.row(); {
      mainTable.add(joystick).expand().left().bottom().pad(40);
    }


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
