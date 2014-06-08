//= require lighting

uniform DirectionalLight u_mainLight;

uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;

uniform vec4 u_ambientLight;

uniform float u_opacity;
uniform float u_alphaTest;

attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2   v_texCoords0;
varying float  v_opacity;
varying float  v_alphaTest;
varying vec3   v_lightDiffuse;

void main() {
    v_texCoords0    = a_texCoord0;
    v_opacity       = u_opacity;
    v_alphaTest     = u_alphaTest;
    v_lightDiffuse  = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, vec3(0,1,0));
    vec4 pos        = u_worldTrans * vec4(a_position, 1.0);
    gl_Position     = u_projViewTrans * pos;
}