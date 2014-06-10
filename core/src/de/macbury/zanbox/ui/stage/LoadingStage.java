package de.macbury.zanbox.ui.stage;

import de.macbury.zanbox.ui.widgets.DefaultLabel;

/**
 * Created by macbury on 10.06.14.
 */
public class LoadingStage extends BaseMenuStage {
  private final DefaultLabel loadingLabel;

  public LoadingStage() {
    super();

    this.loadingLabel = new DefaultLabel("Loading...");

    mainTable.row(); {
      mainTable.add(loadingLabel);
    }

    setProgress(0);
  }

  public void setProgress(float progress) {
    loadingLabel.setText("Loading: " + Math.round(progress * 100) + "%");
  }
}
