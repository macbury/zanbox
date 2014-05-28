package de.macbury.zanbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.macbury.zanbox.terrain.biome.Biome;
import de.macbury.zanbox.terrain.biome.Liquid;
import de.macbury.zanbox.terrain.biome.WorldBiomeProvider;

/**
 * Created by macbury on 26.05.14.
 */
public class PerlinNoiseTestScreen extends BaseScreen implements InputProcessor {

  private SpriteBatch spriteBatch;
  private WorldBiomeProvider worldBiomeProvider;
  private Pixmap pixmap;
  private Texture texture;
  private int sx;
  private int sy;

  public PerlinNoiseTestScreen() {
    this.worldBiomeProvider = new WorldBiomeProvider(99999);
    this.spriteBatch        = new SpriteBatch();

    rebuild();

    Gdx.input.setInputProcessor(this);
  }

  public void rebuild() {
    int width  = 512;
    int height = 512;
    this.pixmap = new Pixmap( width, height, Pixmap.Format.RGBA8888 );
    Gdx.app.log("TAG", "REBUILD!");
    for(int x = sx; x < sx + pixmap.getWidth(); x++) {
      for(int y = sy; y < sy + pixmap.getHeight(); y++) {
        Biome biome = worldBiomeProvider.getBiomeAt(x,y);
        switch (biome) {
          case DESERT:
            pixmap.setColor(Color.YELLOW);
            break;

          case MOUNTAINS:
            pixmap.setColor(Color.DARK_GRAY);
            break;

          case PLAINS:
            pixmap.setColor(Color.GREEN);
            break;

          case SNOW:
            pixmap.setColor(1,1, 1, 1);
            break;

          case FOREST:
            pixmap.setColor(Color.OLIVE);
            break;
        }

        if (biome != Biome.DESERT) {
          Liquid liquid = worldBiomeProvider.getLiquid(x,y);
          if (liquid == Liquid.LAVA) {
            pixmap.setColor(Color.RED);
          } else if (liquid == Liquid.DEEP_WATER) {
            pixmap.setColor(Color.BLUE);
          } else if (liquid == Liquid.SHALLOW_WATER) {
            pixmap.setColor(Color.CYAN);
          }
        }

        pixmap.drawPixel(x-sx,y-sy);
      }
    }

    texture = new Texture(pixmap);
  }

  @Override
  public void onEnter() {

  }

  @Override
  public void onExit() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    spriteBatch.begin(); {
      spriteBatch.draw(texture, 0,0);
    }spriteBatch.end();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (keycode == Input.Keys.T) {
      move(1);
    }
    if (keycode == Input.Keys.E) {
      move(-1);
    }
    if (keycode == Input.Keys.R) {
      rebuild();
    }
    return false;
  }

  private void move(int direction) {
    this.sx += direction * 100;
    this.sy += direction * 100;
    Gdx.app.log("TAG", "SX,SY=" + sx + ", " + sy);
    rebuild();
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}
