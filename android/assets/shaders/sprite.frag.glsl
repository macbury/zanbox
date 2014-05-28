uniform sampler2D u_diffuseTexture;

varying float v_opacity;
varying vec2  v_texCoords0;
varying float v_alphaTest;
void main() {
  vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords0);
  diffuse.a    *= v_opacity;
  gl_FragColor = diffuse;
  if (gl_FragColor.a <= v_alphaTest)
    discard;
}