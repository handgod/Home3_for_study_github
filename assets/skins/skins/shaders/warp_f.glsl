//#version 100
precision highp float;

varying vec3 vTexCoord;
varying vec4 vPosition;
uniform sampler2D Tex0;
uniform vec4 uColor;
uniform vec3 uFactor;
uniform float uSkew;
uniform vec2 uSkewFactor;
uniform vec2 uVtoParam;
uniform vec2 uVtoParamInv;

vec3 Cube(float x)
{
    float x2 = x * x;
    return vec3(x, x2, x2 * x);
}

void main()
{
//    vec2 NDC = vPosition.xy / vPosition.w;
//    NDC = (NDC + 1.0) * 0.5;
    float t = uVtoParam.x * vTexCoord.y + uVtoParam.y;
    float dS = uSkew * (t * t) * (uSkewFactor.x + t * uSkewFactor.y);
    float k = 1.0 / (1.0 - 2.0 * dS);
    float b = -k * dS;
    float s = k * vTexCoord.x + b;
    vec2 tc = vec2(s, uVtoParamInv.x * dot(uFactor, Cube(t)) + uVtoParamInv.y);
    gl_FragColor = texture2D(Tex0, tc) * uColor;
//    gl_FragColor = texture2D(Tex0, vTexCoord.xy) * uColor;
//    gl_FragColor = texture2D(Tex0, NDC) * uColor;
//    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
