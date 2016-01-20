//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec2 vTexCoord1;
uniform sampler2D Tex0;
uniform sampler2D Tex1;
uniform vec4 uColor;
uniform float uFactor;

void main()
{
	vec4 T0 = texture2D(Tex0, vTexCoord);
	vec4 T1 = texture2D(Tex1, vTexCoord1);
	gl_FragColor = mix(T0, T1, uFactor) * uColor;
//	gl_FragColor = vec4(Factor, Factor, Factor, 1.0);
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
