package de.macbury.zanbox.managers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.ui.widgets.TransparentButton;

/**
 * Created by macbury on 28.05.14.
 */
public class UISkin extends Skin {

  public TextButton.TextButtonStyle buttonStyle;
  public Label.LabelStyle defaultLabelStyle;
  private BitmapFont mainFont;
  public Window.WindowStyle debugWindowStyle;
  public TransparentButton.TransparentButtonStyle upButtonStyle;
  public TransparentButton.TransparentButtonStyle downButtonStyle;
  public TransparentButton.TransparentButtonStyle leftButtonStyle;
  public TransparentButton.TransparentButtonStyle rightButtonStyle;

  public UISkin() {
    super((TextureAtlas)Zanbox.assets.get(Assets.GUI_TEXTURE));

    mainFont  = Zanbox.assets.get(Assets.MAIN_FONT);

    this.debugWindowStyle                = new Window.WindowStyle();
    this.debugWindowStyle.titleFont      = mainFont;
    this.debugWindowStyle.titleFontColor = Color.WHITE;
    this.debugWindowStyle.background     = getDrawable("window_background");

    this.defaultLabelStyle               = new Label.LabelStyle();
    defaultLabelStyle.font               = mainFont;
    defaultLabelStyle.fontColor          = Color.WHITE;

    this.buttonStyle                     = new TextButton.TextButtonStyle();
    buttonStyle.up                       = getDrawable("button_normal");
    buttonStyle.down                     = getDrawable("button_active");
    buttonStyle.font                     = mainFont;
    buttonStyle.fontColor                = Color.WHITE;

    buildJoystickTheme();
  }

  private void buildJoystickTheme() {
    this.upButtonStyle  = new TransparentButton.TransparentButtonStyle();
    upButtonStyle.up    = getDrawable("joystick_up_button");
    upButtonStyle.down  = getDrawable("joystick_up_button");

    this.downButtonStyle  = new TransparentButton.TransparentButtonStyle();
    downButtonStyle.up    = getDrawable("joystick_down_button");
    downButtonStyle.down  = getDrawable("joystick_down_button");

    this.leftButtonStyle  = new TransparentButton.TransparentButtonStyle();
    leftButtonStyle.up    = getDrawable("joystick_left_button");
    leftButtonStyle.down  = getDrawable("joystick_left_button");

    this.rightButtonStyle  = new TransparentButton.TransparentButtonStyle();
    rightButtonStyle.up    = getDrawable("joystick_right_button");
    rightButtonStyle.down  = getDrawable("joystick_right_button");
  }

}
