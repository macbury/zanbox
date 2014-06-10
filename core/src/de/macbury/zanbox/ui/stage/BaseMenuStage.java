package de.macbury.zanbox.ui.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.macbury.zanbox.Zanbox;

/**
 * Created by macbury on 09.06.14.
 */
public class BaseMenuStage extends BaseStage {
  protected final Table mainTable;

  public BaseMenuStage() {
    super();

    Image backgroundImage = new Image(Zanbox.skin.getTiledDrawable("background"));
    backgroundImage.setFillParent(true);
    addActor(backgroundImage);

    mainTable = new Table();
    mainTable.setFillParent(true);
    addActor(mainTable);
  }
}
