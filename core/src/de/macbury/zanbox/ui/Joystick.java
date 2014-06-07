package de.macbury.zanbox.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.utils.Direction;

/**
 * Created by macbury on 07.06.14.
 */
public class Joystick extends WidgetGroup {
  private final TransparentButton upButton;
  private final TransparentButton downButton;
  private final TransparentButton rightButton;
  private final TransparentButton leftButton;
  private JoystickListener listener;

  public Joystick() {
    super();

    upButton = new TransparentButton(Zanbox.skin.upButtonStyle);
    upButton.setPosition((getPrefWidth() / 2) - (upButton.getPrefWidth() / 2), getPrefHeight() - upButton.getPrefHeight());

    downButton = new TransparentButton(Zanbox.skin.downButtonStyle);
    downButton.setPosition((getPrefWidth() / 2) - (downButton.getPrefWidth() / 2), 0);

    leftButton = new TransparentButton(Zanbox.skin.leftButtonStyle);
    leftButton.setPosition(0, (getPrefHeight() / 2) - (leftButton.getPrefHeight() / 2));

    rightButton = new TransparentButton(Zanbox.skin.rightButtonStyle);
    rightButton.setPosition(getPrefWidth() - rightButton.getPrefWidth(), (getPrefHeight() / 2) - (rightButton.getPrefHeight() / 2));

    addActor(upButton);
    addActor(downButton);
    addActor(rightButton);
    addActor(leftButton);

    bindMoveEvent(upButton, Direction.Up);
    bindMoveEvent(downButton, Direction.Down);
    bindMoveEvent(leftButton, Direction.Left);
    bindMoveEvent(rightButton, Direction.Right);
  }

  private void bindMoveEvent(TransparentButton button, final Direction direction) {
    button.addListener(new ClickListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        triggerMoveInDirection(true, direction);
        return super.touchDown(event, x, y, pointer, button);
      }

      @Override
      public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        triggerMoveInDirection(false, Direction.None);
      }
    });
  }

  public void triggerMoveInDirection(boolean on, Direction direction) {
    if (listener != null) {
      if (on) {
        listener.onJoystickTouchDownDirection(direction, this);
      } else {
        listener.onJoystickTouchUpDirection(direction, this);
      }
    }
  }

  @Override
  public float getPrefWidth() {
    return 190;
  }

  @Override
  public float getPrefHeight() {
    return 190;
  }

  public JoystickListener getListener() {
    return listener;
  }

  public void setListener(JoystickListener listener) {
    this.listener = listener;
  }

  public interface JoystickListener {
    public void onJoystickTouchDownDirection(Direction direction, Joystick joystick);
    public void onJoystickTouchUpDirection(Direction direction, Joystick joystick);
  }
}
