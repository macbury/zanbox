package de.macbury.zanbox.entities.systems;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by macbury on 30.05.14.
 */
public class DayNightSystem extends VoidEntitySystem {
  private final int DAY_IN_SECONDS = 60 * 60 * 24;
  private final float timeDiffFactor = 0.0138f;
  private float time;
  private int gameTime;

  public DayNightSystem() {
    //MathUtils.lerp(0,24,)
  }

  @Override
  protected void processSystem() {
    time     += Gdx.graphics.getDeltaTime();
    gameTime = Math.round(time / timeDiffFactor);

  }

  public int currentTime() {
    return gameTime;
  }
}
