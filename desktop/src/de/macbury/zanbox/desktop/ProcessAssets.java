package de.macbury.zanbox.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by macbury on 07.06.14.
 */
public class ProcessAssets {
  public static void main (String[] arg) {
    TexturePacker.Settings settings = new TexturePacker.Settings();

    TexturePacker.process(settings, "../../design/input/charset", "./textures", "charset");
    TexturePacker.process(settings, "../../design/input/gui", "./textures", "gui");
    TexturePacker.process(settings, "../../design/input/tiles", "./textures", "tiles");
  }
}
