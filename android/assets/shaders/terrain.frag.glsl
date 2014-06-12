//= require ao

uniform sampler2D u_diffuseTexture;
uniform vec4      u_shadeColor;

varying vec2      v_texCoords0;
varying float     v_shadeFactory;
varying vec3      v_normal;
varying vec3      v_lightDiffuse;
varying vec3      v_position;
varying vec2      v_barycentric;


void main() {
  vec2 borderSize        = vec2(0.2f, 0.2f);
  vec2 borderStart       = vec2(borderSize);
  vec2 borderEnd         = vec2(1f, 1f) - borderStart;

  float borderStartDistance = distance(borderStart, vec2(0f,0f));
  float factor              = distance(borderStart, v_barycentric);

  vec4 shade             = ao(v_shadeFactory, u_shadeColor);
  vec4 diffuse           = texture2D(u_diffuseTexture, v_texCoords0);
  vec4 final             = diffuse * shade * vec4(v_lightDiffuse.rgb, 1f);

  final.r = v_barycentric.x;
  final.g = v_barycentric.y;
  final.b = 0f;
/*
  if (v_shadeFactory > 0.0f) {
    if (any(lessThanEqual(v_barycentric, borderStart)) || any(greaterThanEqual(v_barycentric, borderEnd))) {
      final = final * u_shadeColor;
    }
  }*/

  gl_FragColor      = final ;
}
