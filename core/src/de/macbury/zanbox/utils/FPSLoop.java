package de.macbury.zanbox.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by macbury on 27.05.14.
 */
public class FPSLoop {
  private final int maxSeconds;
  private FPSLoopListener listener;
  private boolean running = false;
  private int totalTicks;

  public FPSLoop(int maxSeconds) {
    this.maxSeconds = maxSeconds;
  }

  public void start(int totalTicks) {
    listener.onFPSLoopStart(this);
    running = true;
    this.totalTicks = totalTicks;
  }

  public void tick() {
    if (running) {
      int tick = totalTicks / maxSeconds;
      if (listener.onFPSLoopTick(this, tick / Gdx.graphics.getFramesPerSecond())) {
        listener.onFPSLoopFinish(this);
        running = false;
      }
    }
  }

  public FPSLoopListener getListener() {
    return listener;
  }

  public void setListener(FPSLoopListener listener) {
    this.listener = listener;
  }

  public static interface FPSLoopListener {
    public void onFPSLoopStart(FPSLoop loop);
    public boolean onFPSLoopTick(FPSLoop loop, int ticks);
    public void onFPSLoopFinish(FPSLoop loop);
  }
}
