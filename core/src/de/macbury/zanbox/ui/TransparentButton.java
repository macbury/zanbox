package de.macbury.zanbox.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * Created by macbury on 07.06.14.
 */
public class TransparentButton extends Button {
  private TransparentButtonStyle style;

  public TransparentButton(TransparentButtonStyle style) {
    super(style);
    this.style = style;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, isPressed() ? style.activeAlpha : style.blurAlpha);
  }

  public static class TransparentButtonStyle extends ButtonStyle {
    public float activeAlpha = 1.0f;
    public float blurAlpha   = 0.5f;
  }
}
