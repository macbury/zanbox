package de.macbury.zanbox.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 10.06.14.
 */
public class NormalButton extends TextButton {

  public NormalButton(String label) {
    super(label, Zanbox.skin.buttonStyle);
  }
}