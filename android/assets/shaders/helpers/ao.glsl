vec4 ao(float a, vec4 color) {
  vec4 ao = mix(vec4(1f,1f,1f,0f), color, pow(a,2.0));
  return ao;
}
