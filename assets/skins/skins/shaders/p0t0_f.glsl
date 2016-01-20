//#version 100
precision stdp float;

varying vec2 vTexCoord;
varying vec4 vLighting;
uniform sampler2D Tex0;
uniform vec4 uColor;

void main()
{
	vec4 resColor = texture2D(Tex0, vTexCoord) * uColor;
	gl_FragColor  = vec4(resColor.xyz * vLighting.w, resColor.w);
//	gl_FragColor = vLighting;
//	gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}
