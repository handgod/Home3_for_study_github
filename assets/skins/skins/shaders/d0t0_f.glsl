//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec4 vLighting;
uniform sampler2D Tex0;
uniform vec4 uColor;

void main()
{
    gl_FragColor = texture2D(Tex0, vTexCoord) * vec4(vLighting.www, 1.0) * uColor;
//  gl_FragColor = vec4(vLighting.www, 1.0);
//  gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
