package de.macbury.zanbox.ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 31.05.14.
 */
public class DefaultLabel extends Label {
  public DefaultLabel(CharSequence text) {
    super(text, Zanbox.skin.defaultLabelStyle);
  }
}
