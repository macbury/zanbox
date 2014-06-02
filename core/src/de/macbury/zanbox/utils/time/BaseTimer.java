package de.macbury.zanbox.utils.time;

import com.badlogic.gdx.Gdx;

/**
 * Created by macbury on 02.05.14.
 */
public abstract class BaseTimer {
  protected TimerListener listener;
  public void setListener(TimerListener listener) {
    this.listener = listener;
  }

  public abstract void update(float delta);

  public void update() {
    this.update(Gdx.graphics.getDeltaTime());
  }
}
