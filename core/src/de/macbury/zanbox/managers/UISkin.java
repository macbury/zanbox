package de.macbury.zanbox.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 28.05.14.
 */
public class UISkin extends Skin {

  public Label.LabelStyle defaultLabelStyle;
  private BitmapFont mainFont;
  public Window.WindowStyle debugWindowStyle;
  public Touchpad.TouchpadStyle touchpadStyle;

  public UISkin() {
    super((TextureAtlas)Zanbox.assets.get(Assets.GUI_TEXTURE));
    this.touchpadStyle = new Touchpad.TouchpadStyle();
    touchpadStyle.background = getDrawable("touchpad_background");
    touchpadStyle.knob       = getDrawable("joystick_left");

    mainFont  = Zanbox.assets.get(Assets.MAIN_FONT);

    this.debugWindowStyle                = new Window.WindowStyle();
    this.debugWindowStyle.titleFont      = mainFont;
    this.debugWindowStyle.titleFontColor = Color.WHITE;
    this.debugWindowStyle.background     = getDrawable("window_background");

    this.defaultLabelStyle               = new Label.LabelStyle();
    defaultLabelStyle.font               = mainFont;
    defaultLabelStyle.fontColor          = Color.WHITE;
  }

  public Touchpad touchpad(float deadzoneRadius) {
    return new Touchpad(deadzoneRadius, touchpadStyle);
  }
}
