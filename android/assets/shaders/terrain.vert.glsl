//= require fog
//= require lighting

uniform DirectionalLight u_mainLight;
uniform vec4  u_ambientLight;

uniform mat3  u_normalMatrix;
uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec2 a_shade;
attribute vec3 a_normal;

varying vec2   v_texCoords0;
varying float  v_shadeFactory;
varying vec3   v_normal;
varying vec3   v_lightDiffuse;
varying vec3   v_position;

void main() {
    v_texCoords0   = a_texCoord0;
    v_shadeFactory = a_shade.x;
    v_normal       = normalize(u_normalMatrix * a_normal);
    v_lightDiffuse = u_ambientLight.rgb + directionalLightDiffuse(u_mainLight, v_normal);

    vec4 pos       = u_worldTrans * vec4(a_position, 1.0);
    v_position     = pos.xyz;
    gl_Position    = u_projViewTrans * pos;
}