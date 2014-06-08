package de.macbury.zanbox.entities.systems.daynight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by macbury on 08.06.14.
 */
public class TimeOfDay {
  private final int startHour;
  private final int endHour;
  public final String name;
  public Color ambientColor;
  public Color lightColor;
  public Vector3 lightDirection;

  public TimeOfDay(String name, int fromHour, int toHour) {
    this.startHour = fromHour * 3600;
    this.endHour   = toHour   * 3600;
    this.name      = name;
    this.ambientColor = new Color(Color.WHITE);
    this.lightColor = new Color(Color.WHITE);
    this.lightDirection = new Vector3();
  }

  public boolean between(float time) {
    return (time >= startHour && time <= endHour);
  }

  public float alpha(float time) {
    float diff   = time - startHour;
    float finish = endHour - startHour;
    return diff / finish;
  }
}
