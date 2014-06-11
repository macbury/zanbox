
float powInterpolation(float a) {
  float power       = 2f;
  float factor      = pow(a, power);

  return factor;
}

float expInterpolation(float alpha) {
  float power       = 10f;
  float value       = 2f;
  float min         = pow(value, -power);
  float scale       = 1f / (1f-min);
  float factor      = (pow(value, power * (alpha - 1f)) - min) * scale;
  return factor;
}

vec4 ao(float alpha, vec4 color) {
  vec4 ao = mix(vec4(1,1,1,0), color, powInterpolation(alpha));
  return ao;
}
