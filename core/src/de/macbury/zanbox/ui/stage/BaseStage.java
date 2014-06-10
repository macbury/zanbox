package de.macbury.zanbox.ui.stage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 09.06.14.
 */
public abstract class BaseStage extends Stage {

  public BaseStage() {
    super(new ScalingViewport(Scaling.stretch, Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight(), new OrthographicCamera(Zanbox.viewport.getWidth(), Zanbox.viewport.getHeight())));
  }
}
