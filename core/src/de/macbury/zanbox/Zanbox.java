package de.macbury.zanbox;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.managers.Screens;
import de.macbury.zanbox.procedular.BrownianNoise3D;
import de.macbury.zanbox.procedular.PerlinNoise;
import de.macbury.zanbox.terrain.WorldBiomeProvider;

public class Zanbox extends Game {
  public static Assets assets;
  public static Screens screens;
  @Override
	public void create () {
    assets  = new Assets();
    screens = new Screens(this);

    screens.showLoadingScreen();
/*
    WorldBiomeProvider provider = new WorldBiomeProvider(9394859);

    int width  = 128;
    int height = 128;

    Pixmap pixmap = new Pixmap( width, height, Pixmap.Format.RGBA8888 );

    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        WorldBiomeProvider.Biome biome = provider.getBiomeAt(x,y);
        switch (biome) {
          case DESERT:
            pixmap.setColor(Color.YELLOW);
          break;

          case DUNGEON:
            pixmap.setColor(Color.DARK_GRAY);
          break;

          case PLAINS:
            pixmap.setColor(Color.GREEN);
          break;

          case SNOW:
            pixmap.setColor(1,1, 1, 1);
          break;

          case FOREST:
            pixmap.setColor(Color.CYAN);
          break;
        }
        pixmap.drawPixel(x,y);
      }
    }
    Gdx.app.log("TEST", Gdx.files.external("perlin.png").file().getAbsolutePath());
    PixmapIO.writePNG(Gdx.files.external("perlin.png"), pixmap);
*/
	}

  @Override
  public void dispose() {
    assets.dispose();
    screens.dispose();
    super.dispose();
  }
}
