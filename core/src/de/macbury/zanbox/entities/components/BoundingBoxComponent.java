package de.macbury.zanbox.entities.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.macbury.zanbox.level.terrain.tiles.Tile;

/**
 * Created by macbury on 03.06.14.
 */
public class BoundingBoxComponent extends Component {
  private BoundingBox boundingBox;
  private Vector3 size;
  private static Vector3 tempA = new Vector3();
  private static Vector3 tempB = new Vector3();

  public BoundingBoxComponent(Vector3 size){
    this.boundingBox = new BoundingBox();
    this.size        = size;
  }

  public BoundingBoxComponent() {
    this(new Vector3(Tile.HALF_SIZE, Tile.HALF_SIZE, Tile.HALF_SIZE));
  }

  public BoundingBox getBoundingBox(Vector3 position) {
    tempA.set(position);
    tempB.set(position).add(size);
    boundingBox.set(tempA, tempB);
    return boundingBox;
  }
}
