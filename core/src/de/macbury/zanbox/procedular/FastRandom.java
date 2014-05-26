package de.macbury.zanbox.procedular;

import java.util.Random;

/**
 * Created by macbury on 26.05.14.
 */
public class FastRandom extends Random {

  private long seed = System.currentTimeMillis();

  /**
   * Initializes a new instance of the random number generator using
   * a specified seed.
   *
   * @param seed The seed to use
   */
  public FastRandom(long seed) {
    this.seed = seed;

  }

  /**
   * Initializes a new instance of the random number generator using
   * "System.currentTimeMillis()" as seed.
   */
  public FastRandom() {
  }

  /**
   * Returns a random int value.
   *
   * @return Random value
   */
  public int nextInt() {
    seed++;
    seed ^= (seed << 21);
    seed ^= (seed >>> 35);
    seed ^= (seed << 4);
    return (int) seed;
  }
}