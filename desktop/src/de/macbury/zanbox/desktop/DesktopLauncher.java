package de.macbury.zanbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import de.macbury.zanbox.Zanbox;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 480 * 2;
    config.height = 320 * 2;
    config.samples = 2;
    TexturePacker.Settings settings = new TexturePacker.Settings();

    TexturePacker.process(settings, "../../design/input/charset", "./textures", "charset");
    TexturePacker.process(settings, "../../design/input/gui", "./textures", "gui");
    TexturePacker.process(settings, "../../design/input/tiles", "./textures", "tiles");

		new LwjglApplication(new Zanbox(), config);
	}
}
