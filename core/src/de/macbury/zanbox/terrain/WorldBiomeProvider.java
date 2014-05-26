package de.macbury.zanbox.terrain;

import de.macbury.zanbox.MyMath;
import de.macbury.zanbox.procedular.BrownianNoise3D;
import de.macbury.zanbox.procedular.PerlinNoise;

/**
 * Created by macbury on 26.05.14.
 */
public class WorldBiomeProvider {
  private final BrownianNoise3D temperatureNoise;
  private final BrownianNoise3D humidityNoise;
  private final static double NOISE_SIZE = 0.005d; // lower is worse details
  public enum Biome {
    DUNGEON(true, 0.95f), SNOW(false, 1.0f), DESERT(true, 0.0f), FOREST(true, 0.9f), PLAINS(true, 0.0f);

    private boolean vegetationFriendly;
    private float fog;

    private Biome(boolean vegetationFriendly, float fog) {
      this.vegetationFriendly = vegetationFriendly;
      this.fog = fog;
    }

    public boolean isVegetationFriendly() {
      return vegetationFriendly;
    }

    public float getFog() {
      return fog;
    }
  }

  public WorldBiomeProvider(int seed) {
    int finalSeed           = seed;
    temperatureNoise        = new BrownianNoise3D(new PerlinNoise(finalSeed + 5));
    humidityNoise           = new BrownianNoise3D(new PerlinNoise(finalSeed + 6));
  }

  public double getHumidityAt(int x, int z) {
    double result = humidityNoise.noise(x * NOISE_SIZE, 0, NOISE_SIZE * z);
    return MyMath.clamp((result + 1.0f) / 2.0f);
  }

  public double getTemperatureAt(int x, int z) {
    double result = temperatureNoise.noise(x * NOISE_SIZE, 0, NOISE_SIZE * z);
    return MyMath.clamp((result + 1.0f) / 2.0f);
  }

  public WorldBiomeProvider.Biome getBiomeAt(int x, int z) {
    double temp = getTemperatureAt(x, z);
    double humidity = getHumidityAt(x, z) * temp;

    if (temp >= 0.5d && humidity < 0.3d) {
      return Biome.DESERT;
    } else if (humidity >= 0.3d && humidity <= 0.6d && temp >= 0.5d) {
      return Biome.PLAINS;
    } else if (temp <= 0.3d && humidity > 0.5d) {
      return Biome.SNOW;
    } else if (humidity >= 0.2d && humidity <= 0.6d && temp < 0.5d) {
      return Biome.DUNGEON;
    }

    return Biome.FOREST;
  }
}
