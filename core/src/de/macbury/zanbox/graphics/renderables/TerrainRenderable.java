package de.macbury.zanbox.graphics.renderables;

import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Pool;
import de.macbury.zanbox.level.terrain.chunks.layer.GeometryCache;

/**
 * Created by macbury on 03.06.14.
 */
public class TerrainRenderable extends Renderable implements Pool.Poolable {

  public void setGeometryCache(GeometryCache cache) {
    this.mesh = new Mesh(true,cache.verties.length, cache.indices.length, cache.attributes);
    mesh.setIndices(cache.indices);
    mesh.setVertices(cache.verties);

    primitiveType            = GL30.GL_TRIANGLES;
    meshPartSize             = mesh.getNumIndices();
    meshPartOffset           = 0;
  }

  @Override
  public void reset() {
    if (mesh != null) {
      mesh.dispose();
    }

    mesh = null;
  }
}
