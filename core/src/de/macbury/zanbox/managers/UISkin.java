package de.macbury.zanbox.managers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 28.05.14.
 */
public class UISkin extends Skin {

  public Touchpad.TouchpadStyle touchpadStyle;

  public UISkin() {
    super((TextureAtlas)Zanbox.assets.get(Assets.GUI_TEXTURE));
    this.touchpadStyle = new Touchpad.TouchpadStyle();
    touchpadStyle.background = getDrawable("touchpad_background");
    touchpadStyle.knob       = getDrawable("joystick_left");
  }

  public Touchpad touchpad(float deadzoneRadius) {
    return new Touchpad(deadzoneRadius, touchpadStyle);
  }
}
