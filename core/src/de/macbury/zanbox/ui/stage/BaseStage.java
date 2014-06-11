package de.macbury.zanbox.ui.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

/**
 * Created by macbury on 09.06.14.
 */
public abstract class BaseStage extends Stage {
  public static final int VIEWPORT_WIDTH  = 800;
  public static final int VIEWPORT_HEIGHT = 480;
  private static final String TAG = "BaseStage";

  public BaseStage() {
    super(new ScalingViewport(Scaling.fill, VIEWPORT_WIDTH, VIEWPORT_HEIGHT));
    Gdx.app.log(TAG, "Scaling: " + getViewport());
  }
}
