uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;
uniform float u_opacity;
uniform float u_alphaTest;

attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2   v_texCoords0;
varying float  v_opacity;
varying float  v_alphaTest;

void main() {
    v_texCoords0 = a_texCoord0;
    v_opacity    = u_opacity;
    v_alphaTest  = u_alphaTest;

    vec4 pos     = u_worldTrans * vec4(a_position, 1.0);
    gl_Position  = u_projViewTrans * pos;
}