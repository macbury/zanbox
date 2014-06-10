package de.macbury.zanbox.screens.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.screens.BaseScreen;

/**
 * Created by macbury on 26.05.14.
 */
public class MeshTestScreen extends BaseScreen {

  private final ModelBatch batch;
  private final PerspectiveCamera camera;
  private final Renderable chunkRenderable;

  public MeshTestScreen() {
    this.camera    = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    camera.position.set(4,5, 5);
    camera.lookAt(5,0,5);
    this.camera.update(true);
    MeshAssembler meshAssembler = new MeshAssembler();
    TextureAtlas terrainAtlas   = Zanbox.assets.get(Assets.TERRAIN_TEXTURE);
    TextureRegion region        = terrainAtlas.findRegion("light_grass");

    meshAssembler.begin(); {
      for(int x = 0; x < 20; x++) {
        for(int y = 0; y < 20; y++) {
          meshAssembler.topFace(x,0,y, 1,1,1, region.getU(), region.getV(), region.getU2(), region.getV2());
        }
      }

    } meshAssembler.end();

    Mesh mesh = new Mesh(false, meshAssembler.getVerties().length, meshAssembler.getIndices().length, meshAssembler.getVertexAttributes());
    mesh.setIndices(meshAssembler.getIndices());
    mesh.setVertices(meshAssembler.getVerties());

    this.batch = new ModelBatch();

    chunkRenderable = new Renderable();
    chunkRenderable.mesh = mesh;
    chunkRenderable.primitiveType = GL30.GL_TRIANGLES;
    chunkRenderable.meshPartSize   = mesh.getNumIndices();
    chunkRenderable.meshPartOffset = 0;
    chunkRenderable.material = new Material(TextureAttribute.createDiffuse(region.getTexture()));
  }

  @Override
  public void onEnter() {

  }

  @Override
  public void onExit() {

  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    batch.begin(camera); {
      batch.render(chunkRenderable);
    } batch.end();
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void show() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void dispose() {

  }
}
