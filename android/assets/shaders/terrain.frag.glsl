uniform sampler2D u_diffuseTexture;

varying vec2  v_texCoords0;

void main() {
  vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords0);
  gl_FragColor = diffuse;
}