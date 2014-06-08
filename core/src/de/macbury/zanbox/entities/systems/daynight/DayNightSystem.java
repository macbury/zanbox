package de.macbury.zanbox.entities.systems.daynight;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.macbury.zanbox.level.terrain.WorldEnv;

/**
 * Created by macbury on 30.05.14.
 */
public class DayNightSystem extends VoidEntitySystem {
  private static final String TAG = "DayNightSystem";
  private final int DAY_IN_SECONDS = 60 * 60 * 24;
  private final float timeDiffFactor = 0.0138f;
  private final WorldEnv env;
  private float speedTimeFactor = 1.0f;
  private float time;
  private int gameTime;
  private Array<TimeOfDay> timeOfDays;
  private TimeOfDay currentTimeOfDay;
  private TimeOfDay nextTimeOfDay;
  private Vector3 tempA = new Vector3();
  private Vector3 tempB = new Vector3();
  public DayNightSystem(WorldEnv env) {
    this.env   = env;
    timeOfDays = new Array<TimeOfDay>();

    TimeOfDay night = new TimeOfDay("Night", 0, 6);
    night.ambientColor.set(Color.BLUE);
    night.lightDirection.set(0.5f, -0.5f, -0.5f);
    timeOfDays.add(night);

    TimeOfDay sunrise = new TimeOfDay("Sunrise", 6, 7);
    sunrise.ambientColor.set(Color.RED);
    sunrise.lightDirection.set(0.5f, -0.5f, 0.5f);
    timeOfDays.add(sunrise);

    TimeOfDay day = new TimeOfDay("Day", 7, 19);
    day.ambientColor.set(Color.BLUE);
    day.lightDirection.set(-0.5f, -0.5f, 0.5f);
    timeOfDays.add(day);

    TimeOfDay sunset = new TimeOfDay("Sunset", 19, 21);
    sunset.ambientColor.set(Color.OLIVE);
    sunset.lightDirection.set(0, -0.5f, 0.5f);
    timeOfDays.add(sunset);

    TimeOfDay curNight = new TimeOfDay("Night", 21, 24);
    curNight.ambientColor.set(Color.PURPLE);
    curNight.lightDirection.set(0, -0.5f, 0.5f);
    timeOfDays.add(curNight);

    setTime(4 * 3600);
  }

  @Override
  protected void processSystem() {
    incrTime();

    if (currentTimeOfDay.between(gameTime)) {
      tempA.set(currentTimeOfDay.ambientColor.r, currentTimeOfDay.ambientColor.g, currentTimeOfDay.ambientColor.b);
      tempB.set(nextTimeOfDay.ambientColor.r, nextTimeOfDay.ambientColor.g, nextTimeOfDay.ambientColor.b);
      tempA.interpolate(tempB, currentTimeOfDay.alpha(gameTime), Interpolation.exp10In);

      env.ambientColor.color.set(tempA.x, tempA.y, tempA.z, 1.0f);
      env.sunLight.set(currentTimeOfDay.lightColor,  currentTimeOfDay.lightDirection);
    } else {
      nextTimeOfDay();
    }
  }

  private void incrTime() {
    time     += world.getDelta() * speedTimeFactor;
    calculateGameTime();
  }

  private void calculateGameTime() {
    gameTime = Math.round(time / timeDiffFactor);
    if (gameTime >= DAY_IN_SECONDS) {
      time = gameTime - DAY_IN_SECONDS;
    }
  }

  // game time in seconds
  public int currentTime() {
    return gameTime;
  }

  public String formattedTime() {
    return currentTimeOfDay.name.toString() + " (" + currentTimeOfDay.alpha(gameTime) + ") " + " = " + String.format("%d:%02d:%02d", gameTime/3600, (gameTime%3600)/60, (gameTime%60));
  }

  public void setTime(int gameTimeInSeconds) {
    time = gameTimeInSeconds * timeDiffFactor;
    currentTimeOfDay = null;
    calculateGameTime();
    nextTimeOfDay();
  }

  private void nextTimeOfDay() {
    int index;
    for (index = 0; index < timeOfDays.size; index++) {
      TimeOfDay timeOfDay = timeOfDays.get(index);
      if (timeOfDay.between(gameTime)) {
        currentTimeOfDay = timeOfDay;
        break;
      }
    }

    index++;
    if (index >= timeOfDays.size) {
      index = 0;
    }
    nextTimeOfDay = timeOfDays.get(index);

    Gdx.app.debug(TAG, "Move " + currentTimeOfDay.name + " -> " + nextTimeOfDay.name);
  }

  public float getSpeedTimeFactor() {
    return speedTimeFactor;
  }

  public void setSpeedTimeFactor(float speedTimeFactor) {
    this.speedTimeFactor = speedTimeFactor;
  }

}
