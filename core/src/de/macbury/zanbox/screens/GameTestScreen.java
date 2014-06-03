package de.macbury.zanbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import de.macbury.zanbox.Zanbox;
import de.macbury.zanbox.graphics.renderables.TerrainRenderable;
import de.macbury.zanbox.graphics.sprites.BaseSprite3D;
import de.macbury.zanbox.graphics.sprites.ModelAndSpriteBatch;
import de.macbury.zanbox.graphics.geometry.MeshAssembler;
import de.macbury.zanbox.managers.Assets;
import de.macbury.zanbox.level.GameLevel;

/**
 * Created by macbury on 27.05.14.
 */
public class GameTestScreen extends BaseScreen {
  private final Renderable chunkRenderable;
  private CameraInputController cameraController;
  private Array<BaseSprite3D> sprites;
  private GameLevel gameLevel;
  private ModelAndSpriteBatch modelBatch;
  private PerspectiveCamera camera;

  public GameTestScreen() {
    this.gameLevel = new GameLevel(1234);

    TextureAtlas terrainAtlas   = Zanbox.assets.get(Assets.TERRAIN_TEXTURE);
    TextureRegion regionA       = terrainAtlas.findRegion("light_grass");
    TextureRegion regionB       = terrainAtlas.findRegion("dirt");
    TextureRegion regionC       = terrainAtlas.findRegion("sand");

    TextureAtlas charAtlas   = Zanbox.assets.get(Assets.CHARSET_TEXTURE);
    TextureRegion[] regions = {regionA, regionB, regionC};

    Animation animation = new Animation(0.25f, charAtlas.findRegions("dummy"));
    animation.setPlayMode(Animation.PlayMode.LOOP);
/*    this.animatedSprite3D = modelBatch.build(animation, true, true);
    animatedSprite3D.set(1,0.5f,1);
    this.sprites = new Array<BaseSprite3D>();
    sprites.add(animatedSprite3D);
    for(int i = 0; i < 0; i++) {
      TextureRegion region = regions[i % regions.length];

      Sprite3D sprite = modelBatch.build(region, true, true);
      sprite.set((float)Math.random() * 10,(float)Math.random() * 10,(float) Math.random() * 10);
      sprite.scale(1 + (float) Math.random() * 5, 1 + (float) Math.random() * 5);
      //sprite.setTransparent(true);
      sprites.add(sprite);
    }

    modelBatch.debug();
*/

    MeshAssembler meshAssembler = new MeshAssembler();
    TextureRegion region        = terrainAtlas.findRegion("debug");

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


    chunkRenderable = new TerrainRenderable();
    chunkRenderable.mesh = mesh;
    chunkRenderable.primitiveType = GL30.GL_TRIANGLES;
    chunkRenderable.meshPartSize   = mesh.getNumIndices();
    chunkRenderable.meshPartOffset = 0;
    chunkRenderable.material = new Material(TextureAttribute.createDiffuse(region.getTexture()));


    this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

    camera.position.set(0,5,0);
    camera.lookAt(Vector3.Zero);
    camera.update(true);

    cameraController = new CameraInputController(camera);
    Gdx.input.setInputProcessor(cameraController);

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

    gameLevel.update(delta);

    gameLevel.renderContext.begin(); {
      gameLevel.modelBatch.begin(gameLevel.camera); {
        gameLevel.modelBatch.render(chunkRenderable);
      } gameLevel.modelBatch.end();
    } gameLevel.renderContext.end();

    gameLevel.render();
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
    modelBatch.dispose();
  }
}
