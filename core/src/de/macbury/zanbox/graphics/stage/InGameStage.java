package de.macbury.zanbox.graphics.stage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.ui.DebugWindow;

/**
 * Created by macbury on 28.05.14.
 */
public class InGameStage extends Stage {
  private final Touchpad movementTouchpad;
  private Table mainTable;
  private MainInGameUIListener listener;
  private static Vector3 temp = new Vector3();
  public InGameStage() {
    super(new ScalingViewport(Scaling.stretch, Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight(), new OrthographicCamera(Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight())));
    mainTable = new Table();
    mainTable.setFillParent(true);

    movementTouchpad = Zanbox.skin.touchpad(10);
    movementTouchpad.setResetOnTouchUp(true);

    mainTable.row(); {
      mainTable.add(movementTouchpad).expand().left().bottom().pad(40);
    }
    addActor(mainTable);

    movementTouchpad.addCaptureListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        InGameStage.this.listener.onPlayerMovementChange(temp.set(movementTouchpad.getKnobPercentX(), 0, -movementTouchpad.getKnobPercentY()), InGameStage.this);
        return super.touchDown(event, x, y, pointer, button);
      }

      @Override
      public void touchDragged(InputEvent event, float x, float y, int pointer) {
        super.touchDragged(event, x, y, pointer);
        InGameStage.this.listener.onPlayerMovementChange(temp.set(movementTouchpad.getKnobPercentX(), 0, -movementTouchpad.getKnobPercentY()), InGameStage.this);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);

        InGameStage.this.listener.onPlayerMovementChange(temp.set(Vector3.Zero), InGameStage.this);
      }
    });

    DebugWindow debugWindow = new DebugWindow();
    addActor(debugWindow);
    debugWindow.setVisible(true);
  }

  public void resize(int width, int height) {
  }

  public void setListener(MainInGameUIListener listener) {
    this.listener = listener;
  }
}
