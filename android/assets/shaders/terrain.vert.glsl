uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;

attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2   v_texCoords0;

void main() {
    v_texCoords0 = a_texCoord0;

    vec4 pos     = u_worldTrans * vec4(a_position, 1.0);
    gl_Position  = u_projViewTrans * pos;
}