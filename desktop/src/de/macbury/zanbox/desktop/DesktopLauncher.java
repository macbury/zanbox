package de.macbury.zanbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.macbury.zanbox.Zanbox;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 480 * 2;
    config.height = 320 * 2;
    config.samples = 2;
		new LwjglApplication(new Zanbox(), config);
	}
}
