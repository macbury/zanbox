package de.macbury.zanbox.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.utils.DebugExtBase;
/**
 * Created by macbury on 11.06.14.z
 */
public class DebugDesktop extends DebugExtBase {
  private static final String TAG = "DebugDesktop";
  private WatchDir watchShaders;
  private WatchDir watchShadersHelpers;

  @Override
  public void init() {
    this.watchShaders = new WatchDir(Gdx.files.internal(Assets.SHADERS_PREFIX));
    this.watchShadersHelpers = new WatchDir(Gdx.files.internal(Assets.SHADERS_PREFIX + "/helpers"));
  }

  @Override
  public void update() {
    watchShaders.update();
    watchShadersHelpers.update();
  }

  @Override
  public void dispose() {

  }

  public class WatchDir {
    private WatchService watchService;

    public WatchDir(FileHandle handle) {
      try {
        this.watchService  = FileSystems.getDefault().newWatchService();
        String watchDir    = handle.file().getAbsolutePath()+"/";
        Gdx.app.log(TAG, "Watching: " + watchDir);
        Path shadersFolder = Paths.get(watchDir);

        shadersFolder.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void update() {
      WatchKey watchKey = watchService.poll();

      if (watchKey != null && watchKey.pollEvents().size() > 0) {
        Zanbox.shaders.reload();
        Gdx.app.log(TAG, "Reloading shaders!");
        watchKey.reset();
      }
    }
  }
}

