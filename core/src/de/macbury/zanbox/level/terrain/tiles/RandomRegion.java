package de.macbury.zanbox.level.terrain.tiles;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by macbury on 30.05.14.
 */
public class RandomRegion {
  private final Array<TextureAtlas.AtlasRegion> regions;

  public RandomRegion(Array<TextureAtlas.AtlasRegion> regions) {
    this.regions = regions;
  }

  public TextureRegion get() {
    return regions.get((int)Math.round(Math.random() * (regions.size - 1)));
  }
}
