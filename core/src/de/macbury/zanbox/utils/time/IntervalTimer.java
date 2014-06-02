package de.macbury.zanbox.utils.time;

/**
 * Created by macbury on 30.04.14.
 */
public class IntervalTimer extends BaseTimer {
  private float acumulated = 0.0f;
  private float maxTime    = 1.0f;


  public IntervalTimer(float max) {
    this.maxTime = max;
  }

  @Override
  public void update(float dt) {
    acumulated += dt;
    if (acumulated > maxTime) {
      acumulated = 0;
      if (listener != null)
        listener.timerTick(this);
    }
  }

}
