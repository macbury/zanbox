struct DirectionalLight {
  vec3 direction;
  vec4 color;
};

vec3 directionalLightDiffuse(DirectionalLight source, vec3 normal) {
  vec3 lightDir = -source.direction;
  float NdotL   = clamp(dot(normal, lightDir), 0.0f, 1.0f);
  vec3 value    = source.color.rgb * NdotL;
  return value;
}