package de.macbury.zanbox.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by macbury on 05.03.14.
 */
public class FrustrumRenderer extends InputAdapter {
  private final static short[][] RENDER_SEQUENCE = {
    {0,1,1,2,2,3,3,0},
    {4,5,5,6,6,7,7,4},
    {0,4,1,5,2,6,3,7}
  };
  private static final String TAG = "FrustrumRenderer";
  private InputAdapter inputAdapter;
  private ShapeRenderer renderer;

  public PerspectiveCamera frustrumCamera;
  private DebugFrustrum frustrum;

  private boolean enabled;

  public FrustrumRenderer(PerspectiveCamera camera) {
    this.frustrumCamera = camera;
    saveState();
  }

  public void saveState() {
    Gdx.app.log(TAG, "Saving state");
    this.frustrumCamera.update(true);
    this.frustrum = new DebugFrustrum(this.frustrumCamera.frustum, this.frustrumCamera.invProjectionView);
  }

  public void render(PerspectiveCamera camera) {
    if (!enabled) {
      return;
    }
    renderer.setProjectionMatrix(camera.combined);
    renderer.begin(ShapeRenderer.ShapeType.Line);
      renderer.setColor(Color.LIGHT_GRAY);

      for(int sequence = 0; sequence < RENDER_SEQUENCE.length; sequence++) {
        for(int index = 1; index < RENDER_SEQUENCE[sequence].length; index+=2) {
          short a = RENDER_SEQUENCE[sequence][index];
          short b = RENDER_SEQUENCE[sequence][index-1];
          renderer.line(frustrum.planePoints[a], frustrum.planePoints[b]);
        }
      }

    renderer.end();
  }

  @Override
  public boolean keyDown(int keycode) {
    if (Input.Keys.F == keycode) {
      this.enabled = !this.enabled;
      saveState();
      return true;
    } else {
      return false;
    }
  }

  public DebugFrustrum getFrustrum() {
    return frustrum;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setRenderer(ShapeRenderer renderer) {
    this.renderer = renderer;
  }
}
