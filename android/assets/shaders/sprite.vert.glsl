uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;

uniform vec4 u_sunLightColor;
uniform vec3 u_sunLightDirection;
uniform vec4 u_ambientLight;

uniform float u_opacity;
uniform float u_alphaTest;

attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2   v_texCoords0;
varying float  v_opacity;
varying float  v_alphaTest;
varying vec3   v_lightDiffuse;

vec3 sunLightDiffuse(vec3 normal) {
  vec3 lightDir = -u_sunLightDirection;
  float NdotL   = clamp(dot(normal, lightDir), 0.0, 1.0);
  vec3 value    = u_sunLightColor.rgb * NdotL;
  return value;
}

void main() {
    v_texCoords0    = a_texCoord0;
    v_opacity       = u_opacity;
    v_alphaTest     = u_alphaTest;
    v_lightDiffuse  = u_ambientLight.rgb + sunLightDiffuse(vec3(0,1,0));
    vec4 pos        = u_worldTrans * vec4(a_position, 1.0);
    gl_Position     = u_projViewTrans * pos;
}