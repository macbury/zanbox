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

  private static Color rgb(float r, float g, float b) {
    return new Color(r/255f, g/255f, b/255f, 1);
  }

  public DayNightSystem(WorldEnv env) {
    this.env   = env;
    timeOfDays = new Array<TimeOfDay>();

    TimeOfDay night = new TimeOfDay("Night", 0, 6);
    night.ambientColor.set(rgb(17,16,26));
    night.lightDirection.set(0f, -0.5f, 0f);
    night.lightColor.set(rgb(12, 12, 18));

    timeOfDays.add(night);

    TimeOfDay sunrise = new TimeOfDay("Sunrise", 6, 8);
    sunrise.ambientColor.set(rgb(2, 27, 34));
    sunrise.lightColor.set(rgb(227, 194, 143));
    sunrise.lightDirection.set(0.5f, -0.5f, 0.5f);
    timeOfDays.add(sunrise);

    TimeOfDay day = new TimeOfDay("Day", 8, 18);
    day.ambientColor.set(0.4f, 0.4f, 0.4f, 1f);
    day.lightColor.set(1f, 1f, 1f, 1f);
    day.lightDirection.set(0.5f, -0.5f, -0.5f);
    timeOfDays.add(day);

    TimeOfDay sunset = new TimeOfDay("Sunset", 18, 20);
    sunset.ambientColor.set(rgb(51, 19, 39));
    sunset.lightColor.set(rgb(255, 110, 39));
    sunset.lightDirection.set(-1, -0.5f, -1);
    timeOfDays.add(sunset);

    TimeOfDay curNight = new TimeOfDay("Night", 20, 24);
    curNight.ambientColor.set(rgb(17,16,26));
    curNight.lightDirection.set(0f, -0.5f, 0f);
    curNight.lightColor.set(rgb(12,12,16));
    timeOfDays.add(curNight);

    setTime(12 * 3600);
    setSpeedTimeFactor(1);
  }

  @Override
  protected void processSystem() {
    incrTime();

    if (currentTimeOfDay.between(gameTime)) {
      interpolate(currentTimeOfDay.ambientColor, nextTimeOfDay.ambientColor, env.ambientColor.color);
      interpolate(currentTimeOfDay.lightColor, nextTimeOfDay.lightColor, env.sunLight.color);
      interpolate(currentTimeOfDay.lightDirection, nextTimeOfDay.lightDirection, env.sunLight.direction);
    } else {
      nextTimeOfDay();
    }
  }

  private void interpolate(Vector3 fromVector, Vector3 targetVector, Vector3 output) {
    tempA.set(fromVector);
    tempB.set(targetVector);
    tempA.interpolate(tempB, currentTimeOfDay.alpha(gameTime), Interpolation.linear);
    output.set(tempA);
  }

  private void interpolate(Color fromColor, Color targetColor, Color outputColor) {
    tempA.set(fromColor.r, fromColor.g, fromColor.b);
    tempB.set(targetColor.r, targetColor.g, targetColor.b);
    tempA.interpolate(tempB, currentTimeOfDay.alpha(gameTime), Interpolation.pow5In);
    outputColor.set(tempA.x, tempA.y, tempA.z, 1.0f);
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
