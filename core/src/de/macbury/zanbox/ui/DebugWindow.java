package de.macbury.zanbox.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.text.DecimalFormat;

import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.level.terrain.chunks.ChunksRenderables;
import de.macbury.zanbox.level.terrain.chunks.provider.ChunksProvider;
import de.macbury.zanbox.ui.widgets.DefaultLabel;
import de.macbury.zanbox.utils.time.BaseTimer;
import de.macbury.zanbox.utils.time.IntervalTimer;
import de.macbury.zanbox.utils.time.TimerListener;

/**
 * Created by macbury on 31.05.14.
 */
public class DebugWindow extends Dialog implements TimerListener {
  private DefaultLabel dateTimeLabel;
  private DefaultLabel tileInfoLabel;
  private IntervalTimer updateInfoTimer;
  private DefaultLabel memoryLabel;
  private DefaultLabel visibleChunks;
  private DefaultLabel visiblityBoundingBoxLabel;
  private DefaultLabel visibleChunkRenderablesLabel;
  private DefaultLabel fpsLabel;
  private static final long K = 1024;
  private static final long M = K * K;
  private static final long G = M * K;
  private static final long T = G * K;

  public static String convertToStringRepresentation(final long value){
    final long[] dividers = new long[] { T, G, M, K, 1 };
    final String[] units = new String[] { "TB", "GB", "MB", "KB", "B" };
    if(value < 1)
      throw new IllegalArgumentException("Invalid file size: " + value);
    String result = null;
    for(int i = 0; i < dividers.length; i++){
      final long divider = dividers[i];
      if(value >= divider){
        result = format(value, divider, units[i]);
        break;
      }
    }
    return result;
  }

  private static String format(final long value,
                               final long divider,
                               final String unit){
    final double result =
            divider > 1 ? (double) value / (double) divider : (double) value;
    return new DecimalFormat("#,##0.#").format(result) + " " + unit;
  }

  public DebugWindow() {
    super("", Zanbox.skin.debugWindowStyle);
    Table table = getContentTable();
    setModal(false);
    this.fpsLabel = new DefaultLabel("FPS: ...");
    this.visibleChunkRenderablesLabel = new DefaultLabel("Visible chunksProvider: ...");
    this.visiblityBoundingBoxLabel    = new DefaultLabel("");
    this.visibleChunks                = new DefaultLabel("");
    this.memoryLabel                  = new DefaultLabel("");
    this.tileInfoLabel                = new DefaultLabel("");
    this.dateTimeLabel                = new DefaultLabel("");
    this.updateInfoTimer              = new IntervalTimer(1);
    this.updateInfoTimer.setListener(this);
    table.row(); {
      table.add(fpsLabel).left().top().expandX();
    }

    table.row(); {
      table.add(dateTimeLabel).left().top().expandX();
    }

    table.row(); {
      table.add(visibleChunks).left().top().expandX();
    }

    table.row(); {
      table.add(visibleChunkRenderablesLabel).left().top().expandX();
    }

    table.row(); {
      table.add(visiblityBoundingBoxLabel).left().top().expandX();
    }

    table.row(); {
      table.add(memoryLabel).left().top().expandX();
    }

    table.row(); {
      table.add(tileInfoLabel).left().top().expandX();
    }

    timerTick(updateInfoTimer);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    updateInfoTimer.update();
  }

  @Override
  public void setVisible(boolean visible) {
    super.setVisible(visible);
    setWidth(480);
    setHeight(240);
    setPosition(getStage().getWidth() - getWidth()-10, 10);

  }


  Vector3 temp = new Vector3();
  @Override
  public void timerTick(BaseTimer sender) {
    ChunksProvider provider = Zanbox.level.chunksProvider;
    ChunksRenderables renderables = Zanbox.level.chunksRenderables;

    fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    visibleChunks.setText("Chunks(Visible/Total): " + renderables.visibleChunks.size + "/" + provider.chunks.size);
    visibleChunkRenderablesLabel.setText("Chunk parts(V/T): " + renderables.visibleSectors.size + "/"+renderables.totalSectors.size);
    visiblityBoundingBoxLabel.setText("World Box " + renderables.boundingBox.toString());
    memoryLabel.setText("Memory(Java/Native): " + convertToStringRepresentation(Gdx.app.getJavaHeap()) + "/" + convertToStringRepresentation(Gdx.app.getNativeHeap()));

    byte tileID = provider.getTile(Math.round(Zanbox.level.worldPosition.x), Math.round(Zanbox.level.worldPosition.z), Zanbox.level.currentLayer);

    tileInfoLabel.setText("TileID: " + tileID + " for " + Zanbox.level.worldPosition.toString());

    dateTimeLabel.setText("Time: "+Zanbox.level.dayNightSystem.formattedTime());
  }
}
