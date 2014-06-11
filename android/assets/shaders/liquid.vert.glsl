uniform mat3  u_normalMatrix;
uniform mat4  u_projViewTrans;
uniform mat4  u_worldTrans;

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec3 a_normal;

varying vec2   v_texCoords0;
varying vec3   v_normal;
varying vec4   v_position;
varying vec2   v_normalCoords0;

void main() {
    v_texCoords0    = a_texCoord0;
    v_normal        = normalize(u_normalMatrix * a_normal);
    v_position      = u_worldTrans * vec4(a_position, 1.0);
    v_normalCoords0 = v_position.xz * vec2(0.1,0.1);
    gl_Position     = u_projViewTrans * v_position;
}