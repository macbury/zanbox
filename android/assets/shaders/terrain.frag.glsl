//= require ao
uniform sampler2D u_diffuseTexture;
uniform vec4      u_shadeColor;

varying vec2      v_texCoords0;
varying vec4      v_shadeColor;
varying float     v_shadeFactory;
varying vec3      v_normal;
varying vec3      v_lightDiffuse;
varying vec3      v_position;
void main() {
  vec4 diffuse      = texture2D(u_diffuseTexture, v_texCoords0);
  vec4 final        = diffuse * ao(v_shadeFactory, u_shadeColor) * vec4(v_lightDiffuse.rgb, 1f);
  gl_FragColor      = final;
}