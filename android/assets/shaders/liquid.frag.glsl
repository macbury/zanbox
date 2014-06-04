uniform sampler2D u_diffuseTexture;
uniform float     u_opacity;
varying vec2      v_texCoords0;
varying vec4      v_shadeColor;
varying float     v_shadeFactory;
varying vec3      v_normal;
varying vec3      v_lightDiffuse;

void main() {
  vec4 diffuse     = texture2D(u_diffuseTexture, v_texCoords0);
  vec4 final       = vec4(0,0,0,u_opacity);//TODO: change this
  final.rgb        = diffuse.rgb * v_lightDiffuse;
  gl_FragColor     = final;
}