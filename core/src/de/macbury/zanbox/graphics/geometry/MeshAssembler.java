package de.macbury.zanbox.graphics.geometry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;

/**
 * Created by macbury on 15.03.14.
 */
public class MeshAssembler implements Disposable {
  private static final String BARYCENTRIC_ATTRIBUTE = "a_barycentric";
  private static final String SHADE_ATTRIBUTE = "a_shade";
  private final Pool<MeshVertexData> meshVertexPool      = Pools.get(MeshVertexData.class);
  private final Pool<MeshVertexIndex> vertexIndeciesPool = Pools.get(MeshVertexIndex.class);

  private ArrayList<MeshVertexIndex> vertexIndexes;
  private ArrayList<MeshVertexData> vertexsList;
  private ArrayList<MeshVertexData.AttributeType> attributeTypes;
  private boolean started;
  private MeshVertexData currentVertex;
  protected float[] verties;
  protected short[] indices;
  protected ArrayList<VertexAttribute> attributes;
  private boolean empty;

  public MeshVertexData topLeftVertex;
  public MeshVertexData topRightVertex;
  public MeshVertexData bottomLeftVertex;
  public MeshVertexData bottomRightVertex;

  public MeshAssembler() {
    this.vertexsList      = new ArrayList<MeshVertexData>();
    this.attributeTypes   = new ArrayList<MeshVertexData.AttributeType>();
    this.vertexIndexes    = new ArrayList<MeshVertexIndex>();
  }

  public void using(MeshVertexData.AttributeType type) {
    if (!isUsing(type)) {
      this.attributeTypes.add(type);
    }
  }

  public boolean isUsing(MeshVertexData.AttributeType type) {
    return (this.attributeTypes.indexOf(type) >= 0);
  }

  public int getAttributesPerVertex() {
    int count = 0;

    if (isUsing(MeshVertexData.AttributeType.Position)) {
      count+=3;
    }

    if (isUsing(MeshVertexData.AttributeType.Normal)) {
      count+=3;
    }

    if (isUsing(MeshVertexData.AttributeType.TextureCord)) {
      count+=2;
    }

    if (isUsing(MeshVertexData.AttributeType.Color)) {
      count+=1;
    }

    if (isUsing(MeshVertexData.AttributeType.Shading)) {
      count+=2;
    }

    if (isUsing(MeshVertexData.AttributeType.Barycentric)) {
      count+=2;
    }

    return count;
  }

  public void clear() {
    for(MeshVertexData vert : vertexsList)
      meshVertexPool.free(vert);

    for(MeshVertexIndex index : vertexIndexes)
      vertexIndeciesPool.free(index);

    this.attributeTypes.clear();
    this.vertexsList.clear();
    this.vertexIndexes.clear();

    started = false;
    currentVertex = null;
  }

  @Override
  public void dispose() {
    clear();
  }

  public void checkIfStarted() {
    if (!this.started) {
      throw new GdxRuntimeException("Call begin() first!");
    }
  }

  public void begin() {
    clear();
    if (this.started) {
      throw new GdxRuntimeException("Already started building geometry! Call end() first!");
    }
    this.started = true;
  }

  public int vertex(float x, float y, float z) {
    checkIfStarted();
    currentVertex = meshVertexPool.obtain();
    currentVertex.position.set(x, y, z);
    using(MeshVertexData.AttributeType.Position);
    this.vertexsList.add(currentVertex);

    return this.vertexsList.size()-1;
  }

  public int vertex(Vector3 vector) {
    return vertex(vector.x, vector.y, vector.z);
  }

  public int vertexOrSimilar(Vector3 vector) {
    return vertexOrSimilar(vector.x, vector.y, vector.z);
  }

  public int vertexOrSimilar(float x, float y, float z) {
    currentVertex = meshVertexPool.obtain();
    currentVertex.position.set(x, y, z);
    using(MeshVertexData.AttributeType.Position);

    int index = this.vertexsList.indexOf(currentVertex);

    if (index == -1) {
      vertexsList.add(currentVertex);
      return this.vertexsList.size()-1;
    } else {
      meshVertexPool.free(currentVertex);
      currentVertex = this.vertexsList.get(index);
      return index;
    }
  }

  public void uv(float u, float v) {
    checkIfStarted();
    using(MeshVertexData.AttributeType.TextureCord);
    currentVertex.uv.set(u, v);
  }

  public void uv(Vector2 vector2) {
    uv(vector2.x, vector2.y);
  }

  public void normal(float x, float y, float z) {
    checkIfStarted();
    currentVertex.normal.set(x, y, z);
    using(MeshVertexData.AttributeType.Normal);
  }

  public void barycenric(float x, float y) {
    checkIfStarted();
    currentVertex.barycentric.set(x, y);
    using(MeshVertexData.AttributeType.Barycentric);
  }

  public void topRightBarycentric() {
    barycenric(1,0);
  }

  public void topLeftBarycentric() {
    barycenric(0,0);
  }

  public void bottomRightBarycentric() {
    barycenric(1,1);
  }

  public void bottomLeftBarycentric() {
    barycenric(0,1);
  }


  public void shade() {
    currentVertex.shade = true;
  }

  public void unshade() {
    currentVertex.shade = true;
  }


  public void color(float r, float g, float b, float a) {
    using(MeshVertexData.AttributeType.Color);
    currentVertex.color.set(r, g, b, a);
  }

  public void indices(int n1, int n2, int n3)   {
    checkIfStarted();

    MeshVertexIndex index = vertexIndeciesPool.obtain();
    index.set(n1,n2,n3);
    vertexIndexes.add(index);
  }

  public void end() {
    checkIfStarted();
    this.started = false;

    this.verties = new float[this.vertexsList.size() * getAttributesPerVertex()];
    this.indices = new short[this.vertexIndexes.size() * 3];

    boolean usingTextCord       = isUsing(MeshVertexData.AttributeType.TextureCord);
    boolean usingNormals        = isUsing(MeshVertexData.AttributeType.Normal);
    boolean usingColor          = isUsing(MeshVertexData.AttributeType.Color);
    boolean usingShade          = isUsing(MeshVertexData.AttributeType.Shading);
    boolean usingBarycentric    = isUsing(MeshVertexData.AttributeType.Barycentric);

    int cursor = 0;
    for (MeshVertexIndex index : this.vertexIndexes) {
      this.indices[cursor++] = index.n1;
      this.indices[cursor++] = index.n2;
      this.indices[cursor++] = index.n3;
    }

    if (usingNormals) {
     // calculateNormals();
    }

    int vertexCursor = 0;
    for (MeshVertexData vertex : this.vertexsList) {
      this.verties[vertexCursor++] = vertex.position.x;
      this.verties[vertexCursor++] = vertex.position.y;
      this.verties[vertexCursor++] = vertex.position.z;

      if (usingNormals) {
        vertex.normal.nor();
        this.verties[vertexCursor++] = vertex.normal.x;
        this.verties[vertexCursor++] = vertex.normal.y;
        this.verties[vertexCursor++] = vertex.normal.z;
      }

      if (usingTextCord) {
        this.verties[vertexCursor++] = vertex.uv.x;
        this.verties[vertexCursor++] = vertex.uv.y;
      }

      if (usingColor) {
        this.verties[vertexCursor++] = Color.toFloatBits(vertex.color.r, vertex.color.g, vertex.color.b, vertex.color.a);
      }

      if (usingShade) {
        this.verties[vertexCursor++] = vertex.shade ? 0.8f : 0;
        this.verties[vertexCursor++] = vertex.shade ? 0.8f : 0;
      }

      if (usingBarycentric) {
        this.verties[vertexCursor++] = vertex.barycentric.x;
        this.verties[vertexCursor++] = vertex.barycentric.y;
      }
    }
  }

  public void calculateNormals() {
    for (int i = 0; i < indices.length-2; i+=3) {
      int index1 = indices[i];
      int index2 = indices[i + 1];
      int index3 = indices[i + 2];

      Vector3 v0 = this.vertexsList.get(index1).position.cpy();
      Vector3 v1 = this.vertexsList.get(index2).position.cpy();
      Vector3 v2 = this.vertexsList.get(index3).position.cpy();

      Vector3 normal = v2.sub(v0).crs(v1.sub(v0)).nor();

      this.vertexsList.get(index1).normal.set(normal);
      this.vertexsList.get(index2).normal.set(normal);
      this.vertexsList.get(index3).normal.add(normal);
    }
  }

  public float[] getVerties() {
    return verties;
  }

  public short[] getIndices() {
    return indices;
  }

  public VertexAttribute[] getVertexAttributes() {
    if (attributes == null){
      this.attributes = new ArrayList<VertexAttribute>();
    }
    this.attributes.clear();

    if (isUsing(MeshVertexData.AttributeType.Position)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE));
    }

    if (isUsing(MeshVertexData.AttributeType.Normal)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.Normal, 3, ShaderProgram.NORMAL_ATTRIBUTE));
    }

    if (isUsing(MeshVertexData.AttributeType.TextureCord)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE+ "0"));
    }

    if (isUsing(MeshVertexData.AttributeType.Color)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE));
    }

    if (isUsing(MeshVertexData.AttributeType.Shading)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.Generic, 2, SHADE_ATTRIBUTE));
    }

    if (isUsing(MeshVertexData.AttributeType.Barycentric)) {
      attributes.add(new VertexAttribute(VertexAttributes.Usage.Tangent, 2, BARYCENTRIC_ATTRIBUTE));
    }

    return attributes.toArray(new VertexAttribute[attributes.size()]);
  }

  public void summary() {
    Gdx.app.log("MeshAssembler", "Vert: " + vertexsList.size() + " Indecies " + vertexIndexes.size());
  }

  public MeshVertexData getVertexData(int index) {
    return this.vertexsList.get(index);
  }

  public void topFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2) {
    int n1 = this.vertex(x, y, z); //top left
    uv(u, v2);
    normal(0,1,0);
    topLeftBarycentric();
    this.topLeftVertex = currentVertex;

    int n2 = this.vertex(x, y, z + height); // bottom left
    uv(u, v);
    normal(0,1,0);
    bottomLeftBarycentric();
    this.bottomLeftVertex = currentVertex;

    int n3 = this.vertex(x + width, y, z); //top right
    uv(u2,v2);
    normal(0,1,0);
    indices(n1, n2, n3);
    topRightBarycentric();
    this.topRightVertex = currentVertex;

    n1 = this.vertex(x + width, y, z + height); //bottom right
    indices(n3, n2, n1);
    normal(0,1,0);
    uv(u2,v);
    bottomRightBarycentric();
    this.bottomRightVertex = currentVertex;
  }

  public void frontFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2, boolean outside) {
    int normalMode = outside ? -1 : 1;
    int n1 = this.vertex(x, y,  z+depth); // bottom left
    uv(u, v2);
    normal(0,0,1*normalMode);
    bottomLeftBarycentric();
    this.bottomLeftVertex = currentVertex;
    int n2 = this.vertex(x+width, y,  z+depth); //bottom right
    uv(u2, v2);
    normal(0,0,1*normalMode);
    bottomRightBarycentric();
    this.bottomRightVertex = currentVertex;
    int n3 = this.vertex(x+width,  y+height,  z+depth); //top right
    uv(u2,v);
    normal(0,0,1*normalMode);
    topRightBarycentric();
    this.topRightVertex = currentVertex;
    if (outside) {
      indices(n3, n2, n1);
    } else {
      indices(n1, n2, n3);
    }
    n2 = this.vertex(x,  y+height,  z+depth); //top left
    normal(0, 0, 1*normalMode);
    topLeftBarycentric();
    if (outside) {
      indices(n2, n3, n1);
    } else {
      indices(n1, n3, n2);
    }
    uv(u, v);
    this.topLeftVertex = currentVertex;
  }

  public void backFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2, boolean outside) {
    int normalMode = outside ? -1 : 1;
    int n1 = this.vertex(x, y,  z);// bottom left
    bottomLeftBarycentric();
    this.bottomLeftVertex = currentVertex;
    normal(0,0,-1 * normalMode);
    uv(u, v2);
    int n2 = this.vertex(x, y+height,  z); // top left
    topLeftBarycentric();
    this.topLeftVertex = currentVertex;
    uv(u, v);
    normal(0,0,-1 * normalMode);
    int n3 = this.vertex(x+width,  y+height,  z); // top right
    topRightBarycentric();
    this.topRightVertex = currentVertex;
    uv(u2,v);
    normal(0,0,-1 * normalMode);
    if (outside) {
      indices(n3, n2, n1);
    } else {
      indices(n1, n2, n3);
    }

    n2 = this.vertex(x+width,  y,  z); //bottom right
    bottomRightBarycentric();
    this.bottomRightVertex = currentVertex;
    if (outside) {
      indices(n2, n3, n1);
    } else {
      indices(n1, n3, n2);
    }

    normal(0,0,-1 * normalMode);
    uv(u2,v2);
  }

  public void leftFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2, boolean outside) {
    int normalMode = outside ? 1 : -1;
    int n1 = this.vertex(x, y,  z); // bottom left
    this.bottomLeftVertex = currentVertex;
    bottomLeftBarycentric();
    uv(u, v2);
    normal(-1 * normalMode,0,0);
    int n2 = this.vertex(x, y,  z+depth); //bottom right
    bottomRightBarycentric();
    this.bottomRightVertex = currentVertex;
    uv(u2, v2);
    normal(-1 * normalMode,0,0);
    int n3 = this.vertex(x, y+height, z+depth); //top right
    topRightBarycentric();
    this.topRightVertex = currentVertex;
    uv(u2, v);
    normal(-1 * normalMode, 0, 0);

    if (outside) {
      indices(n3, n2, n1);
    } else {
      indices(n1, n2, n3);
    }

    n2 = this.vertex(x,  y+height,  z); // top left
    this.topLeftVertex = currentVertex;
    topLeftBarycentric();
    uv(u,v);
    normal(-1 * normalMode,0,0);

    if (outside) {
      indices(n2, n3, n1);
    } else {
      indices(n1, n3, n2);
    }
  }

  public void rightFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2, boolean outside) {
    int normalMode = outside ? 1 : -1;
    int n1 = this.vertex(x+width, y, z); //bottom right
    uv(u2, v2);
    normal(1*normalMode,0,0);
    bottomRightBarycentric();
    this.bottomRightVertex = currentVertex;
    int n2 = this.vertex(x+width, y+height, z); //top right
    this.topRightVertex = currentVertex;
    uv(u2, v);
    topRightBarycentric();
    normal(1*normalMode,0,0);

    int n3 = this.vertex(x+width, y+height, z+depth); //top left
    this.topLeftVertex = currentVertex;
    uv(u, v);
    topLeftBarycentric();
    if (outside) {
      indices(n3, n2, n1);
    } else {
      indices(n1, n2, n3);
    }

    normal(1*normalMode, 0, 0);

    n2 = this.vertex(x+width, y, z+depth); // bottom left
    this.bottomLeftVertex = this.currentVertex;
    uv(u,v2);
    normal(1*normalMode,0,0);
    bottomLeftBarycentric();
    if (outside) {
      indices(n2, n3, n1);
    } else {
      indices(n1, n3, n2);
    }
  }

  public void frontSlopeFace(float x, float y, float z, float width, float height, float depth, float u, float v, float u2, float v2) {
    int n1 = this.vertex(x, y,  z+depth); // bottom left
    uv(u, v2);
    normal(0,0,1);
    this.bottomLeftVertex = this.currentVertex;
    int n2 = this.vertex(x+width, y,  z+depth); //bottom right
    this.bottomRightVertex = this.currentVertex;
    uv(u2, v2);
    normal(0,0,1);

    int n3 = this.vertex(x+width,  y+height,  z); //top right
    this.topRightVertex = this.currentVertex;
    uv(u2,v);
    normal(0,0,1);
    indices(n1, n2, n3);

    n2 = this.vertex(x,  y+height,  z); //top left
    this.topLeftVertex = this.currentVertex;
    normal(0, 0, 1);
    indices(n1, n3, n2);
    uv(u, v);
  }

  public boolean isEmpty() {
    return this.vertexsList.size() == 0;
  }
}
