package de.macbury.zanbox.utils.time;

/**
 * Created by macbury on 02.05.14.
 */
public class FrameTickTimer extends BaseTimer {
  private int maxFrames;
  private int accumulatedFrames;

  public FrameTickTimer(int maxFrames) {
    this.maxFrames = maxFrames;
  }

  @Override
  public void update(float delta) {
    accumulatedFrames++;
    if (accumulatedFrames > maxFrames) {
      accumulatedFrames = 1;
      listener.timerTick(this);
    }
  }
}
