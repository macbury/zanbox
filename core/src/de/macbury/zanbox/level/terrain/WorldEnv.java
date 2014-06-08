package de.macbury.zanbox.level.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

/**
 * Created by macbury on 30.05.14.
 */
public class WorldEnv extends Environment {
  public Color shadeColor;
  public DirectionalLight sunLight;
  public ColorAttribute ambientColor;
  public float time;

  public WorldEnv() {
    this.ambientColor = new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f);
    this.sunLight     = new DirectionalLight();
    this.shadeColor   = new Color(0.1f, 0.1f, 0.1f, 0.1f);

    this.sunLight.set(1f, 1f, 1f, 0.5f, -0.5f, -0.5f);
    set(ambientColor);
    add(sunLight);
  }
}
