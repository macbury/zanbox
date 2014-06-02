package de.macbury.zanbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.level.terrain.chunks.ChunksProvider;
import de.macbury.zanbox.level.terrain.chunks.ChunksRenderables;

/**
 * Created by macbury on 31.05.14.
 */
public class DebugWindow extends Window {
  private DefaultLabel visibleChunks;
  private DefaultLabel visiblityBoundingBoxLabel;
  private DefaultLabel visibleChunkRenderablesLabel;
  private DefaultLabel fpsLabel;

  public DebugWindow() {
    super("Debug", Zanbox.skin.debugWindowStyle);
    Table table = new Table();

    table.setFillParent(true);
    this.fpsLabel = new DefaultLabel("FPS: ...");
    this.visibleChunkRenderablesLabel = new DefaultLabel("Visible chunksProvider: ...");
    this.visiblityBoundingBoxLabel    = new DefaultLabel("");
    this.visibleChunks                = new DefaultLabel("");
    table.row().padTop(20).padLeft(20); {
      table.add(fpsLabel).left().top().expandX();
    }

    table.row().padLeft(20); {
      table.add(visibleChunks).left().top().expandX();
    }

    table.row().padLeft(20); {
      table.add(visibleChunkRenderablesLabel).left().top().expandX();
    }

    table.row().padLeft(20); {
      table.add(visiblityBoundingBoxLabel).left().top().expandX();
    }

    table.row().padLeft(20); {
      table.add().expand();
    }

    addActor(table);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    ChunksProvider provider = Zanbox.level.chunksProvider;
    ChunksRenderables renderables = Zanbox.level.chunksRenderables;

    fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    visibleChunks.setText("Chunks(Visible/Total): " + renderables.visibleChunks.size + "/" + provider.chunks.size);
    visibleChunkRenderablesLabel.setText("Chunk parts(V/T): " + renderables.visibleRenderables.size + "/"+renderables.totalRenderables.size);
    visiblityBoundingBoxLabel.setText("World Box " + renderables.boundingBox.toString());
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    setWidth(480);

    setPosition(getStage().getWidth() - getWidth(), 0);

  }

  @Override
  public float getMinWidth() {
    return 240;
  }
}
