uniform sampler2D u_diffuseTexture;
uniform vec4  u_ambientLight;
uniform vec4  u_sunLightColor;
varying float v_opacity;
varying vec2  v_texCoords0;
varying float v_alphaTest;
void main() {
  vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords0);
  vec4 final   = diffuse * (u_ambientLight + u_sunLightColor);
  final.a      *= v_opacity;

  gl_FragColor = final;
  if (gl_FragColor.a <= v_alphaTest)
    discard;
}