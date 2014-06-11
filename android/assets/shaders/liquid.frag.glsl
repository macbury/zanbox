//= require lighting
//= require normalmap
uniform DirectionalLight u_mainLight;

//uniform mat3      u_normalMatrix;
uniform vec4      u_ambientLight;
uniform sampler2D u_diffuseTexture;
//uniform sampler2D u_normalMap;
uniform float     u_opacity;
varying vec2      v_texCoords0;
varying vec4      v_shadeColor;
varying float     v_shadeFactory;
varying vec3      v_normal;
varying vec3      v_lightDiffuse;
varying vec4      v_position;
varying vec2      v_normalCoords0;

void main() {
  vec4 diffuse      = texture2D(u_diffuseTexture, v_texCoords0);
    //vec3 normal       = normalize(u_normalMatrix * colorToNormal(normalColor, true) + v_normal);
  vec3 lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal);

  vec4 final       = vec4(0,0,0,u_opacity);//TODO: change this
  final.rgb        = diffuse.rgb * lightDiffuse;
  gl_FragColor     = final;
}