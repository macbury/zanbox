package de.macbury.zanbox.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.macbury.zanbox.Zanbox;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    config.useImmersiveMode = true;
    config.numSamples = 0;
		initialize(new Zanbox(), config);
	}
}
