uniform sampler2D u_diffuseTexture;


varying float v_opacity;
varying vec2  v_texCoords0;
varying float v_alphaTest;

varying vec3   v_lightDiffuse;

void main() {
  vec4 diffuse = texture2D(u_diffuseTexture, v_texCoords0);
  vec4 final   = vec4(diffuse.rgb * v_lightDiffuse, diffuse.a * v_opacity);
  gl_FragColor = final;
  if (gl_FragColor.a <= v_alphaTest)
    discard;
}