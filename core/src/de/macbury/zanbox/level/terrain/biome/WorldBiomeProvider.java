package de.macbury.zanbox.level.terrain.biome;

import de.macbury.zanbox.utils.MyMath;
import de.macbury.zanbox.procedular.BrownianNoise3D;
import de.macbury.zanbox.procedular.PerlinNoise;

/**
 * Created by macbury on 26.05.14.
 */
public class WorldBiomeProvider {
  private final BrownianNoise3D liquidNoise;
  private final BrownianNoise3D temperatureNoise;
  private final BrownianNoise3D humidityNoise;
  private final static double NOISE_SIZE = 0.005d; // lower is worse details

  public WorldBiomeProvider(int seed) {
    int finalSeed           = seed;
    temperatureNoise        = new BrownianNoise3D(new PerlinNoise(finalSeed + 5));
    humidityNoise           = new BrownianNoise3D(new PerlinNoise(finalSeed + 6));
    liquidNoise             = new BrownianNoise3D(new PerlinNoise(finalSeed + 7));
  }

  public double getHumidityAt(int x, int z) {
    double result = humidityNoise.noise(x * NOISE_SIZE, 0, NOISE_SIZE * z);
    return MyMath.clamp((result + 1.0f) / 2.0f);
  }

  public double getLiquidTypeAt(int x, int z) {
    double result = liquidNoise.noise(x * NOISE_SIZE, 0, NOISE_SIZE * z);
    return MyMath.clamp((result + 1.0f) / 2.0f);
  }

  public double getTemperatureAt(int x, int z) {
    double result = temperatureNoise.noise(x * NOISE_SIZE, 0, NOISE_SIZE * z);
    return MyMath.clamp((result + 1.0f) / 2.0f);
  }

  public Biome getBiomeAt(int x, int z) {
    double temp = getTemperatureAt(x, z);
    double humidity = getHumidityAt(x, z) * temp;

    if (temp >= 0.5d && humidity < 0.3d) {
      return Biome.DESERT;
    } else if (humidity >= 0.3d && humidity <= 0.6d && temp >= 0.5d) {
      return Biome.PLAINS;
    } else if (temp <= 0.3d && humidity > 0.5d) {
      return Biome.SNOW;
    } else if (humidity >= 0.2d && humidity <= 0.6d && temp < 0.5d) {
      return Biome.MOUNTAINS;
    }

    return Biome.FOREST;
  }

  public Liquid getLiquid(int x, int z) {
    double liquid = getLiquidTypeAt(x,z);

    if (liquid >= 0.58d && liquid < 0.6d) {
      return Liquid.SHALLOW_WATER;
    } else if (liquid >= 0.6d && liquid <= 0.7d) {
      return Liquid.DEEP_WATER;
    } else if (liquid <= 0.1d && liquid >= 0.0d) {
      return Liquid.LAVA;
    }

    return Liquid.NONE;
  }
}
