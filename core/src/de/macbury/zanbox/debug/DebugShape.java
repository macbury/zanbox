package de.macbury.zanbox.debug;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import de.macbury.zanbox.level.terrain.chunks.Chunk;
import de.macbury.zanbox.level.terrain.chunk.Chunks;
import de.macbury.zanbox.level.terrain.chunks.layers.ChunkLayerPartRenderable;

/**
 * Created by macbury on 09.03.14.
 */
public class DebugShape {
  private static int[][] boxCornerMap = {
    {0,1,2,3,0},
    {4,5,6,7,4},
    {4,0},
    {3,7},
    {6,2},
    {5,1}
  };

  public static void draw(ShapeRenderer renderer, BoundingBox box) {
    Vector3 corners[] = box.getCorners();

    for(int i = 0; i < boxCornerMap.length; i++) {
      for (int l = 1; l < boxCornerMap[i].length; l++) {
        Vector3 from = corners[boxCornerMap[i][l-1]];
        Vector3 to   = corners[boxCornerMap[i][l]];

        renderer.line(from, to);
      }
    }
  }

  public static void drawMap(ShapeRenderer shapeRender, Chunks chunks) {
    shapeRender.begin(ShapeRenderer.ShapeType.Line); {

      for (Chunk chunk : chunks) {
        shapeRender.setColor(chunks.contains(chunk, true) ? Color.WHITE : Color.BLUE);
        //DebugShape.draw(shapeRender, chunk.getBoundingBox());
      }

      for(ChunkLayerPartRenderable part : chunks.totalRenderables) {
        shapeRender.setColor(chunks.visibleRenderables.contains(part, true) ? Color.GREEN : Color.RED);
        DebugShape.draw(shapeRender, part.boundingBox);
      }

    } shapeRender.end();
  }

}
