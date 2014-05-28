package de.macbury.zanbox.terrain;

import com.badlogic.gdx.utils.Disposable;
import de.macbury.zanbox.terrain.biome.WorldBiomeProvider;
import de.macbury.zanbox.terrain.chunk.Chunks;

/**
 * Created by macbury on 26.05.14.
 */
public class World implements Disposable {
  public WorldBiomeProvider biomeProvider;
  public Chunks chunks;

  public World(int seed) {
    this.biomeProvider = new WorldBiomeProvider(seed);
    this.chunks        = new Chunks(this);
  }

  public void update(float delta) {
    this.chunks.update(delta);
  }

  @Override
  public void dispose() {
    this.biomeProvider = null;
    this.chunks.dispose();
  }
}
